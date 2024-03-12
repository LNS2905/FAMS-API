package com.fams.fams.controllers;

import com.fams.fams.models.entities.Student;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.CertificateStatusDetailDto;
import com.fams.fams.models.payload.dto.StudentDetailsDto;
import com.fams.fams.models.payload.requestModel.StudentAddNew;
import com.fams.fams.services.StudentService;
import com.fams.fams.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/student-management")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Get Students",
            description = "Get List Students in DB"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/students")
    public ResponseEntity<?> getStudents(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10")int size) {
        try {
            Page<StudentDetailsDto> students = studentService.getAllStudents(page, size);
            return ResponseEntity.ok().body(students);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Get Students in Class",
            description = "Get Student in Class by ClassId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/{classId}")
    public ResponseEntity<?> getStudentsByClassId(
            @PathVariable String classId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Student> students = studentService.getStudentsByClassId(classId, page, size);
            return ResponseEntity.ok().body(students);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Get Student Detail by StudentId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/student-id/{id}")
    public ResponseEntity<?> getStudentDetailsById(@PathVariable Long id){
        try{
            StudentDetailsDto studentDetailsDto = studentService.getStudentDetails(id);
            return ResponseEntity.ok(studentDetailsDto);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Get Student Detail by StudentCode"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/student-code/{studentCode}")
    public ResponseEntity<?> getStudentDetailsByCode(@PathVariable String studentCode){
        try{
            StudentDetailsDto studentDetailsDto = studentService.getStudentDetailsByStudentCode(studentCode);
            return ResponseEntity.ok(studentDetailsDto);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Create New Student"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/student")
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentAddNew studentDetails){
        try{
            studentService.addNewStudent(studentDetails);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Add student successfully!", "created", studentDetails)
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Update Student"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/student")
    public ResponseEntity<?> editAStudent(@RequestBody@Valid StudentDetailsDto studentDetails){
        try{
            studentService.editAStudent(studentDetails);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Edit student successfully!", "created", studentDetails)
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
    @Operation(
            summary = "Update Student CertificationStatus and CertificationDate"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/EditCertificationStatus")
    public ResponseEntity<?> editAStudentCertificationStatus (@RequestBody@Valid CertificateStatusDetailDto certificateStatusDetailDto){
        try {
                studentService.editAStudentCertificateStatus(certificateStatusDetailDto);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Edit student successfully!", "created", certificateStatusDetailDto)
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Delete Student",
            description = "Delete Student by StudentId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping ("/student/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long id){
        try{
            studentService.deleteStudentById(id);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Delete student successfully!")
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Delete List Students",
            description = "Delete List Students by List StudentIds"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/students")
    public ResponseEntity<?> deleteStudents(@RequestBody List<Long> studentIds) {
        try {
            studentService.deleteStudentsInBatch(studentIds);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Delete students successfully!")
            );
        }catch(FamsApiException e) {
        return ResponseEntity.ok().body(
                Map.of("status", e.getStatus().value(), "message", e.getMessage())
        );
    }
    }


    @Operation(
            summary = "Import list students",
            description = "Import list students by file excel"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/import",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importFile(@RequestPart("file") MultipartFile file){
        try{
            if(Utils.getFileType(file.getOriginalFilename()).equalsIgnoreCase("xlsx")){
                studentService.importDataFromXlsx(file.getInputStream());
            }
            else{
                return ResponseEntity.ok().body(
                        Map.of("status", HttpStatus.BAD_REQUEST, "message", "File is not supported! (xlsx)")
                );
            }
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Import students successfully!")
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        } catch (IOException | ConstraintViolationException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", HttpStatus.BAD_REQUEST, "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Download file template",
            description = "Download file template to add list students"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/template")
    public  ResponseEntity<?> downloadTemplate(){

        // Load the file from the resources directory
        Resource resource = new ClassPathResource("TemplateDssv.xlsx");

        // Set the content type and headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Operation(
            summary = "Export list Students"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/export")
    public ResponseEntity<?> exportStudentData(@RequestParam List<String> selectedFields){

        try{

            byte[] file = studentService.exportStudents(selectedFields);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported_data.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(file);

        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }

    }

}
