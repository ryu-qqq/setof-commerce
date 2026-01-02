package com.connectly.partnerAdmin.auth.handler;

import com.connectly.partnerAdmin.auth.exception.AuthException;
import com.connectly.partnerAdmin.module.payload.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        String localizedMessage = messageSource.getMessage(ex.getErrorCode(), null, ex.getMessage(), LocaleContextHolder.getLocale());
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatus().value(), localizedMessage, ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }


}
