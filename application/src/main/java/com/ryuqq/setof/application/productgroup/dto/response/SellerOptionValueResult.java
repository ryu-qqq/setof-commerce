package com.ryuqq.setof.application.productgroup.dto.response;

import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;

/** 셀러 옵션 값 조회 결과 DTO. */
public record SellerOptionValueResult(
        Long id, Long sellerOptionGroupId, String optionValueName, int sortOrder) {

    public static SellerOptionValueResult from(SellerOptionValue value) {
        return new SellerOptionValueResult(
                value.idValue(),
                value.sellerOptionGroupIdValue(),
                value.optionValueNameValue(),
                value.sortOrder());
    }
}
