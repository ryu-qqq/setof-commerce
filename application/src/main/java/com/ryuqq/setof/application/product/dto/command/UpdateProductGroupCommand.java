package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;

/**
 * Update ProductGroup Command
 *
 * <p>상품그룹 수정 요청 데이터를 담는 불변 객체
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @param categoryId 카테고리 ID (nullable - null이면 변경 안 함)
 * @param brandId 브랜드 ID (nullable - null이면 변경 안 함)
 * @param name 상품그룹명 (nullable - null이면 변경 안 함)
 * @param regularPrice 정가 (nullable - null이면 변경 안 함)
 * @param currentPrice 판매가 (nullable - null이면 변경 안 함)
 * @param shippingPolicyId 배송 정책 ID (nullable)
 * @param refundPolicyId 환불 정책 ID (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductGroupCommand(
        Long productGroupId,
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        Long shippingPolicyId,
        Long refundPolicyId) {}
