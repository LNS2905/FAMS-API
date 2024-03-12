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
@Table(name = "emailtemplates")
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false, name = "name")
    private String name;

    @Lob
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

    @Column(nullable = false, name = "status")
    private boolean status;

    @OneToMany(mappedBy = "emailTemplates", cascade = CascadeType.ALL)
    private Set<EmailSend> emailSends;
}
