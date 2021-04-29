package com.icloud.ioboundapplication;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;
    public static final String CREATE_POST_QUEUE = "CREATE_POST_QUEUE";

    public void sendTo(String message) {
        this.rabbitTemplate.convertAndSend(CREATE_POST_QUEUE, message);
    }
}
