package com.example.lianxi.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class CsrfFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String sessionToken = (String) request.getSession().getAttribute("csrf_token");
            if (sessionToken == null) {
                sessionToken = UUID.randomUUID().toString();
                request.getSession().setAttribute("csrf_token", sessionToken);
            }
            String requestToken = request.getParameter("_csrf");
            if (requestToken == null || !requestToken.equals(sessionToken)) {
                response.sendError(403, "CSRF token invalid");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}