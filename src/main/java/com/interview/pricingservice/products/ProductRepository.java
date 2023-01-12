package com.interview.pricingservice.products;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

	Optional<Product> findById(UUID id);

	Collection<Product> find();

	UUID createProduct(ProductUpsert productUpsert);

	Product deleteById(UUID id);
}