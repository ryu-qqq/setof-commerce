package com.ryuqq.setof.domain.exchange.vo;

import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;

/**
 * 교환 대상 상품 정보 Value Object.
 *
 * <p>교환 요청 시 교환 대상이 되는 상품 정보를 나타냅니다.
 *
 * @param productGroupId 상품 그룹 ID (필수)
 * @param productId 상품 ID (필수)
 * @param skuCode SKU 코드
 * @param quantity 교환 수량 (1 이상)
 */
public record ExchangeTarget(
        ProductGroupId productGroupId, ProductId productId, String skuCode, int quantity) {

    public ExchangeTarget {
        if (productGroupId == null) {
            throw new IllegalArgumentException("상품 그룹 ID는 필수입니다");
        }
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("교환 수량은 1 이상이어야 합니다");
        }
    }

    public static ExchangeTarget of(
            ProductGroupId productGroupId, ProductId productId, String skuCode, int quantity) {
        return new ExchangeTarget(productGroupId, productId, skuCode, quantity);
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public Long productIdValue() {
        return productId.value();
    }
}
