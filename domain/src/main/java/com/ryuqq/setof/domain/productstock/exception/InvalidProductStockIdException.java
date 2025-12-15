package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 재고 ID 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class InvalidProductStockIdException extends DomainException {

    /**
     * 유효하지 않은 재고 ID로 예외 생성
     *
     * @param productStockId 유효하지 않은 재고 ID 값
     */
    public InvalidProductStockIdException(Long productStockId) {
        super(
                ProductStockErrorCode.INVALID_PRODUCT_STOCK_ID,
                String.format("유효하지 않은 재고 ID: %s", productStockId),
                Map.of("productStockId", productStockId != null ? productStockId : "null"));
    }
}
