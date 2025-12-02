package com.ryuqq.setof.domain.core.member.vo;

import java.time.LocalDateTime;

/**
 * WithdrawalInfo TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 WithdrawalInfo 인스턴스 생성을 위한 팩토리 클래스</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * WithdrawalInfo info = WithdrawalInfoFixture.create();
 * WithdrawalInfo rarelyUsed = WithdrawalInfoFixture.createRarelyUsed();
 * WithdrawalInfo custom = WithdrawalInfoFixture.create(WithdrawalReason.OTHER, LocalDateTime.now());
 * }</pre>
 */
public final class WithdrawalInfoFixture {

    private static final WithdrawalReason DEFAULT_REASON = WithdrawalReason.RARELY_USED;
    private static final LocalDateTime DEFAULT_WITHDRAWN_AT = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

    private WithdrawalInfoFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 WithdrawalInfo 생성 (거의 사용하지 않음, 고정 시간)
     *
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo create() {
        return WithdrawalInfo.of(DEFAULT_REASON, DEFAULT_WITHDRAWN_AT);
    }

    /**
     * 지정된 사유와 시간으로 WithdrawalInfo 생성
     *
     * @param reason 탈퇴 사유
     * @param withdrawnAt 탈퇴 시간
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo create(WithdrawalReason reason, LocalDateTime withdrawnAt) {
        return WithdrawalInfo.of(reason, withdrawnAt);
    }

    /**
     * 현재 시간으로 WithdrawalInfo 생성
     *
     * @param reason 탈퇴 사유
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo createNow(WithdrawalReason reason) {
        return WithdrawalInfo.of(reason, LocalDateTime.now());
    }

    /**
     * 거의 사용하지 않음 사유로 WithdrawalInfo 생성
     *
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo createRarelyUsed() {
        return WithdrawalInfo.of(WithdrawalReason.RARELY_USED, DEFAULT_WITHDRAWN_AT);
    }

    /**
     * 서비스 불만족 사유로 WithdrawalInfo 생성
     *
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo createServiceDissatisfied() {
        return WithdrawalInfo.of(WithdrawalReason.SERVICE_DISSATISFIED, DEFAULT_WITHDRAWN_AT);
    }

    /**
     * 개인정보 우려 사유로 WithdrawalInfo 생성
     *
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo createPrivacyConcern() {
        return WithdrawalInfo.of(WithdrawalReason.PRIVACY_CONCERN, DEFAULT_WITHDRAWN_AT);
    }

    /**
     * 기타 사유로 WithdrawalInfo 생성
     *
     * @return WithdrawalInfo 인스턴스
     */
    public static WithdrawalInfo createOther() {
        return WithdrawalInfo.of(WithdrawalReason.OTHER, DEFAULT_WITHDRAWN_AT);
    }
}
