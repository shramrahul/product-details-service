package com.shreeram.example.repository;

import com.shreeram.example.model.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailsRepository extends MongoRepository<ProductDetails, String> {
}
