package dev.java10x.user.producer;

import dev.java10x.user.dto.EmailDto;
import dev.java10x.user.dto.UserDto;
import dev.java10x.user.entities.UserModel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class UserProducer {

    public static final String EMAIL_EXCHANGE = "email-exchange";
    public static final String EMAIL_ROUTING_KEY = "email-routingkey";
    private final RabbitTemplate rabbitTemplate;
    private UserModel userModel;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public DirectExchange exchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    public Binding binding(Queue queue, DirectExchange exchange) {
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchange.getName(), EMAIL_ROUTING_KEY, null);
    }

    public void sendProducerMessage(UserModel model) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(model.getUserId());
        emailDto.setEmailTo(model.getEmail());
        emailDto.setEmailSubject("Welcome " + model.getName() + " to Our Service");
        emailDto.setBody(" Your registration is successful!\n" +
                " We're excited to have you on board." +
                " If you have any questions, feel free to reach out to our support team.\n" +
                " Best regards, The Team");

        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_ROUTING_KEY, emailDto);
    }
}
