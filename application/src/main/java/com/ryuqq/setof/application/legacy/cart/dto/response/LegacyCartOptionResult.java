package com.ryuqq.setof.application.legacy.cart.dto.response;

/**
 * LegacyCartOptionResult - 레거시 장바구니 옵션 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param optionGroupId 옵션 그룹 ID
 * @param optionDetailId 옵션 상세 ID
 * @param optionName 옵션명 (COLOR, SIZE 등)
 * @param optionValue 옵션값 ("레드", "XL" 등)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCartOptionResult(
        long optionGroupId, long optionDetailId, String optionName, String optionValue) {

    /**
     * 팩토리 메서드.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionDetailId 옵션 상세 ID
     * @param optionName 옵션명
     * @param optionValue 옵션값
     * @return LegacyCartOptionResult
     */
    public static LegacyCartOptionResult of(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {
        return new LegacyCartOptionResult(optionGroupId, optionDetailId, optionName, optionValue);
    }
}
