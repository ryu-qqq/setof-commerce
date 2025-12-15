package com.ryuqq.setof.application.product.dto.response;

import java.math.BigDecimal;

/**
 * Product (SKU) Response
 *
 * <p>상품(SKU) 응답 DTO
 *
 * @param productId 상품 ID
 * @param productGroupId 상품그룹 ID
 * @param optionType 옵션 타입
 * @param option1Name 옵션1 명 (nullable)
 * @param option1Value 옵션1 값 (nullable)
 * @param option2Name 옵션2 명 (nullable)
 * @param option2Value 옵션2 값 (nullable)
 * @param additionalPrice 추가금액
 * @param soldOut 품절 여부
 * @param displayYn 노출 여부
 * @author development-team
 * @since 1.0.0
 */
public record ProductResponse(
        Long productId,
        Long productGroupId,
        String optionType,
        String option1Name,
        String option1Value,
        String option2Name,
        String option2Value,
        BigDecimal additionalPrice,
        boolean soldOut,
        boolean displayYn) {

    /** Static Factory Method */
    public static ProductResponse of(
            Long productId,
            Long productGroupId,
            String optionType,
            String option1Name,
            String option1Value,
            String option2Name,
            String option2Value,
            BigDecimal additionalPrice,
            boolean soldOut,
            boolean displayYn) {
        return new ProductResponse(
                productId,
                productGroupId,
                optionType,
                option1Name,
                option1Value,
                option2Name,
                option2Value,
                additionalPrice,
                soldOut,
                displayYn);
    }
}
