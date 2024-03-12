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
@Table(name = "trainingprograms_modules")
public class TrainingProgramModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "programmodule_id")
    private Long programModuleID;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TrainingProgram trainingPrograms;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module modules;

}
