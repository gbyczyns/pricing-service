package com.interview.pricingservice.pricing.strategies;

import java.math.BigDecimal;

import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.rest.model.Price;
import lombok.ToString;

/**
 * This strategy doesn't apply any discounts. It simply calculates total amount by multiplying product price by quantity.
 */
@ToString
public class DefaultPricingStrategy implements PricingStrategy {

	@Override
	public final Price calculatePrice(Product product, int quantity) {
		BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		return new Price(total);
	}
}