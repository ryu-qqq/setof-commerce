package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.Map;

/**
 * 상품을 찾을 수 없음 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class ProductNotFoundException extends DomainException {

    /**
     * 상품 ID로 예외 생성
     *
     * @param productId 찾을 수 없는 상품 ID
     */
    public ProductNotFoundException(ProductId productId) {
        super(
                ProductGroupErrorCode.PRODUCT_NOT_FOUND,
                String.format(
                        "상품을 찾을 수 없습니다. ID: %s", productId != null ? productId.value() : "null"),
                Map.of(
                        "productId",
                        productId != null && productId.value() != null
                                ? productId.value()
                                : "null"));
    }

    /**
     * Long ID로 예외 생성
     *
     * @param productId 찾을 수 없는 상품 ID (Long)
     */
    public ProductNotFoundException(Long productId) {
        super(
                ProductGroupErrorCode.PRODUCT_NOT_FOUND,
                String.format("상품을 찾을 수 없습니다. ID: %s", productId),
                Map.of("productId", productId != null ? productId : "null"));
    }
}
