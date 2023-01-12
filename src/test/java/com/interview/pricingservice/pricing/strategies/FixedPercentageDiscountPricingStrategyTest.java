package com.interview.pricingservice.pricing.strategies;

import java.math.BigDecimal;
import java.util.UUID;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FixedPercentageDiscountPricingStrategyTest {
	private static final Product PRODUCT = new Product("Test", new BigDecimal("9.99"), UUID.randomUUID());
	private static final int QUANTITY = 3;

	@ValueSource(doubles = {0d, -1d, 100.1d, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN})
	@ParameterizedTest
	void shouldFailOnInvalidPercentage(double percentageDiscount) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedPercentageDiscountPricingStrategy("Discount", new BigDecimal(percentageDiscount)));
	}

	@CsvSource({
			"0.01, 29.97, 0.00",
			"3.5, 28.92, 1.05",
			"10, 26.97, 3.00",
			"100, 0.00, 29.97",
	})
	@ParameterizedTest
	void shouldProperlyCalculateDiscount(BigDecimal percentageDiscount, BigDecimal expectedTotal, BigDecimal expectedDiscount) {
		PricingStrategy pricingStrategy = new FixedPercentageDiscountPricingStrategy("Sale", percentageDiscount);
		Price price = pricingStrategy.calculatePrice(PRODUCT, QUANTITY);
		Assertions.assertEquals(expectedTotal, price.getTotal());
		Assertions.assertEquals(expectedDiscount, price.getAppliedDiscount());
	}
}