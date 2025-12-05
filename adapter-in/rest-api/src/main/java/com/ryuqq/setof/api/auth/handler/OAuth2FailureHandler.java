package com.ryuqq.setof.api.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.api.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth2 Failure Handler
 *
 * <p>카카오 로그인 실패 시 처리를 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String OAUTH2_LOGIN_FAILED = "OAUTH2_LOGIN_FAILED";
    private static final String OAUTH2_LOGIN_FAILED_MESSAGE = "카카오 로그인에 실패했습니다.";

    private final ObjectMapper objectMapper;

    public OAuth2FailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> errorResponse =
                ApiResponse.ofFailure(OAUTH2_LOGIN_FAILED, OAUTH2_LOGIN_FAILED_MESSAGE);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
