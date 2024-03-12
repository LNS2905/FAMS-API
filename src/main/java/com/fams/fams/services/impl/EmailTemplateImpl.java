package com.fams.fams.services.impl;

import com.fams.fams.models.entities.EmailTemplate;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.EmailTemplateDto;
import com.fams.fams.models.payload.requestModel.EmailTemplateUpdate;
import com.fams.fams.models.payload.requestModel.TemplateEmailAddNew;
import com.fams.fams.repositories.EmailTemplateRepository;
import com.fams.fams.services.EmailTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class EmailTemplateImpl implements EmailTemplateService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final ModelMapper mapper;

    @Autowired
    public EmailTemplateImpl(EmailTemplateRepository emailTemplateRepository, ModelMapper mapper) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.mapper = mapper;
    }

    @Override
    public void createTemplate(TemplateEmailAddNew templateEmailAddNew) {
        try {
            EmailTemplate emailTemplate = mapToEntity(templateEmailAddNew);
            emailTemplateRepository.save(emailTemplate);
        }catch (FamsApiException e){
            throw e;
        }
        catch (Exception e) {
            throw  new FamsApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save template!");
        }
    }
    @Override
    public void updateTemplate(EmailTemplateUpdate emailTemplateUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<EmailTemplate> emailTemplate = Optional.ofNullable(emailTemplateRepository.findById(emailTemplateUpdate.getTemplateId())
                .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "NOT FOUND TEMPLATE ID: " + emailTemplateUpdate.getTemplateId())));
        try {
            emailTemplate.get().setType(emailTemplateUpdate.getType());
            emailTemplate.get().setName(emailTemplateUpdate.getName());
            emailTemplate.get().setDescription(emailTemplateUpdate.getDescription());
            emailTemplate.get().setUpdatedDate(LocalDate.now());
            emailTemplate.get().setUpdatedBy(userName);
            emailTemplate.get().setStatus(emailTemplateUpdate.isStatus());
            emailTemplateRepository.save(emailTemplate.get());
        }
        catch (FamsApiException e) {
            throw e;
        }catch (Exception e) {
            throw new FamsApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save template!");
        }
    }

    @Override
    public Page<EmailTemplateDto> getAllEmailTemplate(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<EmailTemplate> emailTemplates = emailTemplateRepository.findAll(pageable);
        return emailTemplates.map(this::mapToDto);
    }

    @Override
    public EmailTemplate getTemplateDetails(Long id) {
        return emailTemplateRepository.findById(id).orElseThrow(()-> new FamsApiException(HttpStatus.NOT_FOUND, "Template not found!!!"));
    }

    public EmailTemplate mapToEntity(TemplateEmailAddNew templateEmailAddNew){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setCreatedDate(LocalDate.now());
        emailTemplate.setCreatedBy(userName);
        emailTemplate.setUpdatedDate(LocalDate.now());
        emailTemplate.setUpdatedBy(userName);
        emailTemplate.setDescription(templateEmailAddNew.getDescription());
        emailTemplate.setName(templateEmailAddNew.getName());
        emailTemplate.setType(templateEmailAddNew.getType());
        return emailTemplate;
    }

    public EmailTemplateDto mapToDto(EmailTemplate emailTemplate){
        return  mapper.map(emailTemplate, EmailTemplateDto.class);
    }
}
