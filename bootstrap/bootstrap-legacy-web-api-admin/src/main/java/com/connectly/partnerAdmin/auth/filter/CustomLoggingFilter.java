package com.connectly.partnerAdmin.auth.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.connectly.partnerAdmin.module.utils.SecurityUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    public static final String HEALTH_URI = "/health";
    private static final String GUEST= "GUEST";
    private static final String  TRACE_ID_HEADER= "X-Trace-Id";
    private static final String  PRODUCT_ID_HEADER= "X-Product-Id";
    private static final String  AUTHORIZATION_HEADER= "Authorization";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            if (!request.getRequestURI().equals(HEALTH_URI)) {
                logRequest(requestWrapper);
            }
            filterChain.doFilter(requestWrapper, wrappedResponse);
        } finally {
            if (!request.getRequestURI().equals(HEALTH_URI)) {
                logResponse(wrappedResponse);
                wrappedResponse.copyBodyToResponse();
                MDC.clear();
            }
        }
    }

    private void logRequest(RequestWrapper request) throws IOException {
        String queryString = request.getQueryString();
        String clientIP = getClientIP(request);
        String uri = request.getRequestURI() + (queryString != null ? "?" + queryString : "");

        Optional<String> sellerEmailOpt = SecurityUtils.currentSellerEmailOpt();
        String email = sellerEmailOpt.map(Object::toString).orElse(GUEST);
        String traceId = UUID.randomUUID().toString();

        String traceIdHeader = request.getHeader(TRACE_ID_HEADER);
        if(traceIdHeader != null) {
            traceId = traceIdHeader;
        }

        String productIdHeader = request.getHeader(PRODUCT_ID_HEADER);
        if(productIdHeader != null) {
            MDC.put("productIdHeader", productIdHeader);
        }

        String authorization = request.getHeader(AUTHORIZATION_HEADER);


        MDC.put("Authorization", authorization);
        MDC.put("server", "ADMIN-API");
        MDC.put("traceId", traceId);
        MDC.put("uri", uri);
        MDC.put("user", email);
        MDC.put("queryString", queryString != null ? queryString : "");
        MDC.put("clientIP", clientIP);
        MDC.put("method", request.getMethod());
        MDC.put("content-type", request.getContentType());

        log.info("uri =[{}], headers=[{}] body =[{}]",
                uri,
                getHeaders(request),
                getRequestBody(request)
        );

    }

    private void logResponse(ContentCachingResponseWrapper response) {
        MDC.put("status", String.valueOf(response.getStatus()));
        MDC.put("content-type", response.getContentType());

        log.info("[{}]", logPayload(response.getContentType(), response.getContentAsByteArray()));
    }

    private String getRequestBody(RequestWrapper request) throws IOException {
        String contentType = request.getContentType();
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, request.getCharacterEncoding() != null ? request.getCharacterEncoding() : StandardCharsets.UTF_8.name());
            }
        }
        return "";
    }

    private String logPayload(String contentType, byte[] content) {
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible && content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "";
    }

    private boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA
        );

        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getClientIP(HttpServletRequest request) {
        String clientIP = request.getHeader("X-Forwarded-For");
        if (clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP)) {
            return request.getRemoteAddr();
        }
        return clientIP.split(",")[0];
    }
}
