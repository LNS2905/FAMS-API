package com.fams.fams.models.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendDto {
    private Long emailsendId;
    private String senderName;
    private String senderEmail;
    private String content;
    private LocalDate sendDate;
    private String receiverType;
}
