package com.ryuqq.setof.domain.payment.vo;

/** PG 트랜잭션 정보 Value Object. */
public record PgTransactionInfo(String agencyId, String uniqueId, String receiptUrl) {

    public PgTransactionInfo {
        if (agencyId == null || agencyId.isBlank()) {
            throw new IllegalArgumentException("PG사 ID는 필수입니다");
        }
        if (uniqueId == null || uniqueId.isBlank()) {
            throw new IllegalArgumentException("고유 거래번호는 필수입니다");
        }
    }

    public static PgTransactionInfo of(String agencyId, String uniqueId, String receiptUrl) {
        return new PgTransactionInfo(agencyId, uniqueId, receiptUrl);
    }
}
