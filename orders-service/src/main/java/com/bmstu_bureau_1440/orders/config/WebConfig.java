package com.bmstu_bureau_1440.orders.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;
import com.bmstu_bureau_1440.shared.web.UserIdHeaderInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserIdHeaderProperties userIdHeaderProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserIdHeaderInterceptor(userIdHeaderProperties))
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**");
    }

}
