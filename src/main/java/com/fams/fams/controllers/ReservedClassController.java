package com.fams.fams.controllers;

import com.fams.fams.models.entities.Class;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.ReservedClassDto;
import com.fams.fams.models.payload.requestModel.ReservedClassAddNew;
import com.fams.fams.models.payload.responseModel.ReservedClassList;
import com.fams.fams.services.ReservedClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reserved-class-management")
public class ReservedClassController {
    private final ReservedClassService reservedClassService;

    @Autowired
    public ReservedClassController(ReservedClassService reservedClassService) {
        this.reservedClassService = reservedClassService;
    }

    @Operation(
            summary = "Get List ReservedStudent"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/reserved-class")
    public ResponseEntity<?> getStudentsWithReservationStatus(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10")int size){
        try {
            Page<ReservedClassList> reservedClass = reservedClassService.getAllStudents(page, size);
            return new ResponseEntity<>(reservedClass, HttpStatus.OK);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
    @Operation(
            summary = "Get List New Class after Reserved"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/reserved-class/new-class")
    public ResponseEntity<?> findNewClassAfterReservation(@RequestParam Long studentId,
                                                              @RequestParam Long classId){
        try {
            List<Class> newClass = reservedClassService.findNewClassForReservedStudent(studentId, classId);
            return new ResponseEntity<>(newClass, HttpStatus.OK);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
    @Operation(
            summary = "Get Reservation Student Detail"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/reserved-class/{id}")
    public ResponseEntity<?> getReservationStudent(@PathVariable Long id){
        try{
            ReservedClassDto reservedClassDto = reservedClassService.getReservationStudent(id);
            return ResponseEntity.ok(reservedClassDto);
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
    @Operation(
            summary = "Create New Reservation Student"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reserved-class")
    public ResponseEntity<?> addNewReservedClass(@RequestBody ReservedClassAddNew reservedClassAddNew){
        try {
            reservedClassService.addNewReservation(reservedClassAddNew);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Add student successfully!", "created", reservedClassAddNew)
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Back To Class Of Student After Reservation"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reserved-class/new-student-class")
    public ResponseEntity<?> addNewStudentClassAfterReservation(@RequestParam Long reservedClassId,
                                                                @RequestParam Long studentId,
                                                                @RequestParam Long classId){
        try {
            reservedClassService.addReservedStudentIntoNewClass(reservedClassId, classId, studentId);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Back To Class successfully!")
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Drop Out Reservation Student "
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/reserved-class")
    public ResponseEntity<?> updateStatusStudentInClass(@RequestParam Long reservedClassId,
            @RequestParam Long studentId, @RequestParam Long classId) {
        try{
            reservedClassService.updateStatusDropOut(reservedClassId, studentId, classId);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Update Attending Status successfully!")
            );
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }


}
