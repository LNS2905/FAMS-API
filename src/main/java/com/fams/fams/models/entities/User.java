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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(nullable = false, name = "dob")
    private LocalDate DOB;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, name = "gender")
    private String gender;

    @Column(nullable = false, name = "phone")
    private String phone;

    @Column(nullable = false, name = "user_name", unique = true)
    private String userName;

    @Column(nullable = false, name = "password")
    private String password;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailSend> emailSends;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserClass> userClasses;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailSendUser> emailSendUsers;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
