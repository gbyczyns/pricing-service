package com.interview.pricingservice.pricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.interview.pricingservice.pricing.strategies.DefaultPricingStrategy;
import com.interview.pricingservice.pricing.strategies.FixedAmountDiscountPricingStrategy;
import com.interview.pricingservice.pricing.strategies.FixedPercentageDiscountPricingStrategy;
import com.interview.pricingservice.pricing.strategies.PricingStrategy;
import com.interview.pricingservice.pricing.strategies.ProgressiveDiscountPricingStrategy;
import com.interview.pricingservice.pricing.strategies.ProgressiveDiscountPricingStrategy.Threshold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class PricingStrategiesRepository {

	private final List<PricingStrategy> pricingStrategies;

	public PricingStrategiesRepository() {
		this.pricingStrategies = List.of(
				new DefaultPricingStrategy(),
				new FixedPercentageDiscountPricingStrategy("Winter sale! Get 10% off!", BigDecimal.valueOf(10L)),
				new FixedPercentageDiscountPricingStrategy("30% off your order", BigDecimal.valueOf(30L)),
				new FixedAmountDiscountPricingStrategy("$10 off order of $49+", BigDecimal.valueOf(10L), BigDecimal.valueOf(49L)),
				new ProgressiveDiscountPricingStrategy("Buy 2 or more and get 3% off!",
						List.of(
								new Threshold(2, BigDecimal.valueOf(3L))
						)),
				new ProgressiveDiscountPricingStrategy("Black friday has arrived! Buy 2 products, get 10% discount! Buy 3 or more, get 15% discount! Get 5 and enjoy 20% off!",
						List.of(
								new Threshold(2, BigDecimal.valueOf(10L)),
								new Threshold(3, BigDecimal.valueOf(15L)),
								new Threshold(5, BigDecimal.valueOf(20L))
						))
		);
	}

	public PricingStrategy findByProductId(UUID productId) {
		int index = ThreadLocalRandom.current().nextInt(pricingStrategies.size()); // this is just a proof-of-concept, so let's choose the strategy randomly
		PricingStrategy pricingStrategy = pricingStrategies.get(index);
		log.info("The following pricing strategy will be applied to product '{}': {}", productId, pricingStrategy);
		return pricingStrategy;
	}
}