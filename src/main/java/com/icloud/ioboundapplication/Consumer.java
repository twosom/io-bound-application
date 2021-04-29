package com.icloud.ioboundapplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    @RabbitListener(queues = Producer.CREATE_POST_QUEUE)
    public void handler(String message) {

        Post post = null;
        try {
            post = objectMapper.readValue(message, Post.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        postRepository.save(post);
    }
}
