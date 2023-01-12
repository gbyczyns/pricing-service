package com.interview.pricingservice.pricing.strategies;

import java.math.BigDecimal;
import java.util.List;

import lombok.ToString;

/**
 * This strategy sums the cost of all the products and then applies a fixed percentage discount.
 */
@ToString(callSuper = true)
public class FixedPercentageDiscountPricingStrategy extends ProgressiveDiscountPricingStrategy {

	public FixedPercentageDiscountPricingStrategy(String description, BigDecimal percentageDiscount) {
		super(description, List.of(new Threshold(0, percentageDiscount)));
	}
}