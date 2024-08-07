package com.shreeram.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shreeram.example.exception.KafkaTransportException;
import com.shreeram.example.model.ProductDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

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

    @Async
    public void sendPriceChangeMessage(ProductDetails productDetails) {
        log.info("sendPriceChangeMessage in thread: {}", Thread.currentThread().getName());
        sendMessageToKafka(productDetails)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send Kafka message", ex);
                    throw new KafkaTransportException("Failed to send Kafka message", ex);
                } else {
                    log.info("Kafka message sent successfully");
                }
            });
    }

    public CompletableFuture<Void> sendMessageToKafka(ProductDetails productDetails) {
        try {
            String message = objectMapper.writeValueAsString(productDetails);
            log.info("Prepared message: {}", message);

            // Send the message asynchronously and return a CompletableFuture
            return kafkaTemplate.send(PRICE_CHANGE_TOPIC, message)
                    .thenAccept(result ->
                            log.info("Sent message [{}] with offset [{}]", message, result.getRecordMetadata().offset())
                    )
                    .exceptionally(ex -> {
                        log.error("Failed to send message [{}] due to: {}", message, ex.getMessage(), ex);
                        return null;
                    });

        } catch (Exception e) {
            log.error("Error preparing message", e);
            return CompletableFuture.failedFuture(new RuntimeException("Error preparing message", e));
        }
    }
}
