package com.ryuqq.setof.domain.productstock.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 재고 분산락 획득 실패 예외
 *
 * <p>동시 수정 방지를 위한 분산락 획득에 실패했을 때 발생합니다.
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
public class StockLockAcquisitionException extends DomainException {

    /**
     * 분산락 획득 실패 예외 생성
     *
     * @param productId 상품 ID
     */
    public StockLockAcquisitionException(Long productId) {
        super(
                ProductStockErrorCode.STOCK_LOCK_ACQUISITION_FAILED,
                String.format("재고 락 획득 실패 - 상품 ID: %s", productId),
                Map.of("productId", productId != null ? productId : "null"));
    }
}
