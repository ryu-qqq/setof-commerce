package com.ryuqq.setof.adapter.out.persistence.cart.dto;

/**
 * CartOptionQueryDto - 장바구니 옵션 조회용 DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartOptionQueryDto(
        long optionGroupId, long optionValueId, String optionGroupName, String optionValueName) {

    public String getSafeOptionValue() {
        return optionValueName != null ? optionValueName : "";
    }
}
