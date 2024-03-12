package com.fams.fams.models.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedClassAddNew {
    private Long studentId;
    private Long classId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
