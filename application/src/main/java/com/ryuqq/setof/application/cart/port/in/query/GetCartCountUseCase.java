package com.ryuqq.setof.application.cart.port.in.query;

import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;

/**
 * 장바구니 개수 조회 UseCase.
 *
 * <p>레거시 GET /api/v1/cart-count 기반. Redis 캐시 없이 DB 직접 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetCartCountUseCase {

    CartCountResult execute(CartSearchParams params);
}
