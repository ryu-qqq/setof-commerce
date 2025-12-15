package com.ryuqq.setof.adapter.out.client.portone.exception;

/**
 * PortOne API 최상위 예외
 *
 * <p>모든 PortOne 관련 예외의 기본 클래스입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public class PortOneException extends RuntimeException {

    private final ErrorType errorType;
    private final boolean retryable;

    public PortOneException(String message) {
        super(message);
        this.errorType = ErrorType.UNKNOWN;
        this.retryable = false;
    }

    public PortOneException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = ErrorType.UNKNOWN;
        this.retryable = false;
    }

    public PortOneException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.retryable = errorType.isRetryable();
    }

    public PortOneException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.retryable = errorType.isRetryable();
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public boolean isRetryable() {
        return retryable;
    }

    /** PortOne API 에러 타입 */
    public enum ErrorType {
        /** 인증 실패 (토큰 만료, 잘못된 시크릿 등) */
        AUTHENTICATION(true),

        /** 권한 없음 */
        AUTHORIZATION(false),

        /** 잘못된 요청 (파라미터 오류 등) */
        BAD_REQUEST(false),

        /** 리소스 없음 */
        NOT_FOUND(false),

        /** 요청 제한 초과 */
        RATE_LIMIT(true),

        /** 서버 에러 */
        SERVER_ERROR(true),

        /** 네트워크/타임아웃 에러 */
        NETWORK(true),

        /** 서킷 오픈 상태 */
        CIRCUIT_OPEN(false),

        /** 알 수 없는 에러 */
        UNKNOWN(false);

        private final boolean retryable;

        ErrorType(boolean retryable) {
            this.retryable = retryable;
        }

        public boolean isRetryable() {
            return retryable;
        }
    }
}
