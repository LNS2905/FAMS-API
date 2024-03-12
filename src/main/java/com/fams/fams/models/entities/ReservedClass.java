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
@Table(name = "reservedclasses")
public class ReservedClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservedclass_id")
    private Long reservedclassid;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classes;

    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, name = "reason")
    private String reason;

    @Column(nullable = false, name = "status")
    private String status;
}
