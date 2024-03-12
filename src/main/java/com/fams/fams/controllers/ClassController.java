package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.ClassDto;
import com.fams.fams.models.payload.responseModel.ClassDetailDto;
import com.fams.fams.services.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/class-management")
public class ClassController {
    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }
    @Operation(
            summary = "Get Classes",
            description = "Get List Classes"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping
    public ResponseEntity<?> getListClasses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try{
            Page<ClassDto> classes = classService.getAllClass(page, size);
            return ResponseEntity.ok().body(classes);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Get Classes by StudentId",
            description = "Get Classes By StudentId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/class-studentId/{studentId}")
    public ResponseEntity<?> getClassesByStudentId(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try{
            Page<ClassDto> classes = classService.getClassesByStudentId(studentId, page, size);
            return ResponseEntity.ok().body(classes);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Get Classes by Id",
            description = "Get Classes By ClassId"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getClassesDetailById(
            @PathVariable Long classId) {
        try{
            ClassDetailDto classes = classService.getClassesDetailById(classId);
            return ResponseEntity.ok().body(classes);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }


}
