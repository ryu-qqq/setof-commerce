package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import java.util.Map;

/**
 * 재고 정보를 찾을 수 없음 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class ProductStockNotFoundException extends DomainException {

    /**
     * ProductStockId로 예외 생성
     *
     * @param productStockId 찾을 수 없는 재고 ID
     */
    public ProductStockNotFoundException(ProductStockId productStockId) {
        super(
                ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND,
                String.format(
                        "재고 정보를 찾을 수 없습니다. ID: %s",
                        productStockId != null ? productStockId.value() : "null"),
                Map.of(
                        "productStockId",
                        productStockId != null && productStockId.value() != null
                                ? productStockId.value()
                                : "null"));
    }

    /**
     * ProductId로 예외 생성
     *
     * @param productId 재고를 찾을 수 없는 상품 ID
     */
    public ProductStockNotFoundException(ProductId productId) {
        super(
                ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND,
                String.format(
                        "상품의 재고 정보를 찾을 수 없습니다. 상품 ID: %s",
                        productId != null ? productId.value() : "null"),
                Map.of(
                        "productId",
                        productId != null && productId.value() != null
                                ? productId.value()
                                : "null"));
    }

    /**
     * Long productId로 예외 생성
     *
     * @param productId 재고를 찾을 수 없는 상품 ID (Long)
     */
    public ProductStockNotFoundException(Long productId) {
        super(
                ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND,
                String.format("상품의 재고 정보를 찾을 수 없습니다. 상품 ID: %s", productId),
                Map.of("productId", productId != null ? productId : "null"));
    }
}
