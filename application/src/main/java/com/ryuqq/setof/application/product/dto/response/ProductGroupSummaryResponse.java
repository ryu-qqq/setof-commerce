package com.ryuqq.setof.application.product.dto.response;

import java.math.BigDecimal;

/**
 * ProductGroup Summary Response
 *
 * <p>상품그룹 요약 응답 DTO (목록 조회용)
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param name 상품그룹명
 * @param optionType 옵션 타입
 * @param currentPrice 판매가
 * @param status 상태
 * @param productCount 상품(SKU) 개수
 * @author development-team
 * @since 1.0.0
 */
public record ProductGroupSummaryResponse(
        Long productGroupId,
        Long sellerId,
        String name,
        String optionType,
        BigDecimal currentPrice,
        String status,
        int productCount) {

    /** Static Factory Method */
    public static ProductGroupSummaryResponse of(
            Long productGroupId,
            Long sellerId,
            String name,
            String optionType,
            BigDecimal currentPrice,
            String status,
            int productCount) {
        return new ProductGroupSummaryResponse(
                productGroupId, sellerId, name, optionType, currentPrice, status, productCount);
    }
}
