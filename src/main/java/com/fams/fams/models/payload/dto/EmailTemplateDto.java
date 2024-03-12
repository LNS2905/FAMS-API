package com.fams.fams.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDto {
    private Long templateId;
    private LocalDate createdDate;
    private String createdBy;
    private String description;
    private String name;
    private String type;
    private boolean status;
}
