package com.ryuqq.setof.application.refundaccount.dto.command;

import java.util.UUID;

/**
 * 환불계좌 등록 Command DTO
 *
 * @param memberId 회원 ID
 * @param bankId 은행 ID
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 */
public record RegisterRefundAccountCommand(
        UUID memberId, Long bankId, String accountNumber, String accountHolderName) {

    /** Static Factory Method */
    public static RegisterRefundAccountCommand of(
            UUID memberId, Long bankId, String accountNumber, String accountHolderName) {
        return new RegisterRefundAccountCommand(memberId, bankId, accountNumber, accountHolderName);
    }
}
