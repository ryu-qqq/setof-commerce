package com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto;

import com.ryuqq.setof.domain.common.exception.DomainException;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

/**
 * V1ErrorResponse - V1 API 에러 응답.
 *
 * <p>레거시 호환을 위한 V1 에러 응답 구조입니다.
 *
 * <p>레거시 ErrorResponse.java 구조:
 *
 * <pre>
 * {
 *   "status": 400,
 *   "message": "에러 메시지",
 *   "error": "ExceptionClassName",
 *   "timestamp": "2024-01-01 12:00:00"
 * }
 * </pre>
 *
 * <p>V2 에러 응답(RFC 7807 ProblemDetail)과의 차이점:
 *
 * <ul>
 *   <li>V1: status, message, error, timestamp
 *   <li>V2: type, title, status, detail, instance, code, traceId, ...
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 API 에러 응답")
public record V1ErrorResponse(
        @Schema(description = "HTTP 상태 코드", example = "400") int status,
        @Schema(description = "에러 메시지", example = "잘못된 요청입니다") String message,
        @Schema(description = "예외 클래스명", example = "IllegalArgumentException") String error,
        @Schema(description = "발생 시간", example = "2024-01-01 12:00:00") String timestamp) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * HTTP 상태, 예외 클래스명, 메시지로 에러 응답 생성.
     *
     * @param httpStatus HTTP 상태
     * @param exceptionClassName 예외 클래스 이름
     * @param detailMessage 상세 메시지
     * @return V1ErrorResponse 인스턴스
     */
    public static V1ErrorResponse of(
            HttpStatus httpStatus, String exceptionClassName, String detailMessage) {
        return new V1ErrorResponse(
                httpStatus.value(), detailMessage, exceptionClassName, nowFormatted());
    }

    /**
     * DomainException으로부터 에러 응답 생성.
     *
     * @param ex 도메인 예외
     * @param httpStatus HTTP 상태
     * @return V1ErrorResponse 인스턴스
     */
    public static V1ErrorResponse of(DomainException ex, HttpStatus httpStatus) {
        return new V1ErrorResponse(
                httpStatus.value(), ex.getMessage(), ex.getClass().getSimpleName(), nowFormatted());
    }

    /**
     * 일반 Exception으로부터 에러 응답 생성.
     *
     * @param ex 예외
     * @param httpStatus HTTP 상태
     * @return V1ErrorResponse 인스턴스
     */
    public static V1ErrorResponse of(Exception ex, HttpStatus httpStatus) {
        return new V1ErrorResponse(
                httpStatus.value(), ex.getMessage(), ex.getClass().getSimpleName(), nowFormatted());
    }

    /**
     * 상태 코드와 메시지로 에러 응답 생성.
     *
     * @param status HTTP 상태 코드
     * @param message 에러 메시지
     * @param errorType 에러 타입명
     * @return V1ErrorResponse 인스턴스
     */
    public static V1ErrorResponse of(int status, String message, String errorType) {
        return new V1ErrorResponse(status, message, errorType, nowFormatted());
    }

    private static String nowFormatted() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
