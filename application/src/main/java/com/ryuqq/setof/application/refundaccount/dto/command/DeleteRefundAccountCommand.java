package com.ryuqq.setof.application.refundaccount.dto.command;

import java.util.UUID;

/**
 * 환불계좌 삭제 Command DTO
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param refundAccountId 환불계좌 ID
 */
public record DeleteRefundAccountCommand(UUID memberId, Long refundAccountId) {

    /**
     * Static Factory Method
     */
    public static DeleteRefundAccountCommand of(UUID memberId, Long refundAccountId) {
        return new DeleteRefundAccountCommand(memberId, refundAccountId);
    }
}
