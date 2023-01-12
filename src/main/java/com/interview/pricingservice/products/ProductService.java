package com.interview.pricingservice.products;

import java.util.Collection;
import java.util.UUID;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Collection<Product> getProducts() {
		return productRepository.find();
	}

	public Product getProduct(UUID id) throws ProductNotFoundException {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product does not exist: " + id));
	}

	public UUID createProduct(ProductUpsert productUpsert) {
		return productRepository.createProduct(productUpsert);
	}

	public void deleteProduct(UUID id) throws ProductNotFoundException {
		Product deletedProduct = productRepository.deleteById(id);
		if (deletedProduct == null) {
			throw new ProductNotFoundException("Product does not exist: " + id);
		}
	}
}
