package com.ryuqq.setof.domain.member.vo;

/**
 * 탈퇴 사유 Enum
 *
 * <ul>
 *   <li>RARELY_USED: 이용 빈도 낮음
 *   <li>SERVICE_DISSATISFIED: 서비스 불만족
 *   <li>PRIVACY_CONCERN: 개인정보 우려
 *   <li>OTHER: 기타
 * </ul>
 */
public enum WithdrawalReason {
    RARELY_USED("이용 빈도 낮음"),
    SERVICE_DISSATISFIED("서비스 불만족"),
    PRIVACY_CONCERN("개인정보 우려"),
    OTHER("기타");

    private final String displayName;

    WithdrawalReason(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
