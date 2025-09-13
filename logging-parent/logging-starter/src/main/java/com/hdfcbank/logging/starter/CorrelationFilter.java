package com.hdfcbank.logging.starter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String corr = request.getHeader("X-Correlation-Id");
        if (corr == null || corr.isBlank()) corr = UUID.randomUUID().toString();
        String txn = request.getHeader("X-Transaction-Id");
        if (txn == null || txn.isBlank()) txn = UUID.randomUUID().toString();

        try {
            MDC.put("correlationId", corr);
            MDC.put("transactionId", txn);
            // optional: serviceName from properties can be set by application
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
        response.setHeader("X-Correlation-Id", corr);
        response.setHeader("X-Transaction-Id", txn);
    }
}