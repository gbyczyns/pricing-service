package com.interview.pricingservice.pricing;

import java.util.UUID;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.products.ProductService;
import com.interview.pricingservice.rest.model.Price;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PricingService {

	private final ProductService productService;
	private final PricingStrategiesRepository pricingStrategiesRepository;

	public Price calculatePrice(UUID id, int quantity) throws ProductNotFoundException {
		Product product = productService.getProduct(id);
		return pricingStrategiesRepository.findByProductId(product.getId())
				.calculatePrice(product, quantity);
	}
}