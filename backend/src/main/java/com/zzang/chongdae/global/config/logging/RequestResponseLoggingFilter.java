package com.zzang.chongdae.global.config.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(
                    (HttpServletResponse) response);
            chain.doFilter(wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse(); // 복사된 응답 바디를 실제 응답으로 다시 씁니다.
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

