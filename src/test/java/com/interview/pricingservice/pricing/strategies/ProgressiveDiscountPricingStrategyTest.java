package com.interview.pricingservice.pricing.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.interview.pricingservice.pricing.strategies.ProgressiveDiscountPricingStrategy.Threshold;
import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ProgressiveDiscountPricingStrategyTest {

	public static final BigDecimal PRODUCT_PRICE = new BigDecimal("100.00");

	private static final Threshold THRESHOLD_1 = new Threshold(2, BigDecimal.valueOf(3L));
	private static final Threshold THRESHOLD_2 = new Threshold(3, BigDecimal.valueOf(5L));
	private static final Threshold THRESHOLD_3 = new Threshold(5, BigDecimal.valueOf(10L));
	private final Product product = new Product("Product", PRODUCT_PRICE, UUID.randomUUID());

	@Test
	@DisplayName("Should fail if the discount is not increasing along with the number of items bought.")
	void shouldFailWhenDiscountDoesNotProgress() {
		Threshold threshold1 = new Threshold(1, BigDecimal.valueOf(4L));
		Threshold threshold2 = new Threshold(2, BigDecimal.valueOf(10L));
		Threshold threshold3 = new Threshold(3, BigDecimal.valueOf(5L));
		assertThrows(IllegalArgumentException.class, () -> new ProgressiveDiscountPricingStrategy("Discount", List.of(threshold1, threshold2, threshold3)));
	}

	@Test
	@DisplayName("Should fail if there are multiple thresholds with the same minimumQuantity.")
	void shouldFailOnDuplicateThresholdQuantities() {
		Threshold threshold1 = new Threshold(1, BigDecimal.valueOf(4L));
		Threshold threshold2 = new Threshold(1, BigDecimal.valueOf(10L));
		assertThrows(IllegalArgumentException.class, () -> new ProgressiveDiscountPricingStrategy("Discount", List.of(threshold1, threshold2)));
	}

	@CsvSource({
			"-1, 10",
			"-1, -10",
			"-1, 101",
			"1, 0",
			"1, 100.0000001",
			"1, 101",
	})
	@ParameterizedTest
	@DisplayName("Constructing threshold should fail for invalid values")
	void thresholdShouldFailForInvalidValues(int minimumQuantity, BigDecimal percentageDiscount) {
		assertThrows(IllegalArgumentException.class, () -> new Threshold(minimumQuantity, percentageDiscount));
	}

	@CsvSource({
			"0, 10",
			"2147483647, 10",
			"1, 0.0000001",
			"1, 100",
	})
	@ParameterizedTest
	@DisplayName("Constructing threshold should succeed for edge cases")
	void thresholdShouldSucceedForEdgeCases(int minimumQuantity, BigDecimal percentageDiscount) {
		new Threshold(minimumQuantity, percentageDiscount);
	}

	@MethodSource("shouldCalculatePrice")
	@ParameterizedTest
	@DisplayName("Should properly calculate prices for given list of products")
	void shouldCalculatePrice(int quantity, BigDecimal expectedTotal, BigDecimal expectedDiscount) {
		PricingStrategy pricingStrategy = new ProgressiveDiscountPricingStrategy("Discount", List.of(THRESHOLD_1, THRESHOLD_2, THRESHOLD_3));
		shouldCalculatePrice(quantity, expectedTotal, expectedDiscount, pricingStrategy);
	}

	@MethodSource("shouldCalculatePrice")
	@ParameterizedTest
	@DisplayName("Should properly calculate prices even if thresholds are randomly ordered.")
	void shouldCalculatePriceEvenIfThresholdsAreUnsorted(int quantity, BigDecimal expectedTotal, BigDecimal expectedDiscount) {
		PricingStrategy pricingStrategy = new ProgressiveDiscountPricingStrategy("Discount", List.of(THRESHOLD_3, THRESHOLD_1, THRESHOLD_2));
		shouldCalculatePrice(quantity, expectedTotal, expectedDiscount, pricingStrategy);
	}

	private void shouldCalculatePrice(int quantity, BigDecimal expectedTotal, BigDecimal expectedDiscount, PricingStrategy pricingStrategy) {
		Price price = pricingStrategy.calculatePrice(product, quantity);
		assertEquals(expectedTotal, price.getTotal());
		assertEquals(expectedDiscount, price.getAppliedDiscount());
	}

	static Stream<Arguments> shouldCalculatePrice() {
		return Stream.of(
				Arguments.of(1,   new BigDecimal("100.00"),   new BigDecimal("0.00")),
				Arguments.of(2,   new BigDecimal("194.00"),   new BigDecimal("6.00")),
				Arguments.of(3,   new BigDecimal("285.00"),   new BigDecimal("15.00")),
				Arguments.of(4,   new BigDecimal("380.00"),   new BigDecimal("20.00")),
				Arguments.of(5,   new BigDecimal("450.00"),   new BigDecimal("50.00")),
				Arguments.of(6,   new BigDecimal("540.00"),   new BigDecimal("60.00")),
				Arguments.of(123, new BigDecimal("11070.00"), new BigDecimal("1230.00"))
		);
	}
}
