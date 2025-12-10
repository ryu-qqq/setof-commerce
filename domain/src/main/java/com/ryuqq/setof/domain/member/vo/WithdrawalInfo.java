package com.ryuqq.setof.domain.member.vo;

import com.ryuqq.setof.domain.member.exception.InvalidWithdrawalInfoException;
import java.time.Instant;

/**
 * 회원 탈퇴 정보 Value Object
 *
 * <p>회원 탈퇴 시 기록되는 정보:
 *
 * <ul>
 *   <li>reason: 탈퇴 사유 (필수)
 *   <li>withdrawnAt: 탈퇴 일시 (필수, UTC 기준 Instant)
 * </ul>
 *
 * <p>Instant 사용 이유:
 *
 * <ul>
 *   <li>타임존 독립적인 절대 시간 표현
 *   <li>전 세계 어디서나 동일한 값으로 비교 가능
 *   <li>DB 저장 시 UTC 기준으로 일관성 보장
 * </ul>
 *
 * @param reason 탈퇴 사유
 * @param withdrawnAt 탈퇴 일시 (UTC 기준)
 */
public record WithdrawalInfo(WithdrawalReason reason, Instant withdrawnAt) {

    public WithdrawalInfo {
        validate(reason, withdrawnAt);
    }

    public static WithdrawalInfo of(WithdrawalReason reason, Instant withdrawnAt) {
        return new WithdrawalInfo(reason, withdrawnAt);
    }

    private static void validate(WithdrawalReason reason, Instant withdrawnAt) {
        if (reason == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 사유는 필수입니다.");
        }
        if (withdrawnAt == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 일시는 필수입니다.");
        }
    }
}
