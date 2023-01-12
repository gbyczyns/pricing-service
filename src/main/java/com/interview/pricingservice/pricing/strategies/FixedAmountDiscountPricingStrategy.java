package com.interview.pricingservice.pricing.strategies;

import java.math.BigDecimal;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class FixedAmountDiscountPricingStrategy implements PricingStrategy {

	private final String description;
	private final BigDecimal minimumTotal;
	private final BigDecimal discountAmount;

	public FixedAmountDiscountPricingStrategy(String description, BigDecimal discountAmount, BigDecimal minimumTotal) {
		Assert.isTrue(minimumTotal.compareTo(BigDecimal.ZERO) >= 0, "Minimum total must be >= 0");
		Assert.isTrue(discountAmount.compareTo(BigDecimal.ZERO) > 0, "Discount amount must be > 0");
		this.description = description;
		this.discountAmount = discountAmount;
		this.minimumTotal = minimumTotal;
	}

	@Override
	public Price calculatePrice(Product product, int quantity) {
		BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		if (total.compareTo(minimumTotal) >= 0) {
			BigDecimal discountPrice = BigDecimal.ZERO.max(total.subtract(discountAmount));
			BigDecimal appliedDiscount = total.subtract(discountPrice);
			return new Price(discountPrice, appliedDiscount, description);
		}
		return new Price(total);
	}
}