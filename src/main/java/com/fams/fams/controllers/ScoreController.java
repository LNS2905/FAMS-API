package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.StudentScorePerClassDto;
import com.fams.fams.services.ScoreService;
import com.fams.fams.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/score-management")
public class ScoreController {

    private final ScoreService scoreService;

    @Operation(
            summary = "Import list students score",
            description = "Import list students score by file excel" +
                    "Option: allow, skip, replace"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_TRAINER')" + " || hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/import",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importListScore(@RequestPart("file") MultipartFile file,
                                             @RequestParam Long classId,
                                             @RequestParam String option){

        try{
            if(Utils.getFileType(file.getOriginalFilename()).equalsIgnoreCase("xlsx")){
                scoreService.importScoreFromXlsx(file.getInputStream(),option,classId);
            }
            else{
                return ResponseEntity.ok().body(
                        Map.of("status", HttpStatus.BAD_REQUEST, "message", "File is not supported! (xlsx)")
                );
            }
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Import students' score successfully!")
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        } catch (IOException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", HttpStatus.BAD_REQUEST, "message", e.getMessage())
            );
        }

    }

    @Operation(
            summary = "Download file template",
            description = "Download file template to track students' scores"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/template")
    public  ResponseEntity<?> downloadTemplate(){


        HttpHeaders headers = scoreService.downloadScoreTemplate();

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ClassPathResource("Mark Tracking Template.xlsx"));
    }

    @Operation(summary = "Get Student Score By Class",
            description= "Get Student Score Details Per Class base on classId ")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/class-score/{classId}")
    public ResponseEntity<?> getStudentScoreDetailListByClass(@PathVariable Long classId){
        try {
            List<StudentScorePerClassDto> scoreList = scoreService.getStudentScoreListByClass(classId);
            return ResponseEntity.ok(scoreList);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(summary = "Get Student Score Detail",
            description = "Get Student Score Detail By StudentId")
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/student-score/{studentId}")
    public ResponseEntity<?> getStudentScoreDetail(@PathVariable Long studentId){
        try {
            StudentScorePerClassDto studentScoreDetail = scoreService.getStudentScoreDetail(studentId);
            return ResponseEntity.ok(studentScoreDetail);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }

    }
}
