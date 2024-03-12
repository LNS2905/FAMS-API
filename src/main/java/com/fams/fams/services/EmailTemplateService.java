package com.fams.fams.services;

import com.fams.fams.models.entities.EmailTemplate;
import com.fams.fams.models.payload.dto.EmailTemplateDto;
import com.fams.fams.models.payload.requestModel.EmailTemplateUpdate;
import com.fams.fams.models.payload.requestModel.TemplateEmailAddNew;

import org.springframework.data.domain.Page;


public interface EmailTemplateService {
    void createTemplate(TemplateEmailAddNew templateEmailAddNew);
    void updateTemplate(EmailTemplateUpdate emailTemplateUpdate);
    Page<EmailTemplateDto> getAllEmailTemplate(int page, int size);
    EmailTemplate getTemplateDetails(Long id);

}
