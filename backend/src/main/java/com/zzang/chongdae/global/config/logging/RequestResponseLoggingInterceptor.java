package com.zzang.chongdae.global.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        String requestBody = new String(cachedRequest.getInputStream().readAllBytes());
        logger.info("Incoming request: method={}, uri={}, body={}", request.getMethod(), request.getRequestURI(),
                requestBody);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        CachedBodyHttpServletResponse cachedResponse = (CachedBodyHttpServletResponse) response;
        String responseBody = new String(cachedResponse.getCachedBody());
        logger.info("Completed response: status={}, body={}", response.getStatus(), responseBody);
    }
}
