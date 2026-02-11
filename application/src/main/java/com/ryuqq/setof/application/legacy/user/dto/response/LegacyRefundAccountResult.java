package com.ryuqq.setof.application.legacy.user.dto.response;

/**
 * 레거시 환불 계좌 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param refundAccountId 환불 계좌 ID
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyRefundAccountResult(
        long refundAccountId, String bankName, String accountNumber, String accountHolderName) {

    public static LegacyRefundAccountResult of(
            long refundAccountId, String bankName, String accountNumber, String accountHolderName) {
        return new LegacyRefundAccountResult(
                refundAccountId, bankName, accountNumber, accountHolderName);
    }
}
