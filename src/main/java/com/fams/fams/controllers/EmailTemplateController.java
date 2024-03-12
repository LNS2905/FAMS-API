package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.EmailTemplateDto;
import com.fams.fams.models.payload.requestModel.EmailTemplateUpdate;
import com.fams.fams.models.payload.requestModel.TemplateEmailAddNew;
import com.fams.fams.services.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/email-template-management")
public class EmailTemplateController {
    private final EmailTemplateService emailTemplateService;

    @Autowired
    public EmailTemplateController(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Operation(
            summary = "Get Email Templates",
            description = "Get List Email Templates"
    )
    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @GetMapping("/email-template")
    public ResponseEntity<?> getEmailTemplates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<EmailTemplateDto> emailTemplates = emailTemplateService.getAllEmailTemplate(page, size);
            return ResponseEntity.ok().body(emailTemplates);
        } catch (FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Create New Email Template"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email-template")
    public ResponseEntity<?> createTemplate(@RequestBody@Valid TemplateEmailAddNew templateEmailAddNew) {
        try {
            emailTemplateService.createTemplate(templateEmailAddNew);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Add template email successfully!", "created", templateEmailAddNew)
            );
        } catch (FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }

    @Operation(
            summary = "Update Email Template"
    )
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/email-template")
    public ResponseEntity<?> updateTemplate(@RequestBody@Valid EmailTemplateUpdate emailTemplateUpdate) {
        try {
            emailTemplateService.updateTemplate(emailTemplateUpdate);
            return ResponseEntity.ok().body(
                    Map.of("status", 200, "message", "Update template email successfully!","updated", emailTemplateUpdate)
            );
        } catch (FamsApiException e) {
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
}
