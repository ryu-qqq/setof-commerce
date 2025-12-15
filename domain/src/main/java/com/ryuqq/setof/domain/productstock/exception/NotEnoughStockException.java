package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 재고 부족 예외
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 */
public class NotEnoughStockException extends DomainException {

    /**
     * 재고 부족 예외 생성
     *
     * @param productId 상품 ID
     * @param currentStock 현재 재고
     * @param requestedQuantity 요청 수량
     * @param reason 사유
     */
    public NotEnoughStockException(
            Long productId, int currentStock, int requestedQuantity, String reason) {
        super(
                ProductStockErrorCode.NOT_ENOUGH_STOCK,
                String.format(
                        "재고 부족 - 상품 ID: %s, 현재 재고: %d, 요청 수량: %d, 사유: %s",
                        productId, currentStock, requestedQuantity, reason),
                Map.of(
                        "productId", productId != null ? productId : "null",
                        "currentStock", currentStock,
                        "requestedQuantity", requestedQuantity,
                        "reason", reason));
    }
}
