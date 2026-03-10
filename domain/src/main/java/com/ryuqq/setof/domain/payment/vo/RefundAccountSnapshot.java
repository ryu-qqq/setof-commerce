package com.ryuqq.setof.domain.payment.vo;

/**
 * 결제 시점의 환불 계좌 스냅샷 VO.
 *
 * @param userId 회원 ID
 * @param bankCode 은행 코드
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundAccountSnapshot(
        long userId, String bankCode, String accountNumber, String accountHolderName) {

    public RefundAccountSnapshot {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be positive");
        }
        if (bankCode == null || bankCode.isBlank()) {
            throw new IllegalArgumentException("은행 코드는 필수입니다");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("계좌번호는 필수입니다");
        }
        if (accountHolderName == null || accountHolderName.isBlank()) {
            throw new IllegalArgumentException("예금주는 필수입니다");
        }
    }

    public static RefundAccountSnapshot of(
            long userId, String bankCode, String accountNumber, String accountHolderName) {
        return new RefundAccountSnapshot(userId, bankCode, accountNumber, accountHolderName);
    }
}
