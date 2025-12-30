package com.ryuqq.setof.application.product.dto.command;

/**
 * Update Product Display Command
 *
 * <p>상품(SKU) 전시 상태 변경 요청 데이터를 담는 불변 객체
 *
 * @param productId 상품 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @param displayYn 전시 여부 (true: 노출, false: 숨김)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductDisplayCommand(Long productId, Long sellerId, boolean displayYn) {}
