package dev.java10x.email.service;

import dev.java10x.email.dto.EmailDto;
import dev.java10x.email.entities.EmailModel;
import dev.java10x.email.entities.enums.EmailStatus;
import dev.java10x.email.repository.EmailRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class EmailSend {

    private final JavaMailSender mailSender;
    @Value("${APP_USER}")
    private String emailFrom;
    private final EmailRepository emailRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailSend.class);


    public EmailSend(JavaMailSender mailSender, EmailRepository emailRepository) {
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
    }

    @Transactional
    public void sendEmail(EmailModel model) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(model.getEmailTo());
            message.setSubject(model.getEmailSubject());
            message.setText(model.getBody());
            mailSender.send(message);
            model.setStatusEmail(EmailStatus.SENT);
            model.setSendDateEmail(LocalDateTime.now());
        } catch (MailException e) {
            model.setStatusEmail(EmailStatus.FAILED);
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
        emailRepository.save(model);
    }

}
