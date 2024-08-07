package com.shreeram.example.service;

import com.shreeram.example.exception.ProductNotFoundException;
import com.shreeram.example.model.ProductDetails;
import com.shreeram.example.repository.ProductDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final KafkaProducerService kafkaProducerService;

    public ProductDetailsService(ProductDetailsRepository productDetailsRepository, KafkaProducerService kafkaProducerService) {
        this.productDetailsRepository = productDetailsRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Cacheable("products")
    public List<ProductDetails> getAllProducts() {
        return productDetailsRepository.findAll();
    }

    @Cacheable(value = "product", key = "#id")
    public Optional<ProductDetails> getAllProductsById(String id) {
        return productDetailsRepository.findById(id);
    }

    @CachePut(value = "product", key = "#productDetails.id")
    public ProductDetails addProductDetails(ProductDetails productDetails) {
       return productDetailsRepository.save(productDetails);
    }

    @CachePut(value = "product", key = "#id")
    public void updateProductPrice(String id, int newPrice) {
        Optional<ProductDetails> optionalProduct = productDetailsRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductDetails productDetails = optionalProduct.get();
            productDetails.setPrice(newPrice);
            productDetails = productDetailsRepository.save(productDetails);
            log.info("main task in thread: {}", Thread.currentThread().getName());

            kafkaProducerService.sendPriceChangeMessage(productDetails);

        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    @CacheEvict(value = "product", key = "#id")
    public void deleteProductById(String id) {
        productDetailsRepository.deleteById(id);
    }
}
