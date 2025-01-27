package com.test.Management.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "yalu-topic")
    public void consume(String message) {
        try {
            System.out.println("Received message: " + message);
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }

}