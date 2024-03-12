package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.StudentDetailsInClassDto;
import com.fams.fams.services.StudentClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student-class-management")
public class StudentClassController {
    private final StudentClassService studentClassService;

    @Autowired
    public StudentClassController(StudentClassService studentClassService) {
        this.studentClassService = studentClassService;
    }

    @Operation(
            summary = "Get Student Detail in the class by StudentId and ClassId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/{studentClassId}")
    public ResponseEntity<?> getStudentDetailsById(@PathVariable(name = "studentClassId") Long studentClassId){
        try{
            StudentDetailsInClassDto studentDetailsInClassDto = studentClassService.getStudentDetailsInClass(studentClassId);
            return ResponseEntity.ok(studentDetailsInClassDto);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Update attending status of list students",
            description = "Update attending status in StudentClass by list studentIds"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/student-class")
    public ResponseEntity<?> updateStudentsAttendingStatusInClass(
            @RequestBody List<Long> studentIds, @RequestParam(name = "classId") Long classId, @RequestParam(name = "attendingStatus") String attendingStatus) {
        try{
            studentClassService.updateStatusStudents(studentIds, classId, attendingStatus);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Edit student successfully!")
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Update attending status of a student",
            description = "Update attending status in StudentClass by studentId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/student-class/{studentId}")
    public ResponseEntity<?> updateStudentAttendingStatusInClass(
            @PathVariable(name = "studentId") Long studentId, @RequestParam(name = "classId") Long classId, @RequestParam(name = "attendingStatus") String attendingStatus) {
        try{
            studentClassService.updateStatusStudent(studentId, classId, attendingStatus);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Edit student successfully!")
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }


}
