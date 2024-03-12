package com.fams.fams.services.impl;

import com.fams.fams.models.entities.StudentClass;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.StudentDetailsInClassDto;
import com.fams.fams.repositories.StudentClassRepository;
import com.fams.fams.services.StudentClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentClassServiceImpl implements StudentClassService {
    private final StudentClassRepository studentClassRepository;
    private final ModelMapper mapper;

    @Autowired
    public StudentClassServiceImpl(StudentClassRepository studentClassRepository, ModelMapper mapper) {
        this.studentClassRepository = studentClassRepository;
        this.mapper = mapper;
    }

    @Override
    public void updateStatusStudents(List<Long> studentIds, Long classId, String attendingStatus) {
        if(studentIds.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"Student not found!");
        }
        List<StudentClass> studentClass = studentClassRepository.getStudentClassByClassId(classId);
        if(studentClass.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!");
        }

        //Check StudentId not exist
        List<Long> studentNotExist = new ArrayList<>();
        for(Long stdIds: studentIds){
            int count = 0;
            for (StudentClass stds: studentClass){
                count++;
                if(stdIds.equals(stds.getStudents().getStudentId())){
                    break;
                }else if(!stdIds.equals(stds.getStudents().getStudentId()) && count == studentClass.size()){
                    studentNotExist.add(stdIds);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        if(!studentNotExist.isEmpty()){
            for (Long tmp : studentNotExist) {
                sb.append(tmp);
                sb.append(", "); // You can change the separator as needed
            }

            // Remove the last separator ", " if it's not needed
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            throw new FamsApiException(HttpStatus.NOT_FOUND,"StudentIds " + sb + " not exist in Class!");
        }

        List<StudentClass> studentChange = studentClass.stream().filter(obj1 -> studentIds.stream().anyMatch(obj2 -> obj2.equals(obj1.getStudents().getStudentId()))).toList();

        for(StudentClass student: studentChange){
            student.setAttendingStatus(attendingStatus);
            studentClassRepository.save(student);
        }
    }

    @Override
    public void updateStatusStudent(Long studentIds, Long classId, String attendingStatus) {
        StudentClass studentClass = studentClassRepository.findAllByStudentsAndClasses(classId, studentIds);
        if(studentClass == null){
            throw new FamsApiException(HttpStatus.NOT_FOUND,"Student is not in this Class");
        }
        String attending = studentClass.getAttendingStatus();
        if (studentClass.getAttendingStatus().equals(attendingStatus)) {
            throw new FamsApiException(HttpStatus.CONFLICT,"Status is already " + studentClass.getAttendingStatus() + "!!!");
        } else if (attendingStatus.equals("DropOut")) {
            if(attending.equals("InClass") || attending.equals("BackToClass") || attending.equals("Reservation")){
                studentClass.setAttendingStatus(attendingStatus);
            }else {
                throw new FamsApiException(HttpStatus.CONFLICT,"Cannot update status!!!");
            }
        }else {
            studentClass.setAttendingStatus(attendingStatus);
        }
        studentClassRepository.save(studentClass);
    }

    @Override
    public StudentDetailsInClassDto getStudentDetailsInClass(Long studentClassId) {
        StudentClass studentClass = studentClassRepository.findById(studentClassId).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND, "Student not found!!!"));
        StudentDetailsInClassDto studentDetailsDto = mapper.map(studentClass.getStudents(), StudentDetailsInClassDto.class);
        studentDetailsDto.setSchool(studentClass.getStudents().getSchool());
        studentDetailsDto.setAddress(studentClass.getStudents().getAddress());
        studentDetailsDto.setCertificationDate(studentClass.getCertificationDate());
        studentDetailsDto.setCertificationStatus(studentClass.getCertificationStatus());
        return studentDetailsDto;
    }
}
