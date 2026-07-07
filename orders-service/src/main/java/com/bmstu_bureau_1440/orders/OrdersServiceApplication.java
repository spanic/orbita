package com.bmstu_bureau_1440.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(UserIdHeaderProperties.class)
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
