package com.zzang.chongdae.logging.config;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.logging.domain.HttpStatusCategory;
import com.zzang.chongdae.logging.domain.MemberIdentifier;
import com.zzang.chongdae.logging.dto.LoggingInfoFailResponse;
import com.zzang.chongdae.logging.dto.LoggingInfoSuccessResponse;
import com.zzang.chongdae.logging.dto.LoggingWarnResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
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
            return;
        }

        if (isMultipart(request)) {
            return;
        }

        long startTime = Long.parseLong(request.getAttribute("startTime").toString());
        long endTime = System.currentTimeMillis();
        String latency = endTime - startTime + "ms";

        MemberIdentifier identifier = new MemberIdentifier(request.getCookies());
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String requestBody = new String(request.getInputStream().readAllBytes());

        CachedHttpServletResponseWrapper cachedResponse = (CachedHttpServletResponseWrapper) response;
        String statusCode = String.valueOf(cachedResponse.getStatus());
        String responseBody = new String(cachedResponse.getCachedBody());

        HttpStatusCategory statusCategory = HttpStatusCategory.fromStatusCode(cachedResponse.getStatus());
        if (statusCategory == HttpStatusCategory.INFO_SUCCESS) {
            LoggingInfoSuccessResponse logResponse = new LoggingInfoSuccessResponse(identifier.getIdInfo(), httpMethod,
                    uri,
                    requestBody, statusCode, latency);
            log.info(logResponse.toString());
        }
        if (statusCategory == HttpStatusCategory.INFO_FAIL) {
            LoggingInfoFailResponse logResponse = new LoggingInfoFailResponse(identifier.getIdInfo(), httpMethod, uri,
                    requestBody, statusCode, responseBody, latency);
            log.info(logResponse.toString());
        }
        if (statusCategory == HttpStatusCategory.WARN && ex instanceof MarketException) {
            LoggingWarnResponse logResponse = new LoggingWarnResponse(identifier.getIdInfo(), httpMethod, uri,
                    requestBody, statusCode, responseBody, latency);
            log.warn(logResponse.toString());
        }
    }

    private boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }
}
