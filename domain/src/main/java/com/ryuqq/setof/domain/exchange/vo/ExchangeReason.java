package com.ryuqq.setof.domain.exchange.vo;

/**
 * 교환 사유 Value Object.
 *
 * <p>교환 사유 유형과 상세 내용을 포함합니다.
 *
 * @param type 교환 사유 유형 (필수)
 * @param detail 교환 사유 상세 내용 (선택)
 */
public record ExchangeReason(ExchangeReasonType type, String detail) {

    public ExchangeReason {
        if (type == null) {
            throw new IllegalArgumentException("교환 사유 유형은 필수입니다");
        }
    }

    public static ExchangeReason of(ExchangeReasonType type, String detail) {
        return new ExchangeReason(type, detail);
    }

    public static ExchangeReason ofType(ExchangeReasonType type) {
        return new ExchangeReason(type, null);
    }
}
