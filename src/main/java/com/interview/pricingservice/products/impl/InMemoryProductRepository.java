package com.interview.pricingservice.products.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.products.ProductRepository;
import com.interview.pricingservice.products.ProductUpsert;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryProductRepository implements ProductRepository {
	private final Map<UUID, Product> productsByIds = new ConcurrentHashMap<>();

	public InMemoryProductRepository(Products products) {
		Map<UUID, Product> productsByIds = products.getProducts().stream()
				.collect(Collectors.toMap(Product::getId, Function.identity()));
		this.productsByIds.putAll(productsByIds);
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return Optional.ofNullable(productsByIds.get(id));
	}

	@Override
	public Collection<Product> find() {
		return Collections.unmodifiableCollection(productsByIds.values());
	}

	@Override
	public UUID createProduct(ProductUpsert productUpsert) {
		UUID id = UUID.randomUUID(); // in reality, we could delegate the responsibility of generating ids to the database, e.g. using `uuid_generate_v4()` in case of PostgreSQL
		Product product = new Product(productUpsert.getName(), productUpsert.getPrice(), id);
		productsByIds.put(id, product); // to keep it simple, let's ignore the fact that there might be uuid collisions
		return id;
	}

	@Override
	public Product deleteById(UUID id) {
		return productsByIds.remove(id);
	}
}