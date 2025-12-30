package com.ryuqq.setof.application.product.dto.command;

/**
 * Mark Product Out Of Stock Command
 *
 * <p>상품(SKU) 품절 상태 변경 요청 데이터를 담는 불변 객체
 *
 * @param productId 상품 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @param soldOut 품절 여부 (true: 품절, false: 재고 있음)
 * @author development-team
 * @since 1.0.0
 */
public record MarkProductOutOfStockCommand(Long productId, Long sellerId, boolean soldOut) {}
