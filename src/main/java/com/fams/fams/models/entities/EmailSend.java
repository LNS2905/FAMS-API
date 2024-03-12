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
@Table(name = "emailsends")
public class EmailSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emailsend_id")
    private Long emailsendId;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private EmailTemplate emailTemplates;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User users;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "send_date")
    private LocalDate sendDate;

    @Column(nullable = false, name = "receiver_type")
    private String receiverType;

    @OneToMany(mappedBy = "emailSends", cascade = CascadeType.ALL)
    private Set<EmailSendUser> emailSendUsers;

    @OneToMany(mappedBy = "emailSends", cascade = CascadeType.ALL)
    private Set<EmailSendStudent> emailSendStudents;
}
