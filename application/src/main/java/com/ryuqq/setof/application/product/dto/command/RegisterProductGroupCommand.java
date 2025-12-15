package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;
import java.util.List;

/**
 * Register ProductGroup Command
 *
 * <p>상품그룹 등록 요청 데이터를 담는 불변 객체
 *
 * @param sellerId 셀러 ID
 * @param categoryId 카테고리 ID
 * @param brandId 브랜드 ID
 * @param name 상품그룹명
 * @param optionType 옵션 타입 (SINGLE, ONE_LEVEL, TWO_LEVEL)
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param shippingPolicyId 배송 정책 ID (nullable - null이면 셀러 기본)
 * @param refundPolicyId 환불 정책 ID (nullable - null이면 셀러 기본)
 * @param products 상품(SKU) 목록
 * @author development-team
 * @since 1.0.0
 */
public record RegisterProductGroupCommand(
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        String optionType,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        Long shippingPolicyId,
        Long refundPolicyId,
        List<RegisterProductCommand> products) {

    /**
     * 상품(SKU) 등록 커맨드
     *
     * @param option1Name 옵션1 명 (nullable for SINGLE)
     * @param option1Value 옵션1 값 (nullable for SINGLE)
     * @param option2Name 옵션2 명 (nullable)
     * @param option2Value 옵션2 값 (nullable)
     * @param additionalPrice 추가금액
     * @param initialStock 초기 재고 수량
     */
    public record RegisterProductCommand(
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            BigDecimal additionalPrice,
            int initialStock) {}
}
