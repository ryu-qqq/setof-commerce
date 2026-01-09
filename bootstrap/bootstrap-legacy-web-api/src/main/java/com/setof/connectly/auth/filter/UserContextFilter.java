package com.setof.connectly.auth.filter;

import com.setof.connectly.module.utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * User Context Filter for Legacy Web API.
 *
 * SDK(observability-adapter)가 HTTP 로깅, TraceId, MDC 기본 설정을 담당하고,
 * 이 필터는 인증된 사용자 정보만 MDC에 추가합니다.
 *
 * CustomLoggingFilter의 기능 중 SDK로 대체된 기능:
 * - TraceId 생성/전파 → SDK 자동 처리
 * - Request/Response Body 로깅 → SDK (observability.http.log-request-body)
 * - Headers 로깅 → SDK (observability.http.exclude-headers)
 * - Health check 제외 → SDK (observability.http.exclude-paths)
 * - Client IP 추출 → SDK 자동 처리
 * - MDC 자동 정리 → SDK 자동 처리
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class UserContextFilter extends OncePerRequestFilter {

    private static final String MDC_KEY_USER = "user";
    private static final String MDC_KEY_SERVER = "server";
    private static final String SERVER_NAME = "WEB-API";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Server identifier
            MDC.put(MDC_KEY_SERVER, SERVER_NAME);

            // User ID from Security Context (인증 후 설정됨)
            Long userId = SecurityUtils.currentUserId();
            if (userId != null) {
                MDC.put(MDC_KEY_USER, String.valueOf(userId));
            }

            filterChain.doFilter(request, response);
        } finally {
            // SDK가 MDC를 자동 정리하지만, 이 필터에서 추가한 키는 명시적으로 정리
            MDC.remove(MDC_KEY_USER);
            MDC.remove(MDC_KEY_SERVER);
        }
    }
}
