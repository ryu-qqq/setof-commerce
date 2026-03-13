package com.ryuqq.setof.application.product.dto.response;

/** 상품-옵션값 매핑 조회 결과 DTO. */
public record ProductOptionMappingResult(
        Long id,
        Long productId,
        Long sellerOptionValueId,
        String optionGroupName,
        String optionValueName) {

    /** Composite 쿼리에서 옵션 이름이 해석된 결과로 생성. */
    public static ProductOptionMappingResult withOptionNames(
            Long id,
            Long productId,
            Long sellerOptionValueId,
            String optionGroupName,
            String optionValueName) {
        return new ProductOptionMappingResult(
                id, productId, sellerOptionValueId, optionGroupName, optionValueName);
    }
}
