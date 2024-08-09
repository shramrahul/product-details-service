package com.shreeram.example.service;

import com.shreeram.example.model.ProductDetails;
import com.shreeram.example.repository.ProductDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProductDetailsServiceTest {

    @Mock
    private ProductDetailsRepository productDetailsRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private ProductDetailsService productDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {

        ProductDetails product1 = ProductDetails.builder()
                .productId("1")
                .productName("watch")
                .productDescription("this is an apple watch")
                .currency("USD")
                .vendorName("Apple")
                .price(500)
                .build();
        ProductDetails product2 = ProductDetails.builder()
                .productId("2")
                .productName("ipad")
                .productDescription("this is an apple ipad")
                .currency("USD")
                .vendorName("Apple")
                .price(400)
                .build();

        when(productDetailsRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductDetails> products = productDetailsService.getAllProducts();

        assertEquals(2, products.size());
    }

    @Test
    void getAllProductsById_ShouldReturnProductDetails_WhenProductExists() {

        ProductDetails product = ProductDetails.builder()
                .productId("1")
                .productName("watch")
                .productDescription("this is an apple watch")
                .currency("USD")
                .vendorName("Apple")
                .price(500)
                .build();

        when(productDetailsRepository.findById("1")).thenReturn(Optional.of(product));

        Optional<ProductDetails> result = productDetailsService.getAllProductsById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getProductId());
    }

    @Test
    void addProductDetails_ShouldSaveAndReturnProductDetails() {

        ProductDetails product = ProductDetails.builder()
                .productId("1")
                .productName("watch")
                .productDescription("this is an apple watch")
                .currency("USD")
                .vendorName("Apple")
                .price(500)
                .build();

        when(productDetailsRepository.save(product)).thenReturn(product);

        ProductDetails savedProduct = productDetailsService.addProductDetails(product);

        assertEquals("1", savedProduct.getProductId());
        assertEquals("watch", savedProduct.getProductName());
    }

    @Test
    void updateProductPrice_ShouldUpdatePriceAndSendMessage_WhenProductExists() {

        String productId = "1";
        int newPrice = 200;
        ProductDetails product = ProductDetails.builder()
                .productId("1")
                .productName("watch")
                .productDescription("this is an apple watch")
                .currency("USD")
                .vendorName("Apple")
                .price(500)
                .build();

        when(productDetailsRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productDetailsRepository.save(product)).thenReturn(product);

        productDetailsService.updateProductPrice(productId, newPrice);

        assertEquals(newPrice, product.getPrice());
        verify(kafkaProducerService, times(1)).sendPriceChangeMessage(product);
    }

    @Test
    void deleteProductById_ShouldDeleteProduct() {

        String productId = "1";

        productDetailsService.deleteProductById(productId);

        verify(productDetailsRepository).deleteById(productId);
    }

}