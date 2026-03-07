package com.ryuqq.setof.application.refundaccount;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import java.time.Instant;

/**
 * RefundAccount 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    private RefundAccountDomainFixtures() {}

    // ===== RefundAccount (재구성) =====

    public static RefundAccount activeRefundAccount(Long refundAccountId, Long userId) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(refundAccountId),
                MemberId.of(String.valueOf(userId)),
                RefundBankInfo.of("신한은행", "110-123-456789", "홍길동"),
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static RefundAccount activeRefundAccount(
            Long refundAccountId,
            Long userId,
            String bankName,
            String accountNumber,
            String holderName) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(refundAccountId),
                MemberId.of(String.valueOf(userId)),
                RefundBankInfo.of(bankName, accountNumber, holderName),
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static RefundAccount deletedRefundAccount(Long refundAccountId, Long userId) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(refundAccountId),
                MemberId.of(String.valueOf(userId)),
                RefundBankInfo.of("신한은행", "110-123-456789", "홍길동"),
                DeletionStatus.deletedAt(FIXED_NOW),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static RefundAccount newRefundAccount(Long userId) {
        return RefundAccount.forNew(
                MemberId.of(String.valueOf(userId)),
                RefundBankInfo.of("신한은행", "110-123-456789", "홍길동"),
                FIXED_NOW);
    }

    public static RefundAccount newRefundAccount(
            Long userId, String bankName, String accountNumber, String holderName) {
        return RefundAccount.forNew(
                MemberId.of(String.valueOf(userId)),
                RefundBankInfo.of(bankName, accountNumber, holderName),
                FIXED_NOW);
    }

    // ===== RefundAccountUpdateData =====

    public static RefundAccountUpdateData updateData() {
        return RefundAccountUpdateData.of(
                RefundBankInfo.of("국민은행", "123-456-789012", "김철수"), FIXED_NOW);
    }

    public static RefundAccountUpdateData updateData(
            String bankName, String accountNumber, String holderName) {
        return RefundAccountUpdateData.of(
                RefundBankInfo.of(bankName, accountNumber, holderName), FIXED_NOW);
    }

    // ===== RefundBankInfo =====

    public static RefundBankInfo defaultBankInfo() {
        return RefundBankInfo.of("신한은행", "110-123-456789", "홍길동");
    }

    public static RefundBankInfo updatedBankInfo() {
        return RefundBankInfo.of("국민은행", "123-456-789012", "김철수");
    }
}
