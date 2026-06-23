package com.bmstu_bureau_1440.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GreetingsTest {

    @Test
    void buildsGreeting() {
        assertThat(Greetings.hello("orders-service")).isEqualTo("Hello from orders-service!");
    }
}
