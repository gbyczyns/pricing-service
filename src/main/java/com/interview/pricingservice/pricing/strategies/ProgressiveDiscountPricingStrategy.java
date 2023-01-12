package com.interview.pricingservice.pricing.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;
import com.interview.pricingservice.utils.CommonUtils;
import lombok.ToString;

/**
 * A strategy that applies a percentage discount, the amount of which depends on the quantity of products.
 */
@ToString
public class ProgressiveDiscountPricingStrategy implements PricingStrategy {
	private final String description;
	private final NavigableMap<Integer, BigDecimal> percentageDiscountByQuantity = new TreeMap<>();

	public ProgressiveDiscountPricingStrategy(String description, List<Threshold> thresholds) {
		if (thresholds == null || thresholds.isEmpty()) {
			throw new IllegalArgumentException("Please provide at least one threshold.");
		}
		for (Threshold threshold : thresholds) {
			if (percentageDiscountByQuantity.put(threshold.minimumQuantity(), threshold.percentageDiscount()) != null) {
				throw new IllegalArgumentException("More than one threshold has been defined for minimumQuantity=" + threshold.minimumQuantity());
			}
		}
		if (!CommonUtils.isSorted(percentageDiscountByQuantity.values())) {
			throw new IllegalArgumentException("Invalid thresholds have been defined. When the number of quantity increases, the discount should be increased as well.");
		}
		this.description = description;
	}

	@Override
	public Price calculatePrice(Product product, int quantity) {
		BigDecimal sum = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		BigDecimal discount = Optional.ofNullable(percentageDiscountByQuantity.floorEntry(quantity))
				.map(Map.Entry::getValue)
				.map(value -> value.movePointLeft(2))
				.map(sum::multiply)
				.orElse(BigDecimal.ZERO)
				.setScale(2, RoundingMode.HALF_EVEN);
		return new Price(sum.subtract(discount), discount, description);
	}

	public record Threshold(int minimumQuantity, BigDecimal percentageDiscount) {
		public Threshold {
			CommonUtils.validatePercentRange(percentageDiscount);
			if (minimumQuantity < 0) {
				throw new IllegalArgumentException("Minimum quantity must be >= 0.");
			}
		}
	}
}