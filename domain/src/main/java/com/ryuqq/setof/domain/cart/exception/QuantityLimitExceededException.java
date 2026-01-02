package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * QuantityLimitExceededException - 상품 최대 수량 초과 예외
 *
 * <p>단일 상품의 최대 수량을 초과한 경우 발생합니다.
 */
public class QuantityLimitExceededException extends DomainException {

    public QuantityLimitExceededException(Long productStockId, int requestedQuantity, int maxQuantity) {
        super(
                CartErrorCode.QUANTITY_LIMIT_EXCEEDED,
                String.format(
                        "상품 최대 수량(%d)을 초과했습니다. 요청: %d",
                        maxQuantity, requestedQuantity),
                Map.of(
                        "productStockId", productStockId,
                        "requestedQuantity", requestedQuantity,
                        "maxQuantity", maxQuantity));
    }
}
