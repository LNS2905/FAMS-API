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
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(nullable = false, name = "assignment_name")
    private String assignmentName;

    @Column(nullable = false, name = "assignment_type")
    private String assignmentType;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "created_date")
    private LocalDate createdDate;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "updated_date")
    private LocalDate updatedDate;

    @Column(nullable = false, name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module modules;

    @OneToMany(mappedBy = "assignments", cascade = CascadeType.ALL)
    private Set<Score> scores;

}
