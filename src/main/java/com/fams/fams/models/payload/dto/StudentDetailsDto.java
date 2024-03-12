package com.fams.fams.models.payload.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailsDto {
    private Long studentId;

    @NotEmpty(message = "StudentCode should not be null or empty")
    private String studentCode;

    @NotEmpty(message = "FullName should not be null or empty")
    private String fullName;

    @Past(message = "DOB must be in the past")
    private LocalDate DOB;

    @NotEmpty(message = "Gender should not be null or empty")
    private String gender;

    @NotEmpty(message = "Phone should not be null or empty")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Invalid phone number")
    private String phone;

    @NotEmpty(message = "Email should not be null or empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "University should not be null or empty")
    private String school;

    @NotEmpty(message = "Major should not be null or empty")
    private String major;

    @Past(message = "GraduatedDate must be in the past")
    private LocalDate graduatedDate;

    @DecimalMin(value = "0.0", message = "GPA must be greater than or equal to 0.0")
    @DecimalMax(value = "4.0", message = "GPA must be less than or equal to 4.0")
    private Float GPA;

    @NotEmpty(message = "Area should not be null or empty")
    private String area;

    @NotEmpty(message = "Address should not be null or empty")
    private String address;

    @NotEmpty(message = "RECer should not be null or empty")
    private String RECer;

    @NotEmpty(message = "Status should not be null or empty")
    private String status;
}
