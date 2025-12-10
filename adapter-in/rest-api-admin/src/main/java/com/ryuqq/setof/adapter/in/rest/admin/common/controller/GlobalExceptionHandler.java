package com.ryuqq.setof.adapter.in.rest.admin.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.domain.common.exception.DomainException;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMapperRegistry errorMapperRegistry;

    public GlobalExceptionHandler(ErrorMapperRegistry errorMapperRegistry) {
        this.errorMapperRegistry = errorMapperRegistry;
    }

    // ======= 에러 코드 상수 (RFC 7807 + 표준화) =======
    private static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    private static final String BINDING_FAILED = "BINDING_FAILED";
    private static final String CONSTRAINT_VIOLATION = "CONSTRAINT_VIOLATION";
    private static final String INVALID_ARGUMENT = "INVALID_ARGUMENT";
    private static final String INVALID_FORMAT = "INVALID_FORMAT";
    private static final String TYPE_MISMATCH = "TYPE_MISMATCH";
    private static final String MISSING_PARAMETER = "MISSING_PARAMETER";
    private static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
    private static final String STATE_CONFLICT = "STATE_CONFLICT";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    private static final String ERROR_CODE_HEADER = "x-error-code";

    // ======= Common builder (RFC 7807 완전 준수) =======
    private ResponseEntity<ProblemDetail> build(
            HttpStatus status,
            String title,
            String detail,
            String errorCode,
            HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title != null ? title : status.getReasonPhrase());
        pd.setType(URI.create("about:blank"));

        // RFC 7807 확장 필드
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", errorCode);

        // 요청 경로를 instance로
        if (req != null) {
            String uri = req.getRequestURI();
            if (req.getQueryString() != null && !req.getQueryString().isBlank()) {
                uri = uri + "?" + req.getQueryString();
            }
            pd.setInstance(URI.create(uri));
        }

        // Tracing 정보 (MDC에서)
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        if (traceId != null) pd.setProperty("traceId", traceId);
        if (spanId != null) pd.setProperty("spanId", spanId);

        // RFC 7807: Content-Type + x-error-code 헤더
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header(ERROR_CODE_HEADER, errorCode)
                .body(pd);
    }

    // ======= 400 - Validation (@RequestBody) =======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        var res =
                build(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        "Validation failed for request",
                        VALIDATION_FAILED,
                        req);
        assert res.getBody() != null;
        res.getBody().setProperty("errors", errors);
        log.warn("MethodArgumentNotValid: code={}, errors={}", VALIDATION_FAILED, errors);
        return res;
    }

    // ======= 400 - Validation (@ModelAttribute, 바인딩 단계) =======
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(
            BindException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        var res =
                build(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        "Validation failed for request",
                        BINDING_FAILED,
                        req);
        assert res.getBody() != null;
        res.getBody().setProperty("errors", errors);
        log.warn("BindException: code={}, errors={}", BINDING_FAILED, errors);
        return res;
    }

    // ======= 400 - Method-level validation (@Validated on params) =======
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath() == null ? "unknown" : v.getPropertyPath().toString();
            errors.put(path, v.getMessage());
        }
        var res =
                build(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        "Validation failed for request",
                        CONSTRAINT_VIOLATION,
                        req);
        assert res.getBody() != null;
        res.getBody().setProperty("errors", errors);
        log.warn("ConstraintViolation: code={}, errors={}", CONSTRAINT_VIOLATION, errors);
        return res;
    }

    // ======= 400 - 잘못된 인자 =======
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("IllegalArgument: code={}, message={}", INVALID_ARGUMENT, ex.getMessage());
        return build(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                Optional.ofNullable(ex.getMessage()).orElse("Invalid argument"),
                INVALID_ARGUMENT,
                req);
    }

    // ======= 400 - 본문 파싱 실패 =======
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn(
                "HttpMessageNotReadable: code={}, cause={}",
                INVALID_FORMAT,
                ex.getMostSpecificCause().getMessage());
        return build(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "잘못된 요청 형식입니다. JSON 형식을 확인해주세요.",
                INVALID_FORMAT,
                req);
    }

    // ======= 400 - 파라미터 타입 불일치 =======
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(
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
                "TypeMismatch: code={}, parameter={}, value={}, requiredType={}",
                TYPE_MISMATCH,
                name,
                value,
                required);
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, TYPE_MISMATCH, req);
    }

    // ======= 400 - 필수 파라미터 누락 =======
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest req) {
        String param = Optional.of(ex.getParameterName()).orElse("unknown");
        String msg = "필수 파라미터 '%s'가 누락되었습니다".formatted(param);

        log.warn(
                "MissingParam: code={}, parameter={}, type={}",
                MISSING_PARAMETER,
                param,
                ex.getParameterType());
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, MISSING_PARAMETER, req);
    }

    // ======= 404 - 리소스 없음 =======
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResource(
            NoResourceFoundException ex, HttpServletRequest req) {
        log.debug(
                "NoResourceFound: code={}, resourcePath={}",
                RESOURCE_NOT_FOUND,
                ex.getResourcePath());
        return build(
                HttpStatus.NOT_FOUND, "Not Found", "요청한 리소스를 찾을 수 없습니다", RESOURCE_NOT_FOUND, req);
    }

    // ======= 405 - 지원하지 않는 메서드 (Allow 헤더 포함) =======
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotAllowed(
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

        var entity =
                build(
                        HttpStatus.METHOD_NOT_ALLOWED,
                        "Method Not Allowed",
                        message,
                        METHOD_NOT_ALLOWED,
                        req);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        headers.add(ERROR_CODE_HEADER, METHOD_NOT_ALLOWED);
        if (!supported.isEmpty()) headers.setAllow(supported);

        log.warn(
                "MethodNotAllowed: code={}, method={}, supported={}",
                METHOD_NOT_ALLOWED,
                method,
                supportedStr);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .body(entity.getBody());
    }

    // ======= 409 - 상태 충돌 =======
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleIllegalState(
            IllegalStateException ex, HttpServletRequest req) {
        String msg = Optional.ofNullable(ex.getMessage()).orElse("State conflict");
        log.warn("IllegalState: code={}, message={}", STATE_CONFLICT, msg);
        return build(HttpStatus.CONFLICT, "Conflict", msg, STATE_CONFLICT, req);
    }

    // ======= 500 - 나머지 잡기 =======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobal(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error occurred: code={}", INTERNAL_ERROR, ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                INTERNAL_ERROR,
                req);
    }

    /**
     * 도메인 예외 처리
     *
     * <p>Domain Layer에서 발생한 예외를 HTTP 응답으로 변환합니다.
     *
     * <p>ErrorMapperRegistry를 통해 도메인별 커스텀 매핑을 적용합니다.
     *
     * <p><strong>로깅 레벨 전략:</strong>
     *
     * <ul>
     *   <li>5xx 에러 → ERROR 레벨 (서버 문제, 즉시 대응 필요, 스택트레이스 포함)
     *   <li>404 에러 → DEBUG 레벨 (정상적인 흐름, 로그 노이즈 방지)
     *   <li>4xx 에러 → WARN 레벨 (클라이언트 오류, 모니터링 필요)
     * </ul>
     *
     * @param ex 도메인 예외
     * @param req HTTP 요청
     * @param locale 로케일 (국제화 지원)
     * @return RFC 7807 Problem Details 응답
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(
            DomainException ex, HttpServletRequest req, Locale locale) {
        var mapped =
                errorMapperRegistry
                        .map(ex, locale)
                        .orElseGet(() -> errorMapperRegistry.defaultMapping(ex));

        // DomainException 클래스명에서 에러 코드 추출
        String errorCode = errorMapperRegistry.extractErrorCode(ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(mapped.status(), mapped.detail());
        pd.setTitle(mapped.title());
        pd.setType(mapped.type());

        // RFC 7807 확장 필드
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", errorCode);

        // 요청 경로를 instance로
        if (req != null) {
            String uri = req.getRequestURI();
            if (req.getQueryString() != null && !req.getQueryString().isBlank()) {
                uri = uri + "?" + req.getQueryString();
            }
            pd.setInstance(URI.create(uri));
        }

        // Tracing 정보 (MDC에서)
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        if (traceId != null) pd.setProperty("traceId", traceId);
        if (spanId != null) pd.setProperty("spanId", spanId);

        // HTTP 상태 코드에 따라 로깅 레벨 구분
        if (mapped.status().is5xxServerError()) {
            log.error(
                    "DomainException (Server Error): code={}, status={}, detail={}",
                    errorCode,
                    mapped.status().value(),
                    mapped.detail(),
                    ex);
        } else if (mapped.status() == HttpStatus.NOT_FOUND) {
            log.debug(
                    "DomainException (Not Found): code={}, status={}, detail={}",
                    errorCode,
                    mapped.status().value(),
                    mapped.detail());
        } else {
            log.warn(
                    "DomainException (Client Error): code={}, status={}, detail={}",
                    errorCode,
                    mapped.status().value(),
                    mapped.detail());
        }

        // RFC 7807: Content-Type + x-error-code 헤더
        return ResponseEntity.status(mapped.status())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .header(ERROR_CODE_HEADER, errorCode)
                .body(pd);
    }
}
