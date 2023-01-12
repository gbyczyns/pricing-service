package com.interview.pricingservice.products;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Product extends ProductUpsert {

	@Schema(description = "Product ID")
	private final UUID id;

	public Product(String name, BigDecimal price, UUID id) {
		super(name, price);
		this.id = id;
	}
}