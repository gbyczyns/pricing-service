package com.interview.pricingservice.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
	private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(100L);

	/**
	 * Validates whether the given value is between zero (exclusive) and one hundred (inclusive).
	 */
	public static void validatePercentRange(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) <= 0 || value.compareTo(MAX_VALUE) > 0) {
			throw new IllegalArgumentException("Percentage discount must greater than 0 and less or equal to 100. Got: " + value);
		}
	}

	public static <T extends Comparable<T>> boolean isSorted(Collection<T> collection) {
		if (collection == null || collection.size() <= 1) {
			return true;
		}

		Iterator<T> iter = collection.iterator();
		T current, previous = iter.next();
		while (iter.hasNext()) {
			current = iter.next();
			if (previous.compareTo(current) > 0) {
				return false;
			}
			previous = current;
		}
		return true;
	}
}
