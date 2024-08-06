package com.shreeram.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaMessage {
    private final String productId;
    private final String newPrice;
}
