package com.ryuqq.setof.migration.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 레거시 PRODUCT + PRODUCT_OPTION + OPTION_GROUP + OPTION_DETAIL 조인 조회 DTO
 *
 * <p>레거시 DB에서 개별 상품(SKU)과 옵션 정보를 조인하여 조회합니다.
 *
 * @param productId 레거시 상품 ID
 * @param productGroupId 레거시 상품그룹 ID
 * @param soldOutYn 품절 여부 (Y/N)
 * @param displayYn 전시 여부 (Y/N)
 * @param additionalPrice 추가 금액
 * @param option1Name 옵션1 이름 (예: SIZE, COLOR)
 * @param option1Value 옵션1 값 (예: L, RED)
 * @param option2Name 옵션2 이름 (nullable)
 * @param option2Value 옵션2 값 (nullable)
 * @param stockQuantity 재고 수량
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 * @author development-team
 * @since 1.0.0
 */
public record LegacyProductDto(
        Long productId,
        Long productGroupId,
        String soldOutYn,
        String displayYn,
        BigDecimal additionalPrice,
        String option1Name,
        String option1Value,
        String option2Name,
        String option2Value,
        Integer stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
