package com.ryuqq.setof.application.cart.port.out.query;

import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import java.util.List;

/**
 * CartQueryPort - 장바구니 조회 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다. Domain SearchCriteria를 받습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CartQueryPort {

    List<CartItemResult> fetchCarts(CartSearchCriteria criteria);

    long countCarts(CartSearchCriteria criteria);

    CartCountResult fetchCartCount(CartSearchCriteria criteria);
}
