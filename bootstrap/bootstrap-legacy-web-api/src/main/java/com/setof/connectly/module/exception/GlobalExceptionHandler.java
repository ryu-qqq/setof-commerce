package com.setof.connectly.module.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.setof.connectly.module.notification.service.slack.SlackErrorIssueService;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.payload.CheckedErrorResponse;
import com.setof.connectly.module.payload.ErrorResponse;
import com.setof.connectly.module.utils.MessageUtils;
import io.sentry.Sentry;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@RestController
public class GlobalExceptionHandler {
    private static final String CHECK_LOG_CODE_FORMAT = "Class : {}, Code : {}, Message : {}";
    private static final String DEFAULT_ERROR_MSG = "Invalid input format 유효하지 않은 요청 값 입니다.";

    //private final SlackErrorIssueService slackErrorIssueService;

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        String detailMessage = DEFAULT_ERROR_MSG;

        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) e.getCause();
            detailMessage = cause.getPathReference();
        }
        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST,
                        e.getClass().getSimpleName(),
                        detailMessage + e.getMessage());
        //slackErrorIssueService.sendSlackMessage(e);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<?> handleCustomException(ApplicationException e) {

        HttpStatus httpStatus = e.getHttpStatus();
        String errorCode = e.getErrorCode();
        String exceptionClassName = e.getClass().getSimpleName();
        String message = e.getMessage();

        String[] split = e.getErrorCode().split("-");

        log.error(CHECK_LOG_CODE_FORMAT, exceptionClassName, errorCode, message);

        if (split[1].equals("404")) {
            ApiResponse<?> apiResponse = ApiResponse.dataNotFoundWithErrorMessage(message);
            return ResponseEntity.ok(apiResponse);
        } else {
            //slackErrorIssueService.sendSlackMessage(e);
            ErrorResponse response = ErrorResponse.of(httpStatus, exceptionClassName, message);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @ExceptionHandler(value = {CheckedApplicationException.class})
    protected ResponseEntity<?> handleCustomCheckedException(CheckedApplicationException e) {

        HttpStatus httpStatus = e.getHttpStatus();
        String errorCode = e.getErrorCode();
        String exceptionClassName = e.getClass().getSimpleName();
        String message = e.getMessage();
        String title = e.getTitle();

        String[] split = e.getErrorCode().split("-");

        log.error(CHECK_LOG_CODE_FORMAT, exceptionClassName, errorCode, message);

        if (split[1].equals("404")) {
            ApiResponse<?> apiResponse = ApiResponse.dataNotFoundWithErrorMessage(message);
            return ResponseEntity.ok(apiResponse);
        } else {
            //slackErrorIssueService.sendSlackMessage(e);
            ErrorResponse response =
                    CheckedErrorResponse.of(httpStatus, exceptionClassName, message, title);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleBindException(BindException e) {
        String exceptionClassName = e.getClass().getSimpleName();

        String errorMsg =
                e.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining("\n"));

        ErrorResponse response =
                ErrorResponse.of(HttpStatus.BAD_REQUEST, exceptionClassName, errorMsg);
        //slackErrorIssueService.sendSlackMessage(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointException(NullPointerException e) {
        log.error("NullPointerException caught: ", e);
        String stackTraceString =
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n"));
        log.error(stackTraceString);
        Sentry.captureException(e);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e) {
        String exceptionClassName = e.getClass().getSimpleName();
        String errorMsg = MessageUtils.extractDuplicateEntryMessage(e.getMessage());

        ErrorResponse response =
                ErrorResponse.of(HttpStatus.BAD_REQUEST, exceptionClassName, errorMsg);
        String stackTraceString =
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n"));
        log.error(stackTraceString);
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException e) {
        String exceptionClassName = e.getClass().getSimpleName();
        String errorMsg = MessageUtils.extractDuplicateEntryMessage(e.getMessage());

        ErrorResponse response =
                ErrorResponse.of(HttpStatus.BAD_REQUEST, exceptionClassName, errorMsg);
        String stackTraceString =
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n"));
        log.error(stackTraceString);
        Sentry.captureException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateExceptionException(IllegalStateException e) {
        String exceptionClassName = e.getClass().getSimpleName();
        String errorMsg = MessageUtils.extractDuplicateEntryMessage(e.getMessage());

        ErrorResponse response =
                ErrorResponse.of(HttpStatus.BAD_REQUEST, exceptionClassName, errorMsg);
        String stackTraceString =
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n"));
        log.error(stackTraceString);
        Sentry.captureException(e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
