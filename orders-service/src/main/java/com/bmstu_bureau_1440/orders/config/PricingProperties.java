package com.bmstu_bureau_1440.orders.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@ConfigurationProperties(prefix = "orders.pricing")
public record PricingProperties(@NotNull @Positive BigDecimal unitPrice) {
}
