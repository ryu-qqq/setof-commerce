package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;

/**
 * 상품(SKU) Command DTO
 *
 * <p>상품그룹 내 개별 상품(SKU) 등록/수정 데이터
 *
 * @param option1Name 옵션1 명 (nullable for SINGLE)
 * @param option1Value 옵션1 값 (nullable for SINGLE)
 * @param option2Name 옵션2 명 (nullable)
 * @param option2Value 옵션2 값 (nullable)
 * @param additionalPrice 추가금액
 * @param initialStock 초기 재고 수량
 * @author development-team
 * @since 1.0.0
 */
public record ProductSkuCommandDto(
        String option1Name,
        String option1Value,
        String option2Name,
        String option2Value,
        BigDecimal additionalPrice,
        int initialStock) {}
