package com.fams.fams.services.impl;

import com.fams.fams.models.entities.*;
import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.Module;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.ReservedClassDto;
import com.fams.fams.models.payload.requestModel.ReservedClassAddNew;
import com.fams.fams.models.payload.responseModel.ReservedClassList;
import com.fams.fams.repositories.*;
import com.fams.fams.services.EmailSendService;
import com.fams.fams.services.ReservedClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservedClassServiceImpl implements ReservedClassService {

    public final ReservedClassRepository reservedClassRepository;
    public final ClassRepository classRepository;
    public final StudentRepository studentRepository;
    public final EmailSendService emailSendService;
    public final StudentClassRepository studentClassRepository;
    public final TrainingProgramModuleRepository trainingProgramModuleRepository;
    public final ModelMapper mapper;

    @Autowired
    public ReservedClassServiceImpl(ReservedClassRepository reservedClassRepository, ClassRepository classRepository, StudentRepository studentRepository, EmailSendService emailSendService, StudentClassRepository studentClassRepository, TrainingProgramModuleRepository trainingProgramModuleRepository, ModelMapper mapper) {
        this.reservedClassRepository = reservedClassRepository;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.emailSendService = emailSendService;
        this.studentClassRepository = studentClassRepository;
        this.trainingProgramModuleRepository = trainingProgramModuleRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<ReservedClassList> getAllStudents(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ReservedClass> reservedClass = reservedClassRepository.findAll(pageable);
        if (reservedClass.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Students not found!");
        }else {
            return reservedClass.map(this::mapToReserveList);
        }
    }

    @Override
    public ReservedClassDto getReservationStudent(Long id) {
        ReservedClass reservedClass = reservedClassRepository.findById(id).orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!"));
        Optional<Student> student = studentRepository.findById(reservedClass.getStudents().getStudentId());
        Optional<Class> clasObj = classRepository.findById(reservedClass.getClasses().getClassId());
        ReservedClassDto reservedClassDto = mapToDto(Optional.of(reservedClass));
        reservedClassDto.setStudents(student.get());
        reservedClassDto.setClasses(clasObj.get());
        return reservedClassDto;
    }

    @Override
    public void addNewReservation(ReservedClassAddNew reservedClassAddNew) throws FamsApiException {
        //check Student exist
        studentRepository.findById(reservedClassAddNew.getStudentId())
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Student not found!"));

        //check Class exist
        classRepository.findById(reservedClassAddNew.getClassId())
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!"));

        //check time reserved, studentClass exist and reservedClass exist
        StudentClass studentClassExist = studentClassRepository.findAllByStudentsAndClasses(reservedClassAddNew.getClassId(), reservedClassAddNew.getStudentId());
        ReservedClass reservedClassExist =reservedClassRepository.findByClassIdAndStudentId(reservedClassAddNew.getClassId(), reservedClassAddNew.getStudentId());
        if(studentClassExist == null){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"Student not in this class!");
        } else if (reservedClassExist != null) {
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"This student have reserved!");
        }else if (reservedClassAddNew.getStartDate().isBefore(LocalDate.now())) {
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Reserving time is not allowed in Past!");
        }else if(reservedClassAddNew.getStartDate().isAfter(reservedClassAddNew.getEndDate())){
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Reserving endDate must be greater than startDate!");
        }else if(reservedClassAddNew.getStartDate().isEqual(reservedClassAddNew.getEndDate())){
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Reserving endDate must be greater than startDate!");
        }else if (reservedClassAddNew.getStartDate().plusMonths(6).isBefore(reservedClassAddNew.getEndDate())) {
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Reserving period is allowed below 6 months!");
        }

        if(studentClassExist.getAttendingStatus().equals("InClass")){
            studentClassExist.setAttendingStatus("Reservation");
            studentClassRepository.save(studentClassExist);
        }else{
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Cannot change status");
        }
        ReservedClass reservedClass = new ReservedClass();
        reservedClass.setClasses(classRepository.findById(reservedClassAddNew.getClassId()).get());
        reservedClass.setStudents(studentRepository.findById(reservedClassAddNew.getStudentId()).get());
        reservedClass.setStartDate(reservedClassAddNew.getStartDate());
        reservedClass.setEndDate(reservedClassAddNew.getEndDate());
        reservedClass.setReason(reservedClassAddNew.getReason());
        reservedClass.setStatus("Active");
        reservedClassRepository.save(reservedClass);

        emailSendService.sendSuccessful(reservedClass.getStudents().getStudentId(), reservedClass.getClasses().getClassId());
    }

    @Override
    public List<Class> findNewClassForReservedStudent(Long studentId, Long classId) {
        //check Student exist
        studentRepository.findById(studentId)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Student not found!"));
        //check Class exist
        Class classObj = classRepository.findById(classId)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!"));
        //check studentClass exist
        StudentClass studentClassExist = studentClassRepository.findAllByStudentsAndClasses(classId, studentId);
        ReservedClass reservedClassExist =reservedClassRepository.findByClassIdAndStudentId(classId, studentId);
        if(studentClassExist == null){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"Student not in this class!");
        }

        //find modules belong to class of that student
        TrainingProgram trainingProgram = classObj.getTrainingPrograms();
