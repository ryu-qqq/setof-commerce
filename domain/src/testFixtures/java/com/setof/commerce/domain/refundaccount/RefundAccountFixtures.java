package com.setof.commerce.domain.refundaccount;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;

/**
 * RefundAccount 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 RefundAccount 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountFixtures {

    private RefundAccountFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_BANK_NAME = "국민은행";
    public static final String DEFAULT_ACCOUNT_NUMBER = "123456789012";
    public static final String DEFAULT_ACCOUNT_HOLDER_NAME = "홍길동";
    public static final String DEFAULT_MEMBER_ID = "member-uuid-0001";

    // ===== ID Fixtures =====

    public static RefundAccountId defaultRefundAccountId() {
        return RefundAccountId.of(1L);
    }

    public static RefundAccountId refundAccountId(Long value) {
        return RefundAccountId.of(value);
    }

    public static RefundAccountId newRefundAccountId() {
        return RefundAccountId.forNew();
    }

    // ===== MemberId Fixtures =====

    public static MemberId defaultMemberId() {
        return MemberId.of(DEFAULT_MEMBER_ID);
    }

    public static MemberId memberId(String value) {
        return MemberId.of(value);
    }

    // ===== VO Fixtures =====

    public static RefundBankInfo defaultRefundBankInfo() {
        return RefundBankInfo.of(
                DEFAULT_BANK_NAME, DEFAULT_ACCOUNT_NUMBER, DEFAULT_ACCOUNT_HOLDER_NAME);
    }

    public static RefundBankInfo refundBankInfo(
            String bankName, String accountNumber, String holderName) {
        return RefundBankInfo.of(bankName, accountNumber, holderName);
    }

    public static RefundBankInfo updatedRefundBankInfo() {
        return RefundBankInfo.of("신한은행", "987654321098", "김철수");
    }

    // ===== Aggregate Fixtures =====

    public static RefundAccount newRefundAccount() {
        return RefundAccount.forNew(
                defaultMemberId(), defaultRefundBankInfo(), CommonVoFixtures.now());
    }

    public static RefundAccount activeRefundAccount() {
        return RefundAccount.reconstitute(
                RefundAccountId.of(1L),
                defaultMemberId(),
                defaultRefundBankInfo(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static RefundAccount activeRefundAccount(Long id) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                defaultMemberId(),
                defaultRefundBankInfo(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static RefundAccount deletedRefundAccount() {
        return RefundAccount.reconstitute(
                RefundAccountId.of(99L),
                defaultMemberId(),
                defaultRefundBankInfo(),
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== UpdateData Fixtures =====

    public static RefundAccountUpdateData defaultUpdateData() {
        return RefundAccountUpdateData.of(updatedRefundBankInfo(), CommonVoFixtures.now());
    }

    public static RefundAccountUpdateData updateDataWith(RefundBankInfo bankInfo) {
        return RefundAccountUpdateData.of(bankInfo, CommonVoFixtures.now());
    }
}
