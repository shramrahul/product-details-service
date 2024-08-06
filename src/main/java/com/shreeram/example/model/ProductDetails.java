package com.shreeram.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "product-details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {

    @Id
    private String productId;
    private String productName;
    private String productDescription;
    private String vendorName;
    private int price;
    private String currency;
}
