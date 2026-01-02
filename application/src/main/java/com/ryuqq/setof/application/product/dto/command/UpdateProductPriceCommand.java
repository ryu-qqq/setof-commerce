package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;

/**
 * 상품 가격 수정 Command
 *
 * <p>상품 그룹의 가격만 수정하는 경량 Command
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID (권한 검증용, nullable)
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductPriceCommand(
        Long productGroupId, Long sellerId, BigDecimal regularPrice, BigDecimal currentPrice) {}
