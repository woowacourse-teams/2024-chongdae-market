package com.zzang.chongdae.logging.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (((HttpServletRequest) request).getRequestURI().startsWith("/h2-console/")) {
            chain.doFilter(request, response);
            return;
        }
        if (isMultipart(request)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest wrappedRequest
                = new CachedHttpServletRequestWrapper((HttpServletRequest) request);
        CachedHttpServletResponseWrapper wrappedResponse
                = new CachedHttpServletResponseWrapper((HttpServletResponse) response);
        chain.doFilter(wrappedRequest, wrappedResponse);
        wrappedResponse.copyBodyToResponse();
    }

    private boolean isMultipart(ServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }
}

