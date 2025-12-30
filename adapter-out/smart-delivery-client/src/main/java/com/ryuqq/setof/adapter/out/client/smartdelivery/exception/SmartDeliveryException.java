package com.ryuqq.setof.adapter.out.client.smartdelivery.exception;

/**
 * SmartDeliveryException - 스마트택배 API 예외
 *
 * <p>스마트택배 API 호출 중 발생한 예외를 표현합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class SmartDeliveryException extends RuntimeException {

    private final ErrorType errorType;

    /**
     * 기본 생성자
     *
     * @param message 오류 메시지
     */
    public SmartDeliveryException(String message) {
        super(message);
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * 에러 타입 포함 생성자
     *
     * @param errorType 에러 타입
     * @param message 오류 메시지
     */
    public SmartDeliveryException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    /**
     * 원인 예외 포함 생성자
     *
     * @param errorType 에러 타입
     * @param message 오류 메시지
     * @param cause 원인 예외
     */
    public SmartDeliveryException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    /**
     * 에러 타입 반환
     *
     * @return ErrorType
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * 재시도 가능 여부 확인
     *
     * @return 재시도 가능하면 true
     */
    public boolean isRetryable() {
        return errorType.isRetryable();
    }

    /** 에러 타입 enum */
    public enum ErrorType {

        /** 알 수 없는 오류 */
        UNKNOWN(false),

        /** 네트워크 오류 */
        NETWORK(true),

        /** 서버 오류 (5xx) */
        SERVER_ERROR(true),

        /** 클라이언트 오류 (4xx) */
        BAD_REQUEST(false),

        /** 인증 오류 */
        AUTHENTICATION(false),

        /** API 키 오류 */
        INVALID_API_KEY(false),

        /** Rate Limit 초과 */
        RATE_LIMIT(true),

        /** 서킷 오픈 */
        CIRCUIT_OPEN(false),

        /** 운송장 없음 */
        INVOICE_NOT_FOUND(false),

        /** 택배사 미지원 */
        CARRIER_NOT_SUPPORTED(false);

        private final boolean retryable;

        ErrorType(boolean retryable) {
            this.retryable = retryable;
        }

        public boolean isRetryable() {
            return retryable;
        }
    }
}
