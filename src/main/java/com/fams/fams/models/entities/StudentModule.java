package com.fams.fams.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students_modules")
public class StudentModule {

    @EmbeddedId
    private StudentModuleKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    Student students;

    @ManyToOne
    @MapsId("moduleId")
    @JoinColumn(name = "module_id")
    Module modules;

    @Column(nullable = false, name = "module_score")
    private Float moduleScore;

    @Column(nullable = false, name = "module_level")
    private String moduleLevel;

    @Column(nullable = false, name = "status")
    private Boolean status;
}
