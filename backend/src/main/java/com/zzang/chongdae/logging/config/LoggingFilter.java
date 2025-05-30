package com.zzang.chongdae.logging.config;

import com.zzang.chongdae.logging.domain.HttpStatusCategory;
import com.zzang.chongdae.logging.domain.MemberIdentifier;
import com.zzang.chongdae.logging.dto.LogContext;
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
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    private static final int MAX_LOGGABLE_BODY_LENGTH = 2048;
    private static final String MASKED_INFORMATION = "[MASKED_INFORMATION]";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
                (HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);
        String identifier = UUID.randomUUID().toString();
        MDC.put("trackId", identifier);
        wrappedRequest.setAttribute("startTime", System.currentTimeMillis());
        wrappedRequest.setAttribute("loggingMasked", false);
        chain.doFilter(wrappedRequest, wrappedResponse);
        long startTime = Long.parseLong(request.getAttribute("startTime").toString());
        long endTime = System.currentTimeMillis();
        String latency = endTime - startTime + "ms";
        String uri = parseUri(wrappedRequest.getRequestURI(), wrappedRequest.getQueryString());
        String requestBody = parseRequestBody(wrappedRequest);

        MemberIdentifier memberIdentifier = new MemberIdentifier(wrappedRequest.getCookies());
        String httpMethod = wrappedRequest.getMethod();
        String statusCode = String.valueOf(wrappedResponse.getStatus());
        HttpStatusCategory statusCategory = HttpStatusCategory.fromStatusCode(wrappedResponse.getStatus());
        String responseBody = parseResponseBody(wrappedResponse);
        String stackTrace = (String) request.getAttribute("stackTrace");
        statusCategory.log(log,
                new LogContext(identifier, memberIdentifier, latency, httpMethod, uri, requestBody, statusCode,
                        responseBody, stackTrace));
        wrappedResponse.copyBodyToResponse();
        MDC.clear();
    }

    private String parseResponseBody(ContentCachingResponseWrapper response) {
        try {
            String bodyString = new String(response.getContentAsByteArray());
            if (bodyString.length() > MAX_LOGGABLE_BODY_LENGTH) {
                return bodyString.substring(0, MAX_LOGGABLE_BODY_LENGTH) + " ... (truncated)";
            }
            return bodyString;
        } catch (Exception e) {
            return "";
        }
    }

    private String parseRequestBody(ContentCachingRequestWrapper request) {
        if (isMultipart(request)) {
            return "";
        }
        boolean isMasked = (boolean) request.getAttribute("loggingMasked");
        if (isMasked) {
            return MASKED_INFORMATION;
        }
        return request.getContentAsString();
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
