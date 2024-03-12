package com.fams.fams.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "classes")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(nullable = false, name = "class_code", unique = true)
    private String classCode;

    @Column(nullable = false, name = "class_name")
    private String className;

    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, name = "created_date")
    private LocalDate createdDate;

    @Column(nullable = false, name = "created_by")
    private String createdBy;

    @Column(nullable = false, name = "updated_date")
    private LocalDate updatedDate;

    @Column(nullable = false, name = "updated_by")
    private String updatedBy;

    @Column(nullable = false, name = "duration")
    private Float duration;

    @Column(nullable = false, name = "location")
    private String location;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false, name = "approved_date")
    private LocalDate approvedDate;

    @Column(nullable = false, name = "approved_by")
    private String approvedBy;

    @Column(nullable = false, name = "attendee")
    private String attendee;

    @Column(nullable = false, name = "fsu")
    private String FSU;

    @Column(nullable = false, name = "status")
    private String status;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL)
    private Set<StudentClass> studentClasses;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL)
    private Set<UserClass> userClasses;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL)
    private Set<ReservedClass> reservedClasses;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TrainingProgram trainingPrograms;
}
