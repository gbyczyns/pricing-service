package com.interview.pricingservice.rest.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price {
	@Schema(description = "Total price for all the products (discount is already applied).")
	private final BigDecimal total;
	@Schema(description = "The additional discount that has been granted.")
	private final BigDecimal appliedDiscount;
	@Schema(description = "Name of applied discount.")
	private final String appliedDiscountName;

	public Price(BigDecimal total) {
		this(total, BigDecimal.ZERO, null);
	}
}