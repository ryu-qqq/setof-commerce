package com.ryuqq.setof.application.refundaccount;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;

/**
 * RefundAccount Application Query 테스트 Fixtures.
 *
 * <p>환불 계좌 조회 관련 Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountQueryFixtures {

    private RefundAccountQueryFixtures() {}

    // ===== RefundAccountResult =====

    public static RefundAccountResult refundAccountResult(Long refundAccountId) {
        return RefundAccountResult.of(refundAccountId, "신한은행", "110-123-456789", "홍길동");
    }

    public static RefundAccountResult refundAccountResult(
            Long refundAccountId, String bankName, String accountNumber, String holderName) {
        return RefundAccountResult.of(refundAccountId, bankName, accountNumber, holderName);
    }

    public static RefundAccountResult updatedRefundAccountResult(Long refundAccountId) {
        return RefundAccountResult.of(refundAccountId, "국민은행", "123-456-789012", "김철수");
    }
}
