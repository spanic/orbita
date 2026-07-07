package com.bmstu_bureau_1440.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app")
public record UserIdHeaderProperties(@NotBlank String userIdHeader) {
}
