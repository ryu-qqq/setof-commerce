package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 재고 오버플로우 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class StockOverflowException extends DomainException {

    /**
     * 재고 오버플로우 예외 생성
     *
     * @param productId 상품 ID
     * @param currentStock 현재 재고
     * @param restoreQuantity 복원하려는 수량
     * @param reason 사유
     */
    public StockOverflowException(
            Long productId, int currentStock, int restoreQuantity, String reason) {
        super(
                ProductStockErrorCode.STOCK_OVERFLOW,
                String.format(
                        "재고 오버플로우 - 상품 ID: %s, 현재 재고: %d, 복원 수량: %d, 사유: %s",
                        productId, currentStock, restoreQuantity, reason),
                Map.of(
                        "productId", productId != null ? productId : "null",
                        "currentStock", currentStock,
                        "restoreQuantity", restoreQuantity,
                        "reason", reason));
    }
}
