package com.setof.connectly.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.error(
                "접근 권한이 없는 사용자가 접근 하려 합니다. user {}, 요청 URI {}",
                request.getRemoteHost(),
                request.getRequestURI());
        // 프론트엔드 호환성을 위해 403 대신 401 반환 (레거시 정책)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
    }
}
