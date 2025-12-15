package com.setof.connectly.module.discount.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountRedisFetchDto {
    private String productGroupId;
    private String sellerId;

    /**
     * 추후에 즉할 로직 추가 될 시 brnadId, categoryId 등등 추가 될 수 있음
     *
     * @param productGroupId
     * @param sellerId
     */
    @Builder
    public DiscountRedisFetchDto(long productGroupId, long sellerId) {
        this.productGroupId = String.valueOf(productGroupId);
        this.sellerId = String.valueOf(sellerId);
    }
}
