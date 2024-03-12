package com.fams.fams.repositories;

import com.fams.fams.models.entities.EmailSendStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSendStudentRepository extends JpaRepository<EmailSendStudent, Long> {
}
