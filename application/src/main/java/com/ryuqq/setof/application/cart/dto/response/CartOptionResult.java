package com.ryuqq.setof.application.cart.dto.response;

/**
 * 장바구니 옵션 결과 DTO.
 *
 * @param optionGroupId 옵션 그룹 ID
 * @param optionDetailId 옵션 상세 ID
 * @param optionName 옵션명 (COLOR, SIZE 등)
 * @param optionValue 옵션값 ("레드", "XL" 등)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartOptionResult(
        long optionGroupId, long optionDetailId, String optionName, String optionValue) {

    public static CartOptionResult of(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {
        return new CartOptionResult(optionGroupId, optionDetailId, optionName, optionValue);
    }
}
