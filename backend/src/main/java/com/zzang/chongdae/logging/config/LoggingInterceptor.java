package com.zzang.chongdae.logging.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final String MASKED_INFORMATION = "[MASKED_INFORMATION]";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws IOException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(LoggingMasked.class)) {
            request.setAttribute("loggingMasked", true);
        }
    }
}
