package com.fams.fams.controllers;

import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.LoginDto;
import com.fams.fams.models.payload.dto.StudentDetailsDto;
import com.fams.fams.models.payload.dto.UserDto;
import com.fams.fams.models.payload.responseModel.JWTAuthResponse;
import com.fams.fams.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController{
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login",
            description = "Login by UserNameOrEmail"
    )
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        JWTAuthResponse jwtAuthResponse = authService.login(loginDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtAuthResponse.getAccessToken());
        return ResponseEntity.ok()
                .headers(headers)
                .body(jwtAuthResponse);
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping("/user/info")
    public ResponseEntity<?> getUserInfo() {
        try {
            UserDto userDto = authService.getUserInfo();
            return ResponseEntity.ok().body(userDto);
        }catch (FamsApiException e){
            return ResponseEntity.ok().body(
                    Map.of("status", e.getStatus().value(), "message", e.getMessage())
            );
        }
    }
}
