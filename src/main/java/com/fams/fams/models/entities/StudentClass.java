package com.fams.fams.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students_classes")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentclass_id")
    private Long student_classId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classes;

    @Column(nullable = false, name = "attending_status")
    private String attendingStatus;

    @Column(nullable = false, name = "result")
    private Boolean result;

    @Column(nullable = false, name = "final_score")
    private Float finalScore;

    @Column(nullable = false, name = "gpa_level")
    private String GPALevel;

    @Column(nullable = false, name = "certification_status")
    private String certificationStatus;

    @Column(nullable = false, name = "certification_date")
    private LocalDate certificationDate;

    @Column(nullable = false, name = "method")
    private String method;
}
