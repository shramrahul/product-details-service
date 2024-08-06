package com.shreeram.example.service;

import com.shreeram.example.exception.ProductNotFoundException;
import com.shreeram.example.model.ProductDetails;
import com.shreeram.example.repository.ProductDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final KafkaProducerService kafkaProducerService;

    public ProductDetailsService(ProductDetailsRepository productDetailsRepository, KafkaProducerService kafkaProducerService) {
        this.productDetailsRepository = productDetailsRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public List<ProductDetails> getAllProducts() {
        return productDetailsRepository.findAll();
    }

    public Optional<ProductDetails> getAllProductsById(String id) {
        return productDetailsRepository.findById(id);
    }

    public ProductDetails addProductDetails(ProductDetails productDetails) {
       return productDetailsRepository.save(productDetails);
    }

    public void updateProductPrice(String id, int newPrice) {
        Optional<ProductDetails> optionalProduct = productDetailsRepository.findById(id);
        if (optionalProduct.isPresent()) {
            // Send price change message to Kafka
            kafkaProducerService.sendPriceChangeMessage(id, newPrice);

        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    public void deleteProductById(String id) {
        productDetailsRepository.deleteById(id);
    }
}
