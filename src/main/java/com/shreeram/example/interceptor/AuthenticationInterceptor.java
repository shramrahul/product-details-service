package com.shreeram.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String EXPECTED_USERNAME = "admin";
    private static final String EXPECTED_PASSWORD = "admin";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String credentials = authHeader.substring("Basic ".length()).trim();
            String[] values = credentials.split(":", 2);

            String username = values[0];
            String password = values[1];
            System.out.println(values[0] + ":" + values[1]);
            if (EXPECTED_USERNAME.equals(username) && EXPECTED_PASSWORD.equals(password)) {
                return true; // Allow the request
            }
        }

        // If authentication fails, respond with 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
        return false;
    }
}
