package dev.java10x.email.consumer;

import dev.java10x.email.dto.EmailDto;
import dev.java10x.email.entities.EmailModel;
import dev.java10x.email.service.EmailSend;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailSend emailService;

    public EmailConsumer(EmailSend emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email-queue")
        public void listenEmailQueue(@Payload EmailDto dto) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(dto, emailModel);
        emailService.sendEmail(emailModel);
    }
}
