package com.fams.fams.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainingprograms")
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programID;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "code", unique = true)
    private String code;

    @Column(nullable = false, name = "duration")
    private Float duration;

    @Column(nullable = false, name = "created_date")
    private LocalDate createDate;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "update_date")
    private LocalDate updateDate;

    @Column(nullable = false, name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "trainingPrograms", cascade = CascadeType.ALL)
    private Set<Class> classes;

    @OneToMany(mappedBy = "trainingPrograms", cascade = CascadeType.ALL)
    private Set<TrainingProgramModule> trainingProgramModules;
}
