package com.connectly.partnerAdmin.module.exception;

import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;
import com.connectly.partnerAdmin.module.common.exception.CommonErrorConstant;
import com.connectly.partnerAdmin.module.common.exception.UnExpectedException;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackErrorIssueService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.payload.ErrorResponse;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@RestController
public class GlobalExceptionHandler {

    //private final SlackErrorIssueService slackErrorIssueService;

    private static final String ERROR_LOG_MSG_FORMAT = "ERROR Message : {}";
    private static final String CHECK_LOG_CODE_FORMAT = "Class : {}, Code : {}, Message : {}";


    protected void loggingError(ApplicationException e){
        String exceptionClassName = e.getClass().getSimpleName();
        String errorCode = e.getErrorCode();
        String message = e.getMessage();
        log.error(CHECK_LOG_CODE_FORMAT, exceptionClassName, errorCode, message);

        if(shouldSkipSlackMessage(e)) return;

        //slackErrorIssueService.sendSlackMessage(e);
    }

    private boolean shouldSkipSlackMessage(ApplicationException e) {
        return e.getHttpStatus() == HttpStatus.NOT_FOUND || e.getMessage().contains("No static resource");
    }



    private void loggingError(Exception e, String errorMsg){
        String stackTrace = Arrays.stream(e.getStackTrace())
                    .limit(10)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"));
        String logMessage = errorMsg + "\n" + stackTrace;
        log.error(ERROR_LOG_MSG_FORMAT, logMessage);
        Sentry.captureException(e);
    }


    @ExceptionHandler(value = { HasTitleApplicationException.class })
    protected ResponseEntity<?> handleHasTitleExceptionTemplate(HasTitleApplicationException e){
        loggingError(e);
        ErrorResponse errorResponse = toErrorResponse(e);
        if(e.getHttpStatus().value() == 404) return handle404ErrorException(e);
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<?> handleExceptionTemplate(ApplicationException e) {
        loggingError(e);
        ErrorResponse errorResponse = toErrorResponse(e);
        if(e.getHttpStatus().value() == 404) return handle404ErrorException(e);
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMsg = e.getMessage();
        String exceptionClassName = e.getClass().getSimpleName();

        loggingError(e, errorMsg);

        ErrorResponse errorResponse = ErrorResponse.of(
                CommonErrorCode.BAD_REQUEST.getHttpStatus(),
                exceptionClassName,
                errorMsg
        );

        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }


    @ExceptionHandler(value = { UnExpectedException.class })
    protected ResponseEntity<?> handleUnExpectedException(UnExpectedException e) {
        loggingError(e);
        ErrorResponse errorResponse = toErrorResponse(e);
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<?> handleException(Exception e) {
        loggingError(e, e.getMessage());
        UnExpectedException unExpectedException = new UnExpectedException(e.getMessage());
        return handleUnExpectedException(unExpectedException);
    }


    protected ResponseEntity<?> handle404ErrorException(ApplicationException e){
        ApiResponse<?> apiResponse = ApiResponse.dataNotFoundWithErrorMessage(e.getMessage());
        return ResponseEntity.ok(apiResponse);

    }

    protected ResponseEntity<?> handle404ErrorException(HasTitleApplicationException e){
        ApiResponse<?> apiResponse = ApiResponse.dataNotFoundWithErrorMessage(e.getMessage());
        return ResponseEntity.ok(apiResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s", fieldError.getDefaultMessage()))
                .collect(Collectors.joining(" "));


        String exceptionClassName = e.getClass().getSimpleName();
        loggingError(e, errorMsg);

        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.BAD_REQUEST.getHttpStatus(), exceptionClassName, errorMsg);

        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorMsg = "JSON parse error: " + e.getMessage();
        String exceptionClassName = e.getClass().getSimpleName();

        loggingError(e, errorMsg);

        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.BAD_REQUEST.getHttpStatus(), exceptionClassName, CommonErrorConstant.INVALID_REQUEST_BODY);

        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointException(NullPointerException e) {
        String stackTraceString = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(" "));

        loggingError(e, stackTraceString);

        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.BAD_REQUEST.getHttpStatus(), ERROR_LOG_MSG_FORMAT, CommonErrorConstant.INTERNAL_SERVER_ERROR);

        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    protected ErrorResponse toErrorResponse(ApplicationException e){
        return ErrorResponse.of(e);
    }

}
