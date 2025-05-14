package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Register this filter as a Spring-managed bean
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Allow access to login and static resources
        if (path.startsWith("/auth") || path.startsWith("/static") || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        if (httpRequest.getSession(false) == null || httpRequest.getSession(false).getAttribute("loggedInUser") == null) {
            httpResponse.sendRedirect("/auth/login");
            return;
        }

        chain.doFilter(request, response);
    }
}