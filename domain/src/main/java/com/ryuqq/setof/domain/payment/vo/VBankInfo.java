package com.ryuqq.setof.domain.payment.vo;

import java.time.Instant;

/**
 * 가상계좌 정보 VO.
 *
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param depositAmount 입금 금액
 * @param dueDate 입금 기한
 */
public record VBankInfo(
        String bankName, String accountNumber, long depositAmount, Instant dueDate) {

    public VBankInfo {
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("은행명은 필수입니다");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("계좌번호는 필수입니다");
        }
    }

    public static VBankInfo of(
            String bankName, String accountNumber, long depositAmount, Instant dueDate) {
        return new VBankInfo(bankName, accountNumber, depositAmount, dueDate);
    }
}
