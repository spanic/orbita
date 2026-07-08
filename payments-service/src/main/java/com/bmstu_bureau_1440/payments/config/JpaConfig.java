package com.bmstu_bureau_1440.payments.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "com.bmstu_bureau_1440.payments",
        "com.bmstu_bureau_1440.shared"
})
@EnableJpaRepositories(basePackages = {
        "com.bmstu_bureau_1440.payments",
        "com.bmstu_bureau_1440.shared"
})
public class JpaConfig {
}
