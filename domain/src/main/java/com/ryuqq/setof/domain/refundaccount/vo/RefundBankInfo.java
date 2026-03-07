package com.ryuqq.setof.domain.refundaccount.vo;

/**
 * 환불 계좌 은행 정보 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RefundBankInfo(String bankName, String accountNumber, String accountHolderName) {

    public RefundBankInfo {
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("은행명은 필수입니다");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("계좌번호는 필수입니다");
        }
        if (accountHolderName == null || accountHolderName.isBlank()) {
            throw new IllegalArgumentException("예금주는 필수입니다");
        }
        bankName = bankName.trim();
        accountNumber = accountNumber.trim();
        accountHolderName = accountHolderName.trim();
    }

    public static RefundBankInfo of(
            String bankName, String accountNumber, String accountHolderName) {
        return new RefundBankInfo(bankName, accountNumber, accountHolderName);
    }

    /** 계좌번호 마스킹 (뒤 4자리만 표시). */
    public String maskedAccountNumber() {
        if (accountNumber.length() <= 4) {
            return "****";
        }
        return "*".repeat(accountNumber.length() - 4)
                + accountNumber.substring(accountNumber.length() - 4);
    }
}
