package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 상품 ID 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidProductIdException extends DomainException {

    /**
     * 유효하지 않은 상품 ID로 예외 생성
     *
     * @param productId 유효하지 않은 상품 ID 값
     */
    public InvalidProductIdException(Long productId) {
        super(
                ProductGroupErrorCode.INVALID_PRODUCT_ID,
                String.format("유효하지 않은 상품 ID: %s", productId),
                Map.of("productId", productId != null ? productId : "null"));
    }
}
