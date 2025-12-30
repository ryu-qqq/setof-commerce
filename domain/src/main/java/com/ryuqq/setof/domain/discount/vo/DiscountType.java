package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 타입 Enum
 *
 * <p>할인 계산 방식을 정의합니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>RATE: 정률 할인 (예: 10% 할인)
 *   <li>FIXED_PRICE: 정액 할인 (예: 1,000원 할인)
 *   <li>TIERED_RATE: 구간별 정률 할인 (예: 5만원 이상 10%, 10만원 이상 15%)
 *   <li>TIERED_PRICE: 구간별 정액 할인 (예: 5만원 이상 3,000원, 10만원 이상 7,000원)
 * </ul>
 */
public enum DiscountType {
    /** 정률 할인 - 주문 금액의 일정 비율 할인 */
    RATE("정률 할인"),

    /** 정액 할인 - 고정 금액 할인 */
    FIXED_PRICE("정액 할인"),

    /** 구간별 정률 할인 - 주문 금액 구간에 따른 비율 할인 */
    TIERED_RATE("구간별 정률 할인"),

    /** 구간별 정액 할인 - 주문 금액 구간에 따른 고정 금액 할인 */
    TIERED_PRICE("구간별 정액 할인");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 정률 할인 여부 확인
     *
     * @return 정률 할인이면 true
     */
    public boolean isRateType() {
        return this == RATE || this == TIERED_RATE;
    }

    /**
     * 정액 할인 여부 확인
     *
     * @return 정액 할인이면 true
     */
    public boolean isFixedPriceType() {
        return this == FIXED_PRICE || this == TIERED_PRICE;
    }

    /**
     * 정액 할인 여부 확인 (alias)
     *
     * @return 정액 할인이면 true
     */
    public boolean isFixedType() {
        return isFixedPriceType();
    }

    /**
     * 구간별 할인 여부 확인
     *
     * @return 구간별 할인이면 true
     */
    public boolean isTieredType() {
        return this == TIERED_RATE || this == TIERED_PRICE;
    }
}
