package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;

/**
 * Update Product Option Command
 *
 * <p>상품(SKU) 옵션 수정 요청 데이터를 담는 불변 객체
 *
 * @param productId 상품 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @param option1Name 옵션1 이름
 * @param option1Value 옵션1 값
 * @param option2Name 옵션2 이름 (nullable)
 * @param option2Value 옵션2 값 (nullable)
 * @param additionalPrice 추가 금액
 * @param quantity 재고 수량 (nullable - null이면 재고 변경 안함)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductOptionCommand(
        Long productId,
        Long sellerId,
        String option1Name,
        String option1Value,
        String option2Name,
        String option2Value,
        BigDecimal additionalPrice,
        Integer quantity) {}
