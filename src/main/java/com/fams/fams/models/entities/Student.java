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
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(nullable = false, name = "student_code", unique = true)
    private String studentCode;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "dob")
    private LocalDate DOB;

    @Column(nullable = false, name = "gender")
    private String gender;

    @Column(nullable = false, name = "phone")
    private String phone;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(nullable = false, name = "school")
    private String school;

    @Column(nullable = false, name = "major")
    private String major;

    @Column(nullable = false, name = "graduated_date")
    private LocalDate graduatedDate;

    @Column(nullable = false, name = "gpa")
    private Float GPA;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, unique = true, name = "fa_account")
    private String FAAccount;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false, name = "status")
    private String status;

    @Column(nullable = false, name = "re_cer")
    private String RECer;

    @Column(nullable = false, name = "joined_date")
    private LocalDate joinedDate;

    @Column(nullable = false, name = "area")
    private String area;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Set<StudentClass> studentClasses;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Set<StudentModule> studentModules;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Set<Score> scores;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Set<ReservedClass> reservedClasses;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Set<EmailSendStudent> emailSendStudents;
}
