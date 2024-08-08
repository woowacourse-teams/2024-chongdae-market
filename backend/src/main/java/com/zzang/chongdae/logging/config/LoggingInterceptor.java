package com.zzang.chongdae.logging.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws IOException {
        String requestBody = new String(request.getInputStream().readAllBytes());
        logger.info("Incoming request: method={}, uri={}, body={}", request.getMethod(), request.getRequestURI(),
                requestBody);
        CachedHttpServletResponseWrapper cachedResponse = (CachedHttpServletResponseWrapper) response;
        String responseBody = new String(cachedResponse.getCachedBody());
        logger.info("Completed response: status={}, body={}", response.getStatus(), responseBody);
    }
}
