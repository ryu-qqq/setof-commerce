package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidWithdrawalInfoException;
import com.ryuqq.setof.domain.core.member.type.WithdrawalReason;

import java.time.LocalDateTime;

/**
 * 회원 탈퇴 정보 Value Object
 *
 * <p>회원 탈퇴 시 기록되는 정보:</p>
 * <ul>
 *     <li>reason: 탈퇴 사유 (필수)</li>
 *     <li>withdrawnAt: 탈퇴 일시 (필수)</li>
 * </ul>
 *
 * @param reason 탈퇴 사유
 * @param withdrawnAt 탈퇴 일시
 */
public record WithdrawalInfo(
    WithdrawalReason reason,
    LocalDateTime withdrawnAt
) {

    public WithdrawalInfo {
        validate(reason, withdrawnAt);
    }

    public static WithdrawalInfo of(WithdrawalReason reason, LocalDateTime withdrawnAt) {
        return new WithdrawalInfo(reason, withdrawnAt);
    }

    private static void validate(WithdrawalReason reason, LocalDateTime withdrawnAt) {
        if (reason == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 사유는 필수입니다.");
        }
        if (withdrawnAt == null) {
            throw new InvalidWithdrawalInfoException("탈퇴 일시는 필수입니다.");
        }
    }
}
