package com.interview.pricingservice.pricing.strategies;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;

@FunctionalInterface
public interface PricingStrategy {
	Price calculatePrice(Product product, int quantity);
}