package com.fams.fams.repositories;

import com.fams.fams.models.entities.EmailSendUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendUserRepository extends JpaRepository<EmailSendUser, Long> {
}
