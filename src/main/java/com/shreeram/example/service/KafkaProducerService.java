package com.shreeram.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shreeram.example.exception.KafkaTransportException;
import com.shreeram.example.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String PRICE_CHANGE_TOPIC = "product-price-update";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPriceChangeMessage(String id, int newPrice) {
        String message = null;
        try {
          //  message = objectMapper.writeValueAsString(KafkaMessage.builder().productId(id).newPrice(String.valueOf(newPrice)).build());
            message = id+","+newPrice;
            System.out.println(message);
            // Send the message asynchronously
            String finalMessage = message;
            kafkaTemplate.send(PRICE_CHANGE_TOPIC, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            // Handle failure
                            System.out.println("Unable to send message=[ {} ] due to : {} " + ex.getMessage());
                            log.error("Unable to send message=[ {} ] due to : {} ", finalMessage,  ex.getMessage());
                            throw new KafkaTransportException("Failed to send Kafka message", ex);
                        } else {
                            // Handle success
                            log.info("Sent message=[ {} ] with offset=[ {} ]", finalMessage, result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }
}
