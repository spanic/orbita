package com.bmstu_bureau_1440.payments.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.bmstu_bureau_1440.shared.event.PaymentTopics;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic orderPaymentCompletedTopic() {
        return TopicBuilder.name(PaymentTopics.ORDER_PAYMENT_COMPLETED).build();
    }

    @Bean
    NewTopic orderPaymentFailedTopic() {
        return TopicBuilder.name(PaymentTopics.ORDER_PAYMENT_FAILED).build();
    }

}
