package com.bmstu_bureau_1440.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;

@SpringBootApplication(scanBasePackages = {
        "com.bmstu_bureau_1440.orders",
        "com.bmstu_bureau_1440.shared"
})
@ConfigurationPropertiesScan
@EnableConfigurationProperties(UserIdHeaderProperties.class)
@EnableScheduling
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
