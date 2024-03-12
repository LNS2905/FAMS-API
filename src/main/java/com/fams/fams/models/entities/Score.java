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
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignments;

    @Column(nullable = false, name = "score")
    private Float score;

    @Column(nullable = false, name = "submission_date")
    private LocalDate submissionDate;
}
