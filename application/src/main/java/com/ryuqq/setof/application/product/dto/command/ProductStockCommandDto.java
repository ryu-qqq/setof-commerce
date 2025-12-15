package com.ryuqq.setof.application.product.dto.command;

/**
 * 상품재고 Command DTO
 *
 * <p>상품(SKU)별 재고 설정 데이터
 *
 * @param skuIndex SKU 인덱스 (등록 시 순서 매핑용)
 * @param quantity 재고 수량
 * @author development-team
 * @since 1.0.0
 */
public record ProductStockCommandDto(int skuIndex, int quantity) {}
