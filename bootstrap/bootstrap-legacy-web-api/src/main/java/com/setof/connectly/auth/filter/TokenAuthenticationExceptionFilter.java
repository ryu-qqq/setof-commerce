package com.setof.connectly.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setof.connectly.module.exception.auth.ExpireRefreshTokenException;
import com.setof.connectly.module.exception.common.TokenTypeException;
import com.setof.connectly.module.exception.user.UserNotFoundException;
import com.setof.connectly.module.payload.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenTypeException | UserNotFoundException | ExpireRefreshTokenException e) {
            setErrorResponse(response, e.getMessage(), e.getErrorCode(), e.getHttpStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
            setErrorResponse(
                    response,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response, String message, String errorCode, HttpStatus status)
            throws IOException {
        String errorResponse =
                objectMapper.writeValueAsString(ErrorResponse.of(status, message, errorCode));
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorResponse);
    }
}
