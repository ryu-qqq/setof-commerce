package com.ryuqq.setof.application.productgroup.dto.response;

import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import java.util.List;

/** 셀러 옵션 그룹 조회 결과 DTO. */
public record SellerOptionGroupResult(
        Long id,
        String optionGroupName,
        int sortOrder,
        List<SellerOptionValueResult> optionValues) {

    public static SellerOptionGroupResult from(SellerOptionGroup group) {
        List<SellerOptionValueResult> values =
                group.optionValues().stream().map(SellerOptionValueResult::from).toList();

        return new SellerOptionGroupResult(
                group.idValue(), group.optionGroupNameValue(), group.sortOrder(), values);
    }
}
