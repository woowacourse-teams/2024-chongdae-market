package com.zzang.chongdae.logging.config;

import com.zzang.chongdae.logging.domain.HttpStatusCategory;
import com.zzang.chongdae.logging.domain.MemberIdentifier;
import com.zzang.chongdae.logging.dto.LoggingInfoFailResponse;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);
        request.setAttribute("startTime", System.currentTimeMillis());
        if (wrappedRequest.getRequestURI().startsWith("/h2-console/")) {
            chain.doFilter(request, response);
            return;
        }
        if (isMultipart(request)) {
            chain.doFilter(request, response);
            return;
        }
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
        String responseBody = new String(wrappedResponse.getOutputStream().toString());

        if (isMultipart(request)) {
            return;
        }

        HttpStatusCategory statusCategory = HttpStatusCategory.fromStatusCode(wrappedResponse.getStatus());
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

        wrappedResponse.copyBodyToResponse();
    }

    private boolean isMultipart(ServletRequest request) {
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

