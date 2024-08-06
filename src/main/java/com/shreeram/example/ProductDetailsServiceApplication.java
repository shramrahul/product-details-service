package com.shreeram.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProductDetailsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductDetailsServiceApplication.class, args);
	}
}
