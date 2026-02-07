package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyOptionResult - 레거시 옵션 결과 DTO (주문 상품 내 중첩 객체).
 *
 * @param optionGroupId 옵션 그룹 ID
 * @param optionDetailId 옵션 상세 ID
 * @param optionName 옵션명
 * @param optionValue 옵션값
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOptionResult(
        long optionGroupId, long optionDetailId, String optionName, String optionValue) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionDetailId 옵션 상세 ID
     * @param optionName 옵션명
     * @param optionValue 옵션값
     * @return LegacyOptionResult
     */
    public static LegacyOptionResult of(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {
        return new LegacyOptionResult(optionGroupId, optionDetailId, optionName, optionValue);
    }
}
