package com.ryuqq.setof.application.refundaccount;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;

/**
 * RefundAccount Application Command 테스트 Fixtures.
 *
 * <p>환불 계좌 등록/수정/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class RefundAccountCommandFixtures {

    private RefundAccountCommandFixtures() {}

    public static final Long DEFAULT_USER_ID = 1L;
    public static final Long DEFAULT_REFUND_ACCOUNT_ID = 100L;

    // ===== RegisterRefundAccountCommand =====

    public static RegisterRefundAccountCommand registerCommand() {
        return new RegisterRefundAccountCommand(DEFAULT_USER_ID, "신한은행", "110-123-456789", "홍길동");
    }

    public static RegisterRefundAccountCommand registerCommand(Long userId) {
        return new RegisterRefundAccountCommand(userId, "신한은행", "110-123-456789", "홍길동");
    }

    public static RegisterRefundAccountCommand registerCommand(
            Long userId, String bankName, String accountNumber, String holderName) {
        return new RegisterRefundAccountCommand(userId, bankName, accountNumber, holderName);
    }

    // ===== UpdateRefundAccountCommand =====

    public static UpdateRefundAccountCommand updateCommand() {
        return new UpdateRefundAccountCommand(
                DEFAULT_USER_ID, DEFAULT_REFUND_ACCOUNT_ID, "국민은행", "123-456-789012", "김철수");
    }

    public static UpdateRefundAccountCommand updateCommand(Long userId, Long refundAccountId) {
        return new UpdateRefundAccountCommand(
                userId, refundAccountId, "국민은행", "123-456-789012", "김철수");
    }

    public static UpdateRefundAccountCommand updateCommand(
            Long userId,
            Long refundAccountId,
            String bankName,
            String accountNumber,
            String holderName) {
        return new UpdateRefundAccountCommand(
                userId, refundAccountId, bankName, accountNumber, holderName);
    }

    // ===== DeleteRefundAccountCommand =====

    public static DeleteRefundAccountCommand deleteCommand() {
        return new DeleteRefundAccountCommand(DEFAULT_USER_ID, DEFAULT_REFUND_ACCOUNT_ID);
    }

    public static DeleteRefundAccountCommand deleteCommand(Long userId, Long refundAccountId) {
        return new DeleteRefundAccountCommand(userId, refundAccountId);
    }
}
