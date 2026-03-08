package com.ryuqq.setof.application.cart.port.in.query;

import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;

/**
 * 장바구니 목록 조회 UseCase.
 *
 * <p>레거시 GET /api/v1/carts 기반. 커서 기반 페이지네이션으로 장바구니 목록 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetCartsUseCase {

    CartSliceResult execute(CartSearchParams params);
}
