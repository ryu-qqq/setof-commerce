package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 재고 동시 수정 충돌 예외
 *
 * <p>낙관적 락 실패 후 재시도 횟수를 초과했을 때 발생합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>DomainException 상속 필수
 *   <li>Lombok 금지
 *   <li>Spring 의존성 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class StockConcurrentModificationException extends DomainException {

    /**
     * 재고 동시 수정 예외 생성
     *
     * @param productId 상품 ID
     * @param retryCount 시도 횟수
     */
    public StockConcurrentModificationException(Long productId, int retryCount) {
        super(
                ProductStockErrorCode.STOCK_CONCURRENT_MODIFICATION,
                String.format("재고 동시 수정 충돌 - 상품 ID: %s, 재시도 횟수: %d회 초과", productId, retryCount),
                Map.of(
                        "productId",
                        productId != null ? productId : "null",
                        "retryCount",
                        retryCount));
    }
}