//        List<Module> modules = trainingProgramModuleRepository.findModuleByTrainingProgramsID(trainingProgram.getProgramID());

        //find new class have same modules
        List<Class> matchedClass = new ArrayList<>();
        List<Class> classes = classRepository.findAll();
        classes.remove(classObj);
        for(Class tmp: classes){
            TrainingProgram trainingProgramCur = tmp.getTrainingPrograms();
//            List<Module> modulesCur = trainingProgramModuleRepository.findModuleByTrainingProgramsID(trainingProgramCur.getProgramID());

            if(trainingProgramCur.equals(trainingProgram) && tmp.getStartDate().isAfter(reservedClassExist.getEndDate())){
                matchedClass.add(tmp);
            }
        }
        return matchedClass;
    }

    @Override
    public void updateStatusDropOut(Long reservedClassId, Long studentId, Long classId) {
        String attendingStatus = "DropOut";
        StudentClass studentClass = studentClassRepository.findAllByStudentsAndClasses(classId, studentId);
        ReservedClass reservedClassExist =reservedClassRepository.findByClassIdAndStudentIdAndReservedClassId(reservedClassId, classId, studentId);
        if(reservedClassExist == null){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"ReservedClass not found!");
        }
        String attending = studentClass.getAttendingStatus();
        if (studentClass.getAttendingStatus().equals(attendingStatus)) {
            throw new FamsApiException(HttpStatus.CONFLICT,"Status is already " + studentClass.getAttendingStatus() + "!!!");
        } else if (attendingStatus.equals("DropOut")) {
            if(attending.equals("InClass") || attending.equals("BackToClass") || attending.equals("Reservation")){
                studentClass.setAttendingStatus(attendingStatus);
                reservedClassExist.setStatus("Inactive");
            }else {
                throw new FamsApiException(HttpStatus.CONFLICT,"Cannot update status!!!");
            }
        }else {
            studentClass.setAttendingStatus(attendingStatus);
            reservedClassExist.setStatus("Inactive");
        }
        reservedClassRepository.save(reservedClassExist);
        studentClassRepository.save(studentClass);
    }

    @Override
    public void addReservedStudentIntoNewClass(Long reservedClassId, Long classId, Long studentId) {
        String statusInactive = "Inactive";
        //check Student exist
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Student not found!"));
        //check Class exist
        Class classObj = classRepository.findById(classId)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!"));
        //check ReservedClass exist
        ReservedClass reservedClass =reservedClassRepository.findById(reservedClassId)
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"ReservedClass not found!"));
        //check StudentClass exist
        StudentClass studentClassExist =studentClassRepository.findAllByStudentsAndClasses(classId, studentId);

        if(reservedClass.getStatus().equals(statusInactive)){
            throw new FamsApiException(HttpStatus.CONFLICT,"Student is already back to class!");
        }else if(classId.equals(reservedClass.getClasses().getClassId())) {
            throw new FamsApiException(HttpStatus.CONFLICT, "Cannot back to Old Class!");
        }else if(studentClassExist != null){
                throw new FamsApiException(HttpStatus.CONFLICT,"Student is already in Class!");
        }else{
            StudentClass studentClass = new StudentClass();
            studentClass.setStudents(student);
            studentClass.setClasses(classObj);
            studentClass.setAttendingStatus("BackToClass");
            studentClass.setResult(false);
            studentClass.setFinalScore(0f);
            studentClass.setGPALevel("F");
            studentClass.setCertificationStatus("Not yet");
            studentClass.setCertificationDate(studentClass.getClasses().getStartDate());
            studentClass.setMethod("Online");
            reservedClass.setStatus("Inactive");
            studentClassRepository.save(studentClass);
            reservedClassRepository.save(reservedClass);
        }

    }


    private ReservedClassDto mapToDto(Optional<ReservedClass> reservedClass){
        return mapper.map(reservedClass, ReservedClassDto.class);
    }

    private ReservedClassList mapToReserveList(ReservedClass reservedClass){
        List<Module> modules = trainingProgramModuleRepository.findModuleByTrainingProgramsID(reservedClass.getClasses().getTrainingPrograms().getProgramID());
        ReservedClassList reservedClassList = new ReservedClassList();
        reservedClassList.setId(reservedClass.getReservedclassid());
        reservedClassList.setFullName(reservedClass.getStudents().getFullName());
        reservedClassList.setStudentCode(reservedClass.getStudents().getStudentCode());
        reservedClassList.setGender(reservedClass.getStudents().getGender());
        reservedClassList.setDOB(reservedClass.getStudents().getDOB());
        reservedClassList.setLocation(reservedClass.getStudents().getAddress());
        reservedClassList.setClassName(reservedClass.getClasses().getClassName());
        reservedClassList.setModuleName(reservedClass.getClasses().getTrainingPrograms().getName());
        reservedClassList.setReason(reservedClass.getReason());
        reservedClassList.setStartDate(reservedClass.getStartDate());
        reservedClassList.setEndDate(reservedClass.getEndDate());
        reservedClassList.setStatus(reservedClass.getStatus());
        return reservedClassList;
    }

}
