package com.setof.connectly.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setof.connectly.module.payload.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * JWT 인증 실패 시 401 Unauthorized 응답을 처리하는 EntryPoint
 *
 * <p>Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근할 때 표준화된 JSON 형식의 에러 응답을 반환합니다.
 *
 * @author Setof Team
 * @since 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 호출되는 메서드
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authException 인증 예외
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        String remoteHost = request.getRemoteHost();
        String authExceptionMessage = authException.getMessage();

        log.error(
                "인증 실패 - URI: {}, Host: {}, Reason: {}",
                requestUri,
                remoteHost,
                authExceptionMessage);

        // ErrorResponse 객체 생성 (프로젝트 표준 포맷)
        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.UNAUTHORIZED,
                        authException.getClass().getSimpleName(),
                        "인증이 필요합니다. 유효한 토큰을 제공해주세요.");

        // JSON 응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // ErrorResponse를 JSON으로 변환하여 응답
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
