package com.fams.fams.models.payload.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String fullName;

    private String email;

    private LocalDate DOB;

    private String address;

    private String gender;

    private String phone;

    private String roleName;
}
