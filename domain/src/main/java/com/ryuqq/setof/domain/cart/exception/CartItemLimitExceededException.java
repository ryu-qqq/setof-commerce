package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * CartItemLimitExceededException - 장바구니 최대 개수 초과 예외
 *
 * <p>장바구니에 담을 수 있는 최대 아이템 개수를 초과한 경우 발생합니다.
 */
public class CartItemLimitExceededException extends DomainException {

    public CartItemLimitExceededException(int currentCount, int maxLimit) {
        super(
                CartErrorCode.CART_ITEM_LIMIT_EXCEEDED,
                String.format("장바구니 최대 개수(%d)를 초과했습니다. 현재: %d", maxLimit, currentCount),
                Map.of("currentCount", currentCount, "maxLimit", maxLimit));
    }
}
