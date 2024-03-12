package com.fams.fams.services.impl;

import com.fams.fams.models.entities.*;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.EmailSendDto;
import com.fams.fams.repositories.*;
import com.fams.fams.services.EmailSendService;
import com.fams.fams.services.EmailTemplateService;
import com.fams.fams.utils.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Transactional
public class EmailSendServiceImpl implements EmailSendService {
    @Autowired
    private JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;
    private final EmailSendRepository emailSendRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final EmailSendStudentRepository emailSendStudentRepository;
    private final EmailSendUserRepository emailSendUserRepository;
    private final UserRepository userRepository;
    private final ReservedClassRepository reservedClassRepository;
    private final EmailTemplateRepository emailTemplateRepository;
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    String epass;
    private static final String NOT_ALLOW = "Only allow Admin";
    private static final String SEND_TYPE = "email Send Students";
    private static final String FAIL = "Fail to send email!";

    @Autowired
    public EmailSendServiceImpl(EmailTemplateService emailTemplateService, EmailSendRepository emailSendRepository, StudentRepository studentRepository, ClassRepository classRepository, EmailSendStudentRepository emailSendStudentRepository, EmailSendUserRepository emailSendUserRepository, UserRepository userRepository, ReservedClassRepository reservedClassRepository, EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateService = emailTemplateService;
        this.emailSendRepository = emailSendRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.emailSendStudentRepository = emailSendStudentRepository;
        this.emailSendUserRepository = emailSendUserRepository;
        this.userRepository = userRepository;
        this.reservedClassRepository = reservedClassRepository;
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587); // Default SMTP port for TLS
        mailSender.setUsername(email);
        mailSender.setPassword(epass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
    @Override
    public void sendSuccessful(Long studentId, Long classId) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            //Get User is logging
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> user = userRepository.findByUserNameOrEmail(userName, userName);

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Student not found!"));

            //check Class exist
            classRepository.findById(classId)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND,"Class not found!"));
            //Check exist ReservedClass
            ReservedClass reservedClass = reservedClassRepository.findByClassIdAndStudentId(classId, studentId);
            if(reservedClass == null){
                throw new FamsApiException(HttpStatus.NOT_FOUND, "Reservation Student not found!");
            }

            Template template = new Template();
            String str = "Successful Reservation";
            helper.setFrom(email, "FAMS");
            helper.setTo(student.getEmail());
            helper.setSubject(str);
            helper.setText(template.informReservationSuccessful(reservedClass, user.get(), email), true);
            javaMailSender.send(msg);

            EmailSend emailSend = new EmailSend();
            LocalDate now = LocalDate.now();
            emailSend.setSendDate(now);
            emailSend.setContent(str);
            emailSend.setReceiverType(SEND_TYPE);
            emailSend.setUsers(user.get());
            var emailSendResult = emailSendRepository.save(emailSend);

            EmailSendStudent emailSendStudent = new EmailSendStudent();
            emailSendStudent.setStudents(student);
            emailSendStudent.setEmailSends(emailSendResult);
            emailSendStudentRepository.save(emailSendStudent);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new FamsApiException(HttpStatus.CONFLICT, FAIL);
        }
    }

        @Override
        public void sendTrainers(List<Long> userIds, Long templateId) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findById(templateId);
            if (emailTemplate.isEmpty())
                throw new FamsApiException(HttpStatus.NOT_FOUND, "Template not found");

            //Get User is logging
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> user = userRepository.findByUserNameOrEmail(userName, userName);

            List<User> userList = new ArrayList<>();
            Set<Long> uniqueUserIds = new HashSet<>(userIds);
            if (uniqueUserIds.size() != userIds.size()) {
                throw new FamsApiException(HttpStatus.CONFLICT,"Trainers are duplicated!");
            }

            List<Long> invalidList = new ArrayList<>();
            userIds.forEach(u1 -> {
                var temp = userRepository.findWithId(u1);
                if(temp != null) userList.add(temp);
                else invalidList.add(u1);
            });


            if (invalidList.isEmpty()){
                Template template = new Template();
                helper.setFrom(email,"FAMS");

                    userList.forEach(tmp -> {
                        try {
                        helper.setTo(tmp.getEmail());
                        helper.setSubject(emailTemplate.get().getType());
                        String[] paragraphs = emailTemplate.get().getDescription().split("\n");
                        helper.setText(template.generalTemplate(tmp.getFullName(), user.get().getFullName(), paragraphs), true);
                        javaMailSender.send(msg);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    });

                EmailSend emailSend = new EmailSend();
                LocalDate now = LocalDate.now();
                emailSend.setSendDate(now);
                emailSend.setEmailTemplates(emailTemplateService.getTemplateDetails(templateId));
                emailSend.setContent(emailTemplate.get().getType());
                emailSend.setReceiverType("emailSendTrainers");
                emailSend.setUsers(user.get());
                var emailSendResult = emailSendRepository.save(emailSend);

                List<EmailSendUser> emailSendUsersList = new ArrayList<>();
                userList.forEach(o -> {
                    var temp = new EmailSendUser();
                    temp.setUsers(o);
                    temp.setEmailSends(emailSendResult);
                    emailSendUsersList.add(temp);
                });
                emailSendUserRepository.saveAll(emailSendUsersList);

            }else{
                throw new FamsApiException(HttpStatus.NOT_FOUND, "Users not found!" + invalidList);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void reservationEmail(Long reservedClassId, Long templateId) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            //Get User is logging
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> user = userRepository.findByUserNameOrEmail(userName, userName);

            Optional<ReservedClass> rc = Optional.ofNullable(reservedClassRepository.findById(reservedClassId)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Not found Reserved Class!")));

            Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findById(templateId);
            if (emailTemplate.isEmpty())
                throw new FamsApiException(HttpStatus.NOT_FOUND, "Template not found");
            if (user.get().getRole().getId() > 1)
                throw new FamsApiException(HttpStatus.PRECONDITION_FAILED, NOT_ALLOW);

            LocalDate now = LocalDate.now();
            if (now.isBefore(rc.get().getStartDate()) || now.isAfter(rc.get().getStartDate()) )
                throw new FamsApiException(HttpStatus.PRECONDITION_FAILED, "Not the right time");

            Template template = new Template();
            helper.setFrom(email,"FAMS");
            helper.setTo(rc.get().getStudents().getEmail());
            helper.setSubject(emailTemplate.get().getType());
            String[] paragraphs = emailTemplate.get().getDescription().split("\n");
            helper.setText(template.generalTemplate(rc.get().getStudents().getFullName(), user.get().getFullName(), paragraphs), true);
            javaMailSender.send(msg);

            EmailSend emailSend = new EmailSend();
            emailSend.setSendDate(now);
            emailSend.setContent(emailTemplate.get().getType());
            emailSend.setReceiverType(SEND_TYPE);
            emailSend.setUsers(user.get());
            emailSend.setEmailTemplates(emailTemplate.get());
            emailSendRepository.save(emailSend);
            var temp = new EmailSendStudent();
            temp.setStudents(rc.get().getStudents());
            emailSendStudentRepository.save(temp);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterReservation(Long reservedClassId) {
        try {
            //Get User is logging
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> user = userRepository.findByUserNameOrEmail(userName, userName);

            Optional<ReservedClass> rc = Optional.ofNullable(reservedClassRepository.findById(reservedClassId)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Not found Reserved Class!")));

            Template template = new Template();
            String subject = "Fresher Academy miss you, " + rc.get().getStudents().getFullName();

            LocalDate now = LocalDate.now();
            Period period = Period.between(rc.get().getEndDate(), now);
            int diff = period.getDays() + period.getMonths() * 30 + period.getYears() * 365;
            if (now.isBefore(rc.get().getEndDate()))
                throw new FamsApiException(HttpStatus.PRECONDITION_FAILED, "Time is not correct");
            if (diff > 30)
                throw new FamsApiException(HttpStatus.PRECONDITION_FAILED, "Expired");

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(email,"FAMS");
            helper.setTo(rc.get().getStudents().getEmail());
            helper.setSubject(subject);
            helper.setText(template.afterReservation(rc.get().getStudents().getFullName(), user.get().getFullName(), user.get().getPhone()), true);
            javaMailSender.send(msg);

            EmailSend emailSend = new EmailSend();
            emailSend.setSendDate(now);
            emailSend.setContent(subject);
            emailSend.setReceiverType(subject);
            emailSend.setUsers(user.get());
            emailSendRepository.save(emailSend);
            var temp = new EmailSendStudent();
            temp.setStudents(rc.get().getStudents());
            emailSendStudentRepository.save(temp);

        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new FamsApiException(HttpStatus.CONFLICT, FAIL);
        }

    }

    @Override
    public Page<EmailSendDto> findAllEmailWereSent(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<EmailSend> emailSends = emailSendRepository.findAll(pageable);
        if (emailSends.isEmpty()) {
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Email were sent not found!");
        } else {
            return emailSends.map(this::mapToDto);
        }
    }
    private EmailSendDto mapToDto(EmailSend emailSend){
        EmailSendDto emailSendDto = new EmailSendDto();
        emailSendDto.setEmailsendId(emailSend.getEmailsendId());
        emailSendDto.setSenderEmail(emailSend.getUsers().getEmail());
        emailSendDto.setSenderName(emailSend.getUsers().getFullName());
        emailSendDto.setSendDate(emailSend.getSendDate());
        emailSendDto.setContent(emailSend.getContent());
        emailSendDto.setReceiverType(emailSend.getReceiverType());
        return emailSendDto;
    }
}



