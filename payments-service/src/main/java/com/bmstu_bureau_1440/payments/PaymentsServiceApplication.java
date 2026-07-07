package com.bmstu_bureau_1440.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;

@SpringBootApplication
@EnableConfigurationProperties(UserIdHeaderProperties.class)
public class PaymentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsServiceApplication.class, args);
    }
}
