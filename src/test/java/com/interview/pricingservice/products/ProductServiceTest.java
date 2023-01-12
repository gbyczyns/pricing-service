package com.interview.pricingservice.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private final UUID uuid = UUID.randomUUID();

	@Mock
	private ProductRepository productRepository;

	private ProductService productService;

	@BeforeEach
	void beforeEach() {
		productService = new ProductService(productRepository);
	}

	@Test
	void shouldReturnUuidWhenCreatingProduct() {
		ProductUpsert product = new ProductUpsert("Some product", BigDecimal.TEN);

		when(productRepository.createProduct(product)).thenReturn(uuid);

		assertEquals(uuid, productService.createProduct(product));
		verify(productRepository).createProduct(product);
	}

	@Test
	void shouldThrowWhenProductNotExists() {
		when(productRepository.findById(uuid)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.getProduct(uuid));
		verify(productRepository).findById(uuid);
	}

	@Test
	void shouldReturnProduct() throws ProductNotFoundException {
		Product product = mock(Product.class);
		when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

		assertEquals(product, productService.getProduct(uuid));
		verify(productRepository).findById(uuid);
	}
}