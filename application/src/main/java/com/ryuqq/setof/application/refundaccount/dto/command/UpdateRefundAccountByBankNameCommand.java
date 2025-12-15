package com.ryuqq.setof.application.refundaccount.dto.command;

import java.util.UUID;

/**
 * 환불계좌 수정 Command DTO (은행 이름 기반)
 *
 * <p>V1 레거시 API 지원을 위한 Command입니다. V1에서는 bankId 대신 bankName을 사용합니다.
 *
 * @param memberId 회원 ID (소유권 검증용)
 * @param refundAccountId 환불계좌 ID
 * @param bankName 새 은행 이름 (예: "국민은행", "신한은행")
 * @param accountNumber 새 계좌번호
 * @param accountHolderName 새 예금주명
 * @deprecated V2 API에서는 bankId 기반 UpdateRefundAccountCommand 사용 권장
 */
@Deprecated
public record UpdateRefundAccountByBankNameCommand(
        UUID memberId,
        Long refundAccountId,
        String bankName,
        String accountNumber,
        String accountHolderName) {

    /** Static Factory Method */
    public static UpdateRefundAccountByBankNameCommand of(
            UUID memberId,
            Long refundAccountId,
            String bankName,
            String accountNumber,
            String accountHolderName) {
        return new UpdateRefundAccountByBankNameCommand(
                memberId, refundAccountId, bankName, accountNumber, accountHolderName);
    }
}
