package com.fams.fams.services;


import com.fams.fams.models.payload.dto.EmailSendDto;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.List;

public interface EmailSendService {
    JavaMailSender getJavaMailSender();
    void sendSuccessful(Long studentId, Long classId);
    void sendTrainers(List<Long> userIds, Long templateId);
    void reservationEmail (Long reservedClassId, Long templateId);
    void afterReservation(Long reservedClassId);
    Page<EmailSendDto> findAllEmailWereSent(int page, int size);
}

