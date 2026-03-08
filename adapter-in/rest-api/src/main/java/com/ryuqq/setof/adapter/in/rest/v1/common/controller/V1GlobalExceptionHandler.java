package com.ryuqq.setof.adapter.in.rest.v1.common.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.UnauthenticatedAccessException;
import com.ryuqq.setof.adapter.in.rest.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ErrorResponse;
import com.ryuqq.setof.domain.common.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * V1GlobalExceptionHandler - V1 API 전용 예외 핸들러.
 *
 * <p>V1 API 호환을 위해 레거시 응답 형태(V1ErrorResponse)로 에러를 반환합니다.
 *
 * <p>에러 핸들링 로직은 V2 GlobalExceptionHandler와 동일하며, 응답 형태만 다릅니다:
 *
 * <ul>
 *   <li>V1: V1ErrorResponse (status, message, error, timestamp)
 *   <li>V2: RFC 7807 ProblemDetail
 * </ul>
 *
 * <p>레거시 특이사항: 404 에러는 HTTP 200으로 응답하고 body에 status 404를 포함합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Order(1)
@RestControllerAdvice(basePackages = "com.ryuqq.setof.adapter.in.rest.v1")
public class V1GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(V1GlobalExceptionHandler.class);
    private final ErrorMapperRegistry errorMapperRegistry;

    public V1GlobalExceptionHandler(ErrorMapperRegistry errorMapperRegistry) {
        this.errorMapperRegistry = errorMapperRegistry;
    }

    // ======= 400 - Validation (@RequestBody) =======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<V1ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        String errorMessage = errors.values().stream().collect(Collectors.joining(" "));

        log.warn("V1 MethodArgumentNotValid: path={}, errors={}", req.getRequestURI(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                errorMessage));
    }

    // ======= 400 - Validation (@ModelAttribute, 바인딩 단계) =======
    @ExceptionHandler(BindException.class)
    public ResponseEntity<V1ErrorResponse> handleBindException(
            BindException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        String errorMessage = errors.values().stream().collect(Collectors.joining(" "));

        log.warn("V1 BindException: path={}, errors={}", req.getRequestURI(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                errorMessage));
    }

    // ======= 400 - Method-level validation (@Validated on params) =======
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<V1ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath() == null ? "unknown" : v.getPropertyPath().toString();
            errors.put(path, v.getMessage());
        }

        String errorMessage = errors.values().stream().collect(Collectors.joining(" "));

        log.warn("V1 ConstraintViolation: path={}, errors={}", req.getRequestURI(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                errorMessage));
    }

    // ======= 400 - Method-level @Valid on @RequestBody List (Spring 6.1+) =======
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<V1ErrorResponse> handleHandlerMethodValidation(
            HandlerMethodValidationException ex, HttpServletRequest req) {
        String errorMessage =
                ex.getAllValidationResults().stream()
                        .flatMap(result -> result.getResolvableErrors().stream())
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .filter(msg -> msg != null)
                        .collect(Collectors.joining(" "));

        if (errorMessage.isBlank()) {
            errorMessage = "요청 데이터 유효성 검증에 실패했습니다.";
        }

        log.warn(
                "V1 HandlerMethodValidation: path={}, message={}",
                req.getRequestURI(),
                errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                errorMessage));
    }

    // ======= 400 - 잘못된 인자 =======
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<V1ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest req) {
        String errorMessage = Optional.ofNullable(ex.getMessage()).orElse("Invalid argument");

        log.warn("V1 IllegalArgument: path={}, message={}", req.getRequestURI(), errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                errorMessage));
    }

    // ======= 400 - 본문 파싱 실패 =======
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<V1ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn(
                "V1 HttpMessageNotReadable: path={}, cause={}",
                req.getRequestURI(),
                ex.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST,
                                ex.getClass().getSimpleName(),
                                "잘못된 요청 형식입니다. JSON 형식을 확인해주세요."));
    }

    // ======= 400 - 파라미터 타입 불일치 =======
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<V1ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String name = Optional.of(ex.getName()).orElse("unknown");
        Object value = ex.getValue();
        String required =
                ex.getRequiredType() != null
                        ? ex.getRequiredType().getSimpleName()
                        : "required type";
        String msg =
                "파라미터 '%s'의 값 '%s'는 %s 타입으로 변환할 수 없습니다"
                        .formatted(name, String.valueOf(value), required);

        log.warn(
                "V1 TypeMismatch: path={}, parameter={}, value={}, requiredType={}",
                req.getRequestURI(),
                name,
                value,
                required);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), msg));
    }

    // ======= 400 - 필수 파라미터 누락 =======
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<V1ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest req) {
        String param = Optional.of(ex.getParameterName()).orElse("unknown");
        String msg = "필수 파라미터 '%s'가 누락되었습니다".formatted(param);

        log.warn(
                "V1 MissingParam: path={}, parameter={}, type={}",
                req.getRequestURI(),
                param,
                ex.getParameterType());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), msg));
    }

    // ======= 404 - 리소스 없음 (레거시 호환: HTTP 200으로 응답) =======
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<V1ApiResponse<Object>> handleNoResource(
            NoResourceFoundException ex, HttpServletRequest req) {
        log.debug(
                "V1 NoResourceFound: path={}, resourcePath={}",
                req.getRequestURI(),
                ex.getResourcePath());

        // 레거시 호환: HTTP 200으로 응답, body에 status 404 포함
        return ResponseEntity.ok(V1ApiResponse.dataNotFoundWithErrorMessage("요청한 리소스를 찾을 수 없습니다"));
    }

    // ======= 405 - 지원하지 않는 메서드 =======
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<V1ErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        String method = Optional.of(ex.getMethod()).orElse("UNKNOWN");
        Set<HttpMethod> supported =
                Optional.ofNullable(ex.getSupportedHttpMethods()).orElse(Collections.emptySet());
        String supportedStr =
                supported.isEmpty()
                        ? "없음"
                        : supported.stream()
                                .map(HttpMethod::name)
                                .collect(Collectors.joining(", "));
        String message = "%s 메서드는 지원하지 않습니다. 지원되는 메서드: %s".formatted(method, supportedStr);

        log.warn(
                "V1 MethodNotAllowed: path={}, method={}, supported={}",
                req.getRequestURI(),
                method,
                supportedStr);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.METHOD_NOT_ALLOWED,
                                ex.getClass().getSimpleName(),
                                message));
    }

    // ======= 401 - 인증 실패 =======
    @ExceptionHandler(UnauthenticatedAccessException.class)
    public ResponseEntity<V1ErrorResponse> handleUnauthenticatedAccess(
            UnauthenticatedAccessException ex, HttpServletRequest req) {
        String msg = Optional.ofNullable(ex.getMessage()).orElse("인증이 필요합니다. 유효한 토큰을 제공해주세요.");

        log.warn("V1 UnauthenticatedAccess: path={}, message={}", req.getRequestURI(), msg);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.UNAUTHORIZED, ex.getClass().getSimpleName(), msg));
    }

    // ======= 409 - 상태 충돌 =======
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<V1ErrorResponse> handleIllegalState(
            IllegalStateException ex, HttpServletRequest req) {
        String msg = Optional.ofNullable(ex.getMessage()).orElse("State conflict");

        log.warn("V1 IllegalState: path={}, message={}", req.getRequestURI(), msg);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(V1ErrorResponse.of(HttpStatus.CONFLICT, ex.getClass().getSimpleName(), msg));
    }

    // ======= 501 - 지원하지 않는 기능 (Deprecated API) =======
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<V1ErrorResponse> handleUnsupportedOperation(
            UnsupportedOperationException ex, HttpServletRequest req) {
        String msg = Optional.ofNullable(ex.getMessage()).orElse("This operation is not supported");

        log.warn("V1 UnsupportedOperation: path={}, message={}", req.getRequestURI(), msg);

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.NOT_IMPLEMENTED, ex.getClass().getSimpleName(), msg));
    }

    // ======= 500 - 나머지 잡기 =======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<V1ErrorResponse> handleGlobal(Exception ex, HttpServletRequest req) {
        String stackTrace =
                Arrays.stream(ex.getStackTrace())
                        .limit(10)
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n"));

        log.error(
                "V1 Unexpected error: path={}, message={}\n{}",
                req.getRequestURI(),
                ex.getMessage(),
                stackTrace,
                ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        V1ErrorResponse.of(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ex.getClass().getSimpleName(),
                                "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

    /**
     * 도메인 예외 처리.
     *
     * <p>V2와 동일한 로직으로 ErrorMapperRegistry를 통해 도메인별 커스텀 매핑을 적용하되, 응답 형태만 V1ErrorResponse로 반환합니다.
     *
     * <p>레거시 호환: 404 에러는 HTTP 200으로 응답합니다.
     *
     * @param ex 도메인 예외
     * @param req HTTP 요청
     * @param locale 로케일
     * @return V1 형태의 에러 응답
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomain(
            DomainException ex, HttpServletRequest req, Locale locale) {
        var mapped =
                errorMapperRegistry
                        .map(ex, locale)
                        .orElseGet(() -> errorMapperRegistry.defaultMapping(ex));

        // 로깅: V2와 동일한 전략
        if (mapped.status().is5xxServerError()) {
            log.error(
                    "V1 DomainException (Server Error): path={}, code={}, status={}, detail={},"
                            + " args={}",
                    req.getRequestURI(),
                    ex.code(),
                    mapped.status().value(),
                    mapped.detail(),
                    ex.args(),
                    ex);
        } else if (mapped.status() == HttpStatus.NOT_FOUND) {
            log.debug(
                    "V1 DomainException (Not Found): path={}, code={}, status={}, detail={},"
                            + " args={}",
                    req.getRequestURI(),
                    ex.code(),
                    mapped.status().value(),
                    mapped.detail(),
                    ex.args());
        } else {
            log.warn(
                    "V1 DomainException (Client Error): path={}, code={}, status={}, detail={},"
                            + " args={}",
                    req.getRequestURI(),
                    ex.code(),
                    mapped.status().value(),
                    mapped.detail(),
                    ex.args());
        }

        // 레거시 호환: 404는 HTTP 200으로 응답
        if (mapped.status() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.ok(V1ApiResponse.dataNotFoundWithErrorMessage(mapped.detail()));
        }

        return ResponseEntity.status(mapped.status()).body(V1ErrorResponse.of(ex, mapped.status()));
    }
}
