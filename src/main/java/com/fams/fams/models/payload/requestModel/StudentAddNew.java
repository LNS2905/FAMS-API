package com.fams.fams.models.payload.requestModel;

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
public class StudentAddNew {
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
    private String university;

    @NotEmpty(message = "Major should not be null or empty")
    private String major;

    @Past(message = "GraduatedDate must be in the past")
    private LocalDate graduatedDate;

    @DecimalMin(value = "0.0", message = "GPA must be greater than or equal to 0.0")
    @DecimalMax(value = "4.0", message = "GPA must be less than or equal to 4.0")
    private Float GPA;

    @NotEmpty(message = "Area should not be null or empty")
    private String area;

    @NotEmpty(message = "Location should not be null or empty")
    private String location;

    @NotEmpty(message = "RECer should not be null or empty")
    private String RECer;
}
