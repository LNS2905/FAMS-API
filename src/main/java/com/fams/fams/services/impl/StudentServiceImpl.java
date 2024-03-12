package com.fams.fams.services.impl;

import com.fams.fams.models.entities.Student;
import com.fams.fams.models.entities.StudentClass;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.CertificateStatusDetailDto;
import com.fams.fams.models.payload.dto.StudentDetailsDto;
import com.fams.fams.models.payload.dto.StudentScorePerClassDto;
import com.fams.fams.repositories.impl.CustomRepositoryImp;
import com.fams.fams.models.payload.requestModel.StudentAddNew;
import com.fams.fams.repositories.StudentClassRepository;
import com.fams.fams.repositories.StudentRepository;
import com.fams.fams.services.ScoreService;
import com.fams.fams.services.StudentService;


import com.fams.fams.utils.Utils;
import jakarta.validation.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentClassRepository studentClassRepository;
    private final CustomRepositoryImp customRepository;
    private final ScoreService scoreService;
    private final ModelMapper mapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentClassRepository studentClassRepository, CustomRepositoryImp customRepository, ScoreService scoreService, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.studentClassRepository = studentClassRepository;
        this.customRepository = customRepository;
        this.scoreService = scoreService;
        this.mapper = mapper;
    }

    @Override
    public Page<Student> getStudentsByClassId(String classId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Student> students = studentClassRepository.getStudentsByClassId(classId, pageable);
        if(students.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Students not found!");
        }else {
            return students;
        }
    }
    @Override
    public StudentDetailsDto getStudentDetails(Long id){
        Student student = studentRepository.findById(id).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND, "Student not found!!!"));
        StudentDetailsDto studentDetailsDto = mapToDto(student);
        studentDetailsDto.setSchool(student.getSchool());
        studentDetailsDto.setAddress(student.getAddress());
        return studentDetailsDto;

    }

    @Override
    public StudentDetailsDto getStudentDetailsByStudentCode(String studentCode) {
        Student student = studentRepository.findByStudentCode(studentCode).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND, "Student not found!!!"));
        StudentDetailsDto studentDetailsDto = mapToDto(student);
        studentDetailsDto.setSchool(student.getSchool());
        studentDetailsDto.setAddress(student.getAddress());
        return studentDetailsDto;
    }

    @Override
    public void addNewStudent(StudentAddNew studentAddNew){
        List<Student> checkEmailExisted = studentRepository.findByEmail(studentAddNew.getEmail());
        if (!checkEmailExisted.isEmpty()){
            throw new FamsApiException(HttpStatus.CONFLICT,"Student's email existed!");
        }
        Student newStudent = mapToModelAdd(studentAddNew);
        newStudent.setStudentCode(Utils.generateStudentCode());
        try{
            newStudent.setFAAccount(Utils.generateFAAccount());
            newStudent.setStatus("Active");
            newStudent.setSchool(studentAddNew.getUniversity());
            newStudent.setAddress(studentAddNew.getLocation());
            newStudent.setType(Utils.generateStudentType());
            newStudent.setJoinedDate(LocalDate.now());
            studentRepository.save(newStudent);
        }catch (Exception e){
            throw new FamsApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Fail to save student!");
        }

    }

    @Override
    public Page<StudentDetailsDto> getAllStudents(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Student> students = studentRepository.findAllByOrderByStudentId(pageable);
        if(students.isEmpty()){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Students not found!");
        }else {
            return students.map(this::mapToDto);
        }
    }

    @Override
    public StudentDetailsDto deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND, "Student not found!!!"));
        StudentScorePerClassDto studentScorePerClassDto = scoreService.getStudentScoreDetail(id);
        if(student.getStatus().equals("Inactive") && studentScorePerClassDto.getGradeList() == null){
            student.setStatus("Disable");
            studentRepository.save(student);
        }else {
            throw new FamsApiException(HttpStatus.CONFLICT, "Cannot delete Student!");
        }
        return mapToDto(student);
    }

    @Override
    public List<StudentDetailsDto> deleteStudentsInBatch(List<Long> studentIds) {
        List<Student> studentsToDelete = new ArrayList<>();
        for (Long studentId : studentIds) {
            Optional<Student> studentOptional = Optional.ofNullable(studentRepository.findById(studentId)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "NOT FOUND STUDENT ID: " + studentId)));
            StudentScorePerClassDto studentScorePerClassDto = scoreService.getStudentScoreDetail(studentId);
            if(studentOptional.get().getStatus().equals("Inactive") && studentScorePerClassDto.getGradeList() == null){
                studentOptional.get().setStatus("Disable");
                studentsToDelete.add(studentOptional.get());
            }else {
                throw new FamsApiException(HttpStatus.CONFLICT, "Cannot delete Student!");
            }
        }
        if (studentsToDelete.isEmpty()) {
            throw new FamsApiException(HttpStatus.NOT_FOUND, "NOT FOUND STUDENT TO DELETE ");
        }
        List<StudentDetailsDto> deletedStudentDtos = studentsToDelete.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        studentRepository.saveAll(studentsToDelete);
        return deletedStudentDtos;
    }

    @Override
    public void editAStudent(StudentDetailsDto studentDetailsDto){

        Student student = studentRepository.findById(studentDetailsDto.getStudentId()).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND,"Unknown student!!"));
        List<Student> students = studentRepository.findAll();

        for(Student tmp: students){
            if(tmp.getStudentCode().equals(studentDetailsDto.getStudentCode()) && !tmp.getStudentId().equals(studentDetailsDto.getStudentId())){
                throw new FamsApiException(HttpStatus.CONFLICT, "Student's code existed!");
            } else if (tmp.getEmail().equals(studentDetailsDto.getEmail()) && !tmp.getStudentId().equals(studentDetailsDto.getStudentId())) {
                throw new FamsApiException(HttpStatus.CONFLICT,"Student's email existed!");
            }
        }

        Field[] fields = StudentDetailsDto.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(studentDetailsDto);
                if (value != null) {
                    if(field.getName().equalsIgnoreCase("university")){
                        updateStudentField(student, "school", value);
                    }else if(field.getName().equalsIgnoreCase("location")){
                        updateStudentField(student, "address", value);
                    }else{
                        updateStudentField(student, field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new FamsApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Edit student information fail!");
            }
        }

        // Save the updated student entity to the database
        studentRepository.save(student);
    }

    private void updateStudentField(Student student, String fieldName, Object value) {
        try {
            Field studentField = Student.class.getDeclaredField(fieldName);
            studentField.setAccessible(true);
            studentField.set(student, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FamsApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Edit student information fail!");
        }
    }

    private StudentDetailsDto mapToDto(Student student){
        return mapper.map(student, StudentDetailsDto.class);
    }

//    private Student mapToModel(StudentDetailsDto studentDetailsDto){
//        return mapper.map(studentDetailsDto,Student.class);
//    }

    private Student mapToModelAdd(StudentAddNew studentAddNew){
        return mapper.map(studentAddNew,Student.class);
    }
    @Override
    public void importDataFromXlsx(InputStream excelFile) {
        try{
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if(row.getRowNum()!=0){

                    try{
                        StudentAddNew student = new StudentAddNew();

                        student.setFullName(getStringCellValue(row.getCell(0), row.getRowNum()));
                        student.setDOB(parseLocalDate(row.getCell(1), row.getRowNum()));
                        student.setGender(getStringCellValue(row.getCell(2), row.getRowNum()));
                        student.setPhone(getStringCellValue(row.getCell(3), row.getRowNum()));
                        student.setEmail(getStringCellValue(row.getCell(4), row.getRowNum()));
                        student.setUniversity(getStringCellValue(row.getCell(5), row.getRowNum()));
                        student.setMajor(getStringCellValue(row.getCell(6), row.getRowNum()));
                        student.setGraduatedDate(parseLocalDate(row.getCell(7), row.getRowNum()));
                        student.setGPA(getNumericCellValue(row.getCell(8), row.getRowNum()));
                        student.setLocation(getStringCellValue(row.getCell(9), row.getRowNum()));
                        student.setRECer(getStringCellValue(row.getCell(11), row.getRowNum()));
                        student.setArea(getStringCellValue(row.getCell(12), row.getRowNum()));


                        validateStudentInformation(student);

                        Student newStudent = mapToModelAdd(student);

                        newStudent.setStatus(getStringCellValue(row.getCell(10), row.getRowNum()));
                        newStudent.setJoinedDate(LocalDate.now());
                        newStudent.setSchool(student.getUniversity());
                        newStudent.setAddress(student.getLocation());
                        newStudent.setFAAccount(Utils.generateFAAccount());
                        newStudent.setType(Utils.generateStudentType());
                        newStudent.setStudentCode(Utils.generateStudentCode());

                        studentRepository.save(newStudent);
                    }catch (DataIntegrityViolationException e){
                        throw new FamsApiException(HttpStatus.BAD_REQUEST,"Email of student at row "+ (row.getRowNum() + 1)+ " is existed!" );
                    }

                }
            }

            workbook.close();
        }catch (IOException exception){
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Fail to import excel file!");

        }
    }

    private void validateStudentInformation(@Valid StudentAddNew studentAddNew){

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Validate the student object
        Set<ConstraintViolation<StudentAddNew>> violations = validator.validate(studentAddNew);
        if (!violations.isEmpty()) {
            // Handle validation errors
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public byte[] exportStudents(List<String> selectedFields) {

        try{
            List<Object[]> data =  customRepository.findSelectedFields(selectedFields);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Student List");
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < selectedFields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(selectedFields.get(i));
            }
            // Create data rows
            int rowNum = 1;
            for (Object[] rowData : data) {
                Row dataRow = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (Object value : rowData) {
                    Cell cell = dataRow.createCell(cellNum++);
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }
            // Write workbook to ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new FamsApiException(HttpStatus.BAD_REQUEST,"Export list fail!");
        }
    }

    private static LocalDate parseLocalDate(Cell cell,int rowNum) {

        if (cell.getCellType() == CellType.BLANK)
            throw new FamsApiException(HttpStatus.BAD_REQUEST, "Missing data at row " + (rowNum+1) + ", cell " + (cell.getColumnIndex()+1));
        return LocalDate.parse(cell.getStringCellValue(), Utils.formatter);

    }

    private static String getStringCellValue(Cell cell,int rowNum) {
        if (cell.getCellType() == CellType.BLANK)
            throw new FamsApiException(HttpStatus.BAD_REQUEST, "Missing data at row " + (rowNum+1) + ", cell " + (cell.getColumnIndex()+1));
        return cell.getStringCellValue();
    }

    private static float getNumericCellValue(Cell cell,int rowNum) {
        if (cell.getCellType() == CellType.BLANK)
            throw new FamsApiException(HttpStatus.BAD_REQUEST, "Missing data at row " + (rowNum+1) + ", cell " + (cell.getColumnIndex()+1));
        return (float) cell.getNumericCellValue();
    }

    @Override
    public void editAStudentCertificateStatus(CertificateStatusDetailDto certificateStatusDetailDto) {
        Optional<StudentClass> studentClasses = studentClassRepository.findById(certificateStatusDetailDto.getStudentClassId());
        if (studentClasses.isEmpty()) {
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Unknown student!!");
        }
        studentClasses.get().setCertificationStatus(certificateStatusDetailDto.getCertificationStatus());
        studentClasses.get().setCertificationDate(certificateStatusDetailDto.getCertificationDate());
        studentClassRepository.save(studentClasses.get());
    }

}
