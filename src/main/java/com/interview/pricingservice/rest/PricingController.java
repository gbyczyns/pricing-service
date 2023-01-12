package com.interview.pricingservice.rest;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.util.UUID;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import com.interview.pricingservice.pricing.PricingService;
import com.interview.pricingservice.rest.RestExceptionHandler.Problem;
import com.interview.pricingservice.rest.model.Price;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pricing")
@AllArgsConstructor
@Validated
public class PricingController {

	private final PricingService pricingService;

	@GetMapping(path = "{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Calculates the total price for given product & quantity and returns the information about applied discounts (if there are any).")
	@ApiResponse(responseCode = "200", description = "OK.")
	@ApiResponse(responseCode = "404", description = "Product does not exist.", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public Price getProductPrice(
			@Schema(description = "Product ID.", example = "00000000-0000-0000-0000-000000000000")
			@PathVariable UUID productId,
			@Schema(description = "Quantity ordered.", example = "10", defaultValue = "1")
			@Positive(message = "Quantity must be positive.")
			@RequestParam(required = false, defaultValue = "1") int quantity) throws ProductNotFoundException {
		return pricingService.calculatePrice(productId, quantity);
	}
}