package com.fams.fams.models.payload.requestModel;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateEmailAddNew {
    @NotEmpty(message = "Description should not be null or empty")
    private String description;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Type should not be null or empty")
    private String type;
}
