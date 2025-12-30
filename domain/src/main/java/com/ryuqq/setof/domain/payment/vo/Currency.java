package com.ryuqq.setof.domain.payment.vo;

/**
 * Currency - 통화 단위
 *
 * <p>지원하는 통화를 정의합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 * </ul>
 */
public enum Currency {

    /** 대한민국 원화 */
    KRW("대한민국 원", "₩", 0),

    /** 미국 달러 */
    USD("미국 달러", "$", 2),

    /** 일본 엔화 */
    JPY("일본 엔", "¥", 0);

    private final String description;
    private final String symbol;
    private final int decimalPlaces;

    Currency(String description, String symbol, int decimalPlaces) {
        this.description = description;
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
    }

    /**
     * 통화 설명 반환
     *
     * @return 통화 설명
     */
    public String description() {
        return description;
    }

    /**
     * 통화 기호 반환
     *
     * @return 통화 기호
     */
    public String symbol() {
        return symbol;
    }

    /**
     * 소수점 자릿수 반환
     *
     * @return 소수점 자릿수
     */
    public int decimalPlaces() {
        return decimalPlaces;
    }

    /**
     * 기본 통화 반환
     *
     * @return KRW
     */
    public static Currency defaultCurrency() {
        return KRW;
    }
}
