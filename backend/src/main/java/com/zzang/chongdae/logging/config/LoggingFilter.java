package com.zzang.chongdae.logging.config;

import com.zzang.chongdae.logging.domain.HttpStatusCategory;
import com.zzang.chongdae.logging.domain.MemberIdentifier;
import com.zzang.chongdae.logging.dto.LoggingErrorResponse;
import com.zzang.chongdae.logging.dto.LoggingInfoFailResponse;
import com.zzang.chongdae.logging.dto.LoggingInfoSuccessResponse;
import com.zzang.chongdae.logging.dto.LoggingWarnResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    private final String MASKED_INFORMATION = "[MASKED_INFORMATION]";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
                (HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);
        wrappedRequest.setAttribute("startTime", System.currentTimeMillis());
        wrappedRequest.setAttribute("loggingMasked", false);
        chain.doFilter(wrappedRequest, wrappedResponse);
        long startTime = Long.parseLong(request.getAttribute("startTime").toString());
        long endTime = System.currentTimeMillis();
        String latency = endTime - startTime + "ms";
        String identifier = UUID.randomUUID().toString();
        MemberIdentifier memberIdentifier = new MemberIdentifier(wrappedRequest.getCookies());
        String httpMethod = wrappedRequest.getMethod();
        String uri = parseUri(wrappedRequest.getRequestURI(), wrappedRequest.getQueryString());
        String requestBody = new String(wrappedRequest.getContentAsString());
        String statusCode = String.valueOf(wrappedResponse.getStatus());
        HttpStatusCategory statusCategory = HttpStatusCategory.fromStatusCode(wrappedResponse.getStatus());
        String responseBody = new String(wrappedResponse.getContentAsByteArray());
        boolean isMasked = (boolean) wrappedRequest.getAttribute("loggingMasked");
        if (isMasked) {
            requestBody = MASKED_INFORMATION;
        }
        if (isMultipart(wrappedRequest)) {
            wrappedResponse.copyBodyToResponse();
            return;
        }
        if (statusCategory == HttpStatusCategory.INFO_SUCCESS) {
            LoggingInfoSuccessResponse logResponse = new LoggingInfoSuccessResponse(
                    identifier,
                    memberIdentifier.getIdInfo(),
                    httpMethod,
                    uri,
                    requestBody,
                    statusCode,
                    latency);
            log.info(logResponse.toString());
        }
        if (statusCategory == HttpStatusCategory.INFO_FAIL) {
            LoggingInfoFailResponse logResponse = new LoggingInfoFailResponse(
                    identifier,
                    memberIdentifier.getIdInfo(),
                    httpMethod,
                    uri,
                    requestBody,
                    statusCode,
                    responseBody,
                    latency);
            log.info(logResponse.toString());
        }
        if (statusCategory == HttpStatusCategory.WARN) {
            LoggingWarnResponse logResponse = new LoggingWarnResponse(
                    identifier,
                    memberIdentifier.getIdInfo(),
                    httpMethod,
                    uri,
                    requestBody,
                    statusCode,
                    responseBody,
                    latency);
            log.warn(logResponse.toString());
        }
        String stackTrace = (String) request.getAttribute("stackTrace");
        if (request.getAttribute("stackTrace") != null) {
            LoggingErrorResponse logResponse = new LoggingErrorResponse(
                    identifier,
                    memberIdentifier.getIdInfo(),
                    httpMethod,
                    uri,
                    requestBody,
                    "500",
                    responseBody,
                    latency,
                    stackTrace);
            log.error(logResponse.toString());
        }
        wrappedResponse.copyBodyToResponse();
    }

    private boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    private String parseUri(String requestUri, String queryString) {
        if (queryString == null) {
            return requestUri;
        }
        return requestUri + "?" + queryString;
    }
}


