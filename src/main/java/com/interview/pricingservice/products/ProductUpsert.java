package com.interview.pricingservice.products;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductUpsert {

	@Schema(description = "Product name", example = "Sample product name")
	@NotBlank(message = "Name cannot be empty.")
	private final String name;

	@Schema(description = "Price", example = "0.99")
	@Positive(message = "Price must be positive.")
	@NotNull(message = "Price cannot be empty.")
	private final BigDecimal price;
}