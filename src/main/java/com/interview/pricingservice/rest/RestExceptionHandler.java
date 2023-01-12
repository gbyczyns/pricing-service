package com.interview.pricingservice.rest;

import com.interview.pricingservice.exceptions.ProductNotFoundException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class, ConstraintViolationException.class})
	ResponseEntity<Problem> handleBadRequest(Exception e) {
		return handleException(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Problem> handleBadRequest(MethodArgumentNotValidException e) {
		return handleException(HttpStatus.BAD_REQUEST, e, e.getFieldError().getDefaultMessage());
	}

	@ExceptionHandler({ProductNotFoundException.class})
	ResponseEntity<Problem> handleNotFound(Exception e) {
		return handleException(HttpStatus.NOT_FOUND, e);
	}

	private ResponseEntity<Problem> handleException(HttpStatus httpStatus, Exception exception) {
		return handleException(httpStatus, exception, exception.getMessage());
	}

	private ResponseEntity<Problem> handleException(HttpStatus httpStatus, Exception exception, String detailMessage) {
		if (httpStatus.is5xxServerError()) {
			log.error(exception.getMessage(), exception);
		} else {
			log.info(exception.getMessage());
		}
		Problem problem = new Problem(httpStatus.value(), httpStatus.getReasonPhrase(), detailMessage);
		return ResponseEntity.status(httpStatus)
				.contentType(MediaType.APPLICATION_PROBLEM_JSON)
				.body(problem);
	}

	@Data
	public static class Problem {
		@Schema(description = "HTTP status code", example = "500")
		private final int status;
		@Schema(description = "Reason phrase", example = "Error")
		private final String title;
		@Schema(description = "Detailed description", example = "Something went wrong.")
		private final String detail;
	}
}
