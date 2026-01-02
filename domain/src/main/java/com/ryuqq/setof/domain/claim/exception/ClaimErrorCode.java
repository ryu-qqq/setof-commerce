package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ClaimErrorCode - 클레임 도메인 에러 코드
 *
 * <p>Claim Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p>에러 코드 형식: CLAIM-{NUMBER}
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ClaimErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_CLAIM_ID("CLAIM-001", 400, "유효하지 않은 클레임 ID입니다"),
    INVALID_CLAIM_NUMBER("CLAIM-002", 400, "유효하지 않은 클레임 번호입니다"),
    INVALID_CLAIM_TYPE("CLAIM-003", 400, "유효하지 않은 클레임 유형입니다"),
    INVALID_CLAIM_REASON("CLAIM-004", 400, "유효하지 않은 클레임 사유입니다"),
    INVALID_QUANTITY("CLAIM-005", 400, "유효하지 않은 수량입니다"),
    INVALID_REFUND_AMOUNT("CLAIM-006", 400, "유효하지 않은 환불 금액입니다"),
    REJECT_REASON_REQUIRED("CLAIM-007", 400, "반려 사유를 입력해주세요"),

    // 404 Not Found
    CLAIM_NOT_FOUND("CLAIM-010", 404, "클레임을 찾을 수 없습니다"),

    // 409 Conflict - 상태 관련
    CLAIM_CANNOT_APPROVE("CLAIM-020", 409, "승인할 수 없는 클레임 상태입니다"),
    CLAIM_CANNOT_REJECT("CLAIM-021", 409, "반려할 수 없는 클레임 상태입니다"),
    CLAIM_CANNOT_START_PROCESSING("CLAIM-022", 409, "처리를 시작할 수 없는 클레임 상태입니다"),
    CLAIM_CANNOT_COMPLETE("CLAIM-023", 409, "완료 처리할 수 없는 클레임 상태입니다"),
    CLAIM_CANNOT_CANCEL("CLAIM-024", 409, "취소할 수 없는 클레임 상태입니다"),
    CLAIM_ALREADY_EXISTS("CLAIM-025", 409, "이미 처리 중인 클레임이 존재합니다"),

    // 409 Conflict - 비즈니스 규칙
    CLAIM_NOT_ALLOWED_BEFORE_SHIPPING("CLAIM-030", 409, "배송 전에는 해당 클레임을 요청할 수 없습니다"),
    CLAIM_NOT_ALLOWED_AFTER_SHIPPING("CLAIM-031", 409, "배송 후에는 해당 클레임을 요청할 수 없습니다"),
    CLAIM_PERIOD_EXPIRED("CLAIM-032", 409, "클레임 신청 기간이 만료되었습니다"),

    // 409 Conflict - 반품 배송 상태 관련
    CANNOT_SCHEDULE_PICKUP("CLAIM-040", 409, "수거 예약을 할 수 없는 상태입니다"),
    CANNOT_REGISTER_RETURN_SHIPPING("CLAIM-041", 409, "반품 송장을 등록할 수 없는 상태입니다"),
    CANNOT_CONFIRM_RETURN_RECEIVED("CLAIM-042", 409, "반품 수령을 확인할 수 없는 상태입니다"),
    CANNOT_REGISTER_EXCHANGE_SHIPPING("CLAIM-043", 409, "교환품 발송을 등록할 수 없는 상태입니다"),
    RETURN_NOT_REQUIRED("CLAIM-044", 409, "반품이 필요하지 않은 클레임 유형입니다"),
    EXCHANGE_NOT_APPLICABLE("CLAIM-045", 409, "교환이 적용되지 않는 클레임 유형입니다"),

    // 400 Bad Request - 반품 배송 관련
    INVALID_TRACKING_NUMBER("CLAIM-050", 400, "유효하지 않은 송장 번호입니다"),
    INVALID_CARRIER("CLAIM-051", 400, "유효하지 않은 배송사입니다"),
    INVALID_PICKUP_SCHEDULE("CLAIM-052", 400, "유효하지 않은 수거 예약 일시입니다"),
    PICKUP_ADDRESS_REQUIRED("CLAIM-053", 400, "수거지 주소는 필수입니다"),
    INSPECTION_RESULT_REQUIRED("CLAIM-054", 400, "검수 결과는 필수입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    ClaimErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
