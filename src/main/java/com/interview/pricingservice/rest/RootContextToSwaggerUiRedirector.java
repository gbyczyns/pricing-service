package com.interview.pricingservice.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class RootContextToSwaggerUiRedirector {

	@GetMapping("/")
	String redirectToSwaggerUi() {
		return "redirect:swagger-ui.html";
	}
}