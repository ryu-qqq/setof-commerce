package com.ryuqq.setof.application.refundaccount.dto.command;

import java.util.UUID;

/**
 * 환불계좌 수정 Command DTO
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param refundAccountId 환불계좌 ID
 * @param bankId 새 은행 ID
 * @param accountNumber 새 계좌번호
 * @param accountHolderName 새 예금주명
 */
public record UpdateRefundAccountCommand(
        UUID memberId,
        Long refundAccountId,
        Long bankId,
        String accountNumber,
        String accountHolderName) {

    /** Static Factory Method */
    public static UpdateRefundAccountCommand of(
            UUID memberId,
            Long refundAccountId,
            Long bankId,
            String accountNumber,
            String accountHolderName) {
        return new UpdateRefundAccountCommand(
                memberId, refundAccountId, bankId, accountNumber, accountHolderName);
    }
}
