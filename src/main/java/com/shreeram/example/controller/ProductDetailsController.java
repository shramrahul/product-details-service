package com.shreeram.example.controller;

import com.shreeram.example.exception.ProductNotFoundException;
import com.shreeram.example.model.ProductDetails;
import com.shreeram.example.service.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products")
public class ProductDetailsController {

    private final ProductDetailsService productDetailsService;

    public ProductDetailsController(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @GetMapping
 //  @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<ProductDetails>> getAllProducts() {
        List<ProductDetails> products = productDetailsService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/auth")
    //  @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<ProductDetails>> getAllAuthenticatedProducts() {
        List<ProductDetails> products = productDetailsService.getAllProducts();
        return ResponseEntity.ok(products);
    }

  //  @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetails> getProductById(@PathVariable String id) {
        Optional<ProductDetails> product = productDetailsService.getAllProductsById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDetails> addProductDetails(@RequestBody ProductDetails productDetails) {
        ProductDetails createdProduct = productDetailsService.addProductDetails(productDetails);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}/price")
  //  @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> updateProductPrice(@PathVariable String id, @RequestParam int newPrice) {
        productDetailsService.updateProductPrice(id, newPrice);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Product price updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable String id) {
        productDetailsService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
