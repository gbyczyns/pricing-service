package com.interview.pricingservice.rest;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import com.interview.pricingservice.products.Product;
import com.interview.pricingservice.products.ProductService;
import com.interview.pricingservice.products.ProductUpsert;
import com.interview.pricingservice.rest.RestExceptionHandler.Problem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
@AllArgsConstructor
@Validated
public class ProductController {

	private final ProductService productService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Retrieves all products.")
	@ApiResponse(responseCode = "200", description = "OK.")
	public Collection<Product> getProducts() {
		return productService.getProducts();
	}

	@GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Retrieves product by ID.")
	@ApiResponse(responseCode = "200", description = "OK.")
	@ApiResponse(responseCode = "404", description = "Product does not exist.", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public Product getProduct(@Parameter(description = "Product ID.", example = "00000000-0000-0000-0000-000000000000") @PathVariable UUID id) throws ProductNotFoundException {
		return productService.getProduct(id);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Creates a new product.")
	@ApiResponse(responseCode = "201", description = "Created.", headers = @Header(name = HttpHeaders.LOCATION, description = "Location URI of the new product.", schema = @Schema(type = "string")))
	@ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> createProduct(@Valid @Parameter(description = "Product details.") @RequestBody ProductUpsert productUpsert) {
		UUID id = productService.createProduct(productUpsert);
		return ResponseEntity.created(URI.create("/product/" + id)).build();
	}

	@DeleteMapping("{id}")
	@Operation(description = "Deletes a product with given ID.")
	@ApiResponse(responseCode = "200", description = "Product has been deleted.")
	@ApiResponse(responseCode = "404", description = "Product does not exist.", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<Void> deleteProduct(@Parameter(description = "Product ID.", example = "00000000-0000-0000-0000-000000000000") @PathVariable UUID id) throws ProductNotFoundException {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
