package com.bmstu_bureau_1440.shared.web;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bmstu_bureau_1440.shared.config.UserIdHeaderProperties;
import com.bmstu_bureau_1440.shared.error.MissingUserIdException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserIdHeaderInterceptor implements HandlerInterceptor {

    public static final String USER_ID_ATTRIBUTE = "userId";

    private final UserIdHeaderProperties userIdHeaderProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader(userIdHeaderProperties.userIdHeader());

        if (!StringUtils.hasText(userId)) {
            throw new MissingUserIdException();
        }

        request.setAttribute(USER_ID_ATTRIBUTE, userId);

        return true;
    }

}
