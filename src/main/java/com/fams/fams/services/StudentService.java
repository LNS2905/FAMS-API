package com.fams.fams.services;

import com.fams.fams.models.entities.Student;

import java.io.InputStream;
import java.util.List;

import com.fams.fams.models.payload.dto.CertificateStatusDetailDto;
import com.fams.fams.models.payload.dto.StudentDetailsDto;
import com.fams.fams.models.payload.requestModel.StudentAddNew;
import org.springframework.data.domain.Page;

public interface StudentService {
    Page<Student> getStudentsByClassId(String classId, int page, int size);
    StudentDetailsDto getStudentDetails(Long id);
    StudentDetailsDto getStudentDetailsByStudentCode(String studentCode);
    void addNewStudent(StudentAddNew studentAddNew);
    StudentDetailsDto deleteStudentById(Long id);
    void editAStudent(StudentDetailsDto studentDetailsDto);
    void importDataFromXlsx(InputStream excelFile);
    byte[] exportStudents(List<String> selectedFields);
    List<StudentDetailsDto> deleteStudentsInBatch(List<Long> studentIds);
    Page<StudentDetailsDto> getAllStudents(int page, int size);
    void editAStudentCertificateStatus(CertificateStatusDetailDto studentDetailsDto);
}
