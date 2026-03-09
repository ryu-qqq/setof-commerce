package com.ryuqq.setof.storage.legacy.composite.cart.dto;

/**
 * LegacyWebCartOptionQueryDto - 레거시 장바구니 옵션 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param optionGroupId 옵션 그룹 ID
 * @param optionDetailId 옵션 상세 ID
 * @param optionName 옵션명 (COLOR, SIZE 등)
 * @param optionValue 옵션값 ("레드", "XL" 등)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebCartOptionQueryDto(
        Long optionGroupId, Long optionDetailId, String optionName, String optionValue) {

    /**
     * null-safe 옵션값 반환.
     *
     * @return 옵션값 (null이면 빈 문자열)
     */
    public String getSafeOptionValue() {
        return optionValue != null ? optionValue : "";
    }
}
