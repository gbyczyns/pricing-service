package com.interview.pricingservice.products.impl;

import java.util.List;

import com.interview.pricingservice.products.Product;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pricing-service")
public class Products {
	private final List<Product> products;
}