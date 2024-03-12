package com.fams.fams.repositories;

import com.fams.fams.models.entities.EmailSend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendRepository extends JpaRepository<EmailSend, Long> {
}
