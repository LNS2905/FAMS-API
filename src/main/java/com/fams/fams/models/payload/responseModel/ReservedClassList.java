package com.fams.fams.models.payload.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedClassList {
    private Long id;
    private String fullName;
    private String studentCode;
    private String gender;
    private LocalDate DOB;
    private String location;
    private String className;
    private String moduleName;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
