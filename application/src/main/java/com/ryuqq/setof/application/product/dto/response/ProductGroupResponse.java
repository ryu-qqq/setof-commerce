package com.ryuqq.setof.application.product.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * ProductGroup Response
 *
 * <p>상품그룹 상세 응답 DTO
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param categoryId 카테고리 ID
 * @param brandId 브랜드 ID
 * @param name 상품그룹명
 * @param optionType 옵션 타입
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param status 상태
 * @param shippingPolicyId 배송 정책 ID (nullable)
 * @param refundPolicyId 환불 정책 ID (nullable)
 * @param products 상품(SKU) 목록
 * @author development-team
 * @since 1.0.0
 */
public record ProductGroupResponse(
        Long productGroupId,
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        String optionType,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        String status,
        Long shippingPolicyId,
        Long refundPolicyId,
        List<ProductResponse> products) {

    /** Static Factory Method */
    public static ProductGroupResponse of(
            Long productGroupId,
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String optionType,
            BigDecimal regularPrice,
            BigDecimal currentPrice,
            String status,
            Long shippingPolicyId,
            Long refundPolicyId,
            List<ProductResponse> products) {
        return new ProductGroupResponse(
                productGroupId,
                sellerId,
                categoryId,
                brandId,
                name,
                optionType,
                regularPrice,
                currentPrice,
                status,
                shippingPolicyId,
                refundPolicyId,
                products);
    }
}
