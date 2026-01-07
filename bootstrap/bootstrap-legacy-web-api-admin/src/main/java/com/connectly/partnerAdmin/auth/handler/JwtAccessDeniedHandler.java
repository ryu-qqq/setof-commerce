package com.connectly.partnerAdmin.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final String ERROR_MSG = "인증이 필요합니다.";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 프론트엔드 호환성을 위해 403 대신 401 반환 (레거시 정책)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ERROR_MSG);
    }

}


