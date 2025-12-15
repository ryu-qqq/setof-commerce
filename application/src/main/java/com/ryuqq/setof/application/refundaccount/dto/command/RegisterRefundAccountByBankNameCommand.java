package com.ryuqq.setof.application.refundaccount.dto.command;

import java.util.UUID;

/**
 * 환불계좌 등록 Command DTO (은행 이름 기반)
 *
 * <p>V1 레거시 API 지원을 위한 Command입니다. V1에서는 bankId 대신 bankName을 사용합니다.
 *
 * @param memberId 회원 ID
 * @param bankName 은행 이름 (예: "국민은행", "신한은행")
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @deprecated V2 API에서는 bankId 기반 RegisterRefundAccountCommand 사용 권장
 */
@Deprecated
public record RegisterRefundAccountByBankNameCommand(
        UUID memberId, String bankName, String accountNumber, String accountHolderName) {

    /** Static Factory Method */
    public static RegisterRefundAccountByBankNameCommand of(
            UUID memberId, String bankName, String accountNumber, String accountHolderName) {
        return new RegisterRefundAccountByBankNameCommand(
                memberId, bankName, accountNumber, accountHolderName);
    }
}
