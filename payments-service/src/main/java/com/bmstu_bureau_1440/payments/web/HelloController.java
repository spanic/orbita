package com.bmstu_bureau_1440.payments.web;

import com.bmstu_bureau_1440.shared.Greetings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return Greetings.hello("payments-service");
    }
}
