package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.EmailSendDto;
import com.fams.fams.services.EmailSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/email-send-management")
public class EmailSendController {
    private final EmailSendService emailSendService;

    @Autowired
    public EmailSendController(EmailSendService emailSendService) {
        this.emailSendService = emailSendService;
    }

    @Operation(
            summary = "Get List Email Send"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/email-send")
    public ResponseEntity<?> getAllEmailWereSent(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<EmailSendDto> emailSends = emailSendService.findAllEmailWereSent(page, size);
            return ResponseEntity.ok().body(emailSends);
        } catch (FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Send Email To Trainer"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email-send/send-trainer")
    public ResponseEntity<?> sendEmailToTrainer(@RequestParam List<Long> to,
                                            @RequestParam Long emailTemplateId) {
        try {
            emailSendService.sendTrainers(to, emailTemplateId);
            return ResponseEntity.ok().body(
                    Map.of("status", 201, "message", "Send mail successfully!")
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Send Mail to remind Reservation Students",
            description = "Send Mail to remind Reservation Students after period time"

    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email-send/after-reservation")
    public ResponseEntity<?> remindReservation(@RequestParam Long reservedClassId){
        try {
            emailSendService.afterReservation(reservedClassId);
            return ResponseEntity.ok().body(
                    Map.of("status", 201, "message", "Send mail remind successfully!")
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Send Mail to students inform successful Reservation"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email-send/reservation-successful")
    public ResponseEntity<?> informSuccessfulReservation(@RequestParam Long studentId,
                                                              @RequestParam Long classId) {
        try {
            emailSendService.sendSuccessful(studentId, classId);
            return ResponseEntity.ok().body(
                    Map.of("status", 201, "message", "Send mail inform successfully!")
            );
        }catch(FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }


    @Operation(
            summary = "Send Email To Reservation Student"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email-send/reservation-email ")
    public ResponseEntity<?> sendReservationStudent(@RequestParam Long reservedClassId,
                                                         @RequestParam Long templateId) {
        try {
            emailSendService.reservationEmail(reservedClassId, templateId);
            return ResponseEntity.ok().body(
                    Map.of("status", 201, "message", "Send mail successfully!")
            );
        } catch (FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
}