package com.ryuqq.setof.adapter.out.client.portone.exception;

import com.ryuqq.setof.adapter.out.client.portone.dto.auth.PortOneErrorResponse;

/**
 * PortOne V2 인증 예외
 *
 * <p>PortOne API 인증 관련 오류 발생 시 던져지는 예외입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public class PortOneAuthException extends RuntimeException {

    private final String errorType;

    public PortOneAuthException(String message) {
        super(message);
        this.errorType = null;
    }

    public PortOneAuthException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = null;
    }

    public PortOneAuthException(PortOneErrorResponse errorResponse) {
        super(formatMessage(errorResponse));
        this.errorType = errorResponse != null ? errorResponse.type() : null;
    }

    public PortOneAuthException(String errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    /**
     * 에러 타입 반환
     *
     * @return 에러 타입 (INVALID_REQUEST, UNAUTHORIZED 등)
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * 유효하지 않은 요청 에러인지 확인
     *
     * @return INVALID_REQUEST 타입이면 true
     */
    public boolean isInvalidRequest() {
        return PortOneErrorResponse.INVALID_REQUEST.equals(errorType);
    }

    /**
     * 인증 실패 에러인지 확인
     *
     * @return UNAUTHORIZED 타입이면 true
     */
    public boolean isUnauthorized() {
        return PortOneErrorResponse.UNAUTHORIZED.equals(errorType);
    }

    private static String formatMessage(PortOneErrorResponse errorResponse) {
        if (errorResponse == null) {
            return "PortOne authentication failed: unknown error";
        }
        return String.format(
                "PortOne authentication failed: [%s] %s",
                errorResponse.type(), errorResponse.message());
    }

    /**
     * 유효하지 않은 요청 예외 생성
     *
     * @param message 에러 메시지
     * @return PortOneAuthException 인스턴스
     */
    public static PortOneAuthException invalidRequest(String message) {
        return new PortOneAuthException(PortOneErrorResponse.INVALID_REQUEST, message);
    }

    /**
     * 인증 실패 예외 생성
     *
     * @param message 에러 메시지
     * @return PortOneAuthException 인스턴스
     */
    public static PortOneAuthException unauthorized(String message) {
        return new PortOneAuthException(PortOneErrorResponse.UNAUTHORIZED, message);
    }
}
