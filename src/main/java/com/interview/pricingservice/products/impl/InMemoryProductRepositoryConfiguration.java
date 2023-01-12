package com.interview.pricingservice.products.impl;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Products.class)
class InMemoryProductRepositoryConfiguration {
}