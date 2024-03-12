package com.fams.fams.models.payload.dto;

import com.fams.fams.models.entities.Class;
import com.fams.fams.models.entities.Student;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedClassDto {
    private Long reservedclassId;
    private Student students;
    private Class classes;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
}
