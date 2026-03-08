package com.ryuqq.setof.application.cart.service.query;

import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.factory.CartQueryFactory;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.query.GetCartCountUseCase;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import org.springframework.stereotype.Service;

/**
 * GetCartCountService - 장바구니 개수 조회 Service.
 *
 * <p>QueryFactory로 Criteria 생성 → ReadManager로 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetCartCountService implements GetCartCountUseCase {

    private final CartReadManager readManager;
    private final CartQueryFactory queryFactory;

    public GetCartCountService(CartReadManager readManager, CartQueryFactory queryFactory) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public CartCountResult execute(CartSearchParams params) {
        CartSearchCriteria criteria = queryFactory.createCriteria(params);
        return readManager.fetchCartCount(criteria);
    }
}
