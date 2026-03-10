package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 변동 사유.
 *
 * <p>레거시 매핑: SAVE→EARN, USE→USE, REFUND→REFUND, EXPIRED→EXPIRE.
 */
public enum MileageReason {

    /** 적립 (구매확정, 회원가입, 리뷰, 이벤트 등). */
    EARN,

    /** 사용 (결제 시 차감). */
    USE,

    /** 환불 (전체 취소 시 사용분 반환). */
    REFUND,

    /** 만료 (유효기간 경과). */
    EXPIRE,

    /** 회수 (취소된 주문의 적립분 회수). */
    REVOKE,

    /** 관리자 수동 지급. */
    ADMIN_GRANT;

    public boolean isPositive() {
        return this == EARN || this == REFUND || this == ADMIN_GRANT;
    }

    public boolean isNegative() {
        return this == USE || this == EXPIRE || this == REVOKE;
    }
}
