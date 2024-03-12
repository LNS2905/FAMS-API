package com.fams.fams.models.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateStatusDetailDto {
    private Long studentClassId;

    @NotEmpty(message = "CertificationStatus should not be null or empty")
    private String certificationStatus;

    @Past(message = "GraduatedDate must be in the past")
    private LocalDate certificationDate;


}
