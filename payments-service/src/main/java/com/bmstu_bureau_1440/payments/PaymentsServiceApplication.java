package com.bmstu_bureau_1440.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;

@SpringBootApplication(scanBasePackages = "com.bmstu_bureau_1440")
@EnableConfigurationProperties(UserIdHeaderProperties.class)
@EnableScheduling
public class PaymentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsServiceApplication.class, args);
    }
}
