package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 재고 낙관적 락 충돌 예외
 *
 * <p>동시 업데이트 충돌 발생 시 Persistence Adapter에서 던지는 예외입니다.
 *
 * <p>Application Layer에서 이 예외를 캐치하여 재시도합니다.
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
public class StockOptimisticLockException extends DomainException {

    private final Long productId;

    /**
     * 낙관적 락 충돌 예외 생성
     *
     * @param productId 상품 ID
     */
    public StockOptimisticLockException(Long productId) {
        super(
                ProductStockErrorCode.STOCK_OPTIMISTIC_LOCK,
                String.format("재고 업데이트 충돌 발생 - 상품 ID: %s", productId),
                Map.of("productId", productId != null ? productId : "null"));
        this.productId = productId;
    }

    /**
     * 충돌이 발생한 상품 ID 반환
     *
     * @return 상품 ID
     */
    public Long getProductId() {
        return productId;
    }
}
