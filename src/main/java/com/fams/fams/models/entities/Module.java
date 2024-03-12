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
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private Long moduleId;

    @Column(nullable = false, name = "module_name")
    private String moduleName;

    @Column(nullable = false, name = "created_date")
    private LocalDate createdDate;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "updated_date")
    private LocalDate updatedDate;

    @Column(nullable = false, name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "modules", cascade = CascadeType.ALL)
    private Set<TrainingProgramModule> trainingProgramModules;

    @OneToMany(mappedBy = "modules", cascade = CascadeType.ALL)
    private Set<StudentModule> studentModules;

    @OneToMany(mappedBy = "modules", cascade = CascadeType.ALL)
    private Set<Assignment> assignments;

}
