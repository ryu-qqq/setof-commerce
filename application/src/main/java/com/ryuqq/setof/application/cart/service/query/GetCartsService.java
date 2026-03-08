package com.ryuqq.setof.application.cart.service.query;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.application.cart.factory.CartQueryFactory;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.query.GetCartsUseCase;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetCartsService - 장바구니 목록 조회 Service.
 *
 * <p>QueryFactory로 Criteria 생성 → ReadManager로 조회 → Assembler로 결과 조립.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetCartsService implements GetCartsUseCase {

    private final CartReadManager readManager;
    private final CartQueryFactory queryFactory;
    private final CartAssembler assembler;

    public GetCartsService(
            CartReadManager readManager, CartQueryFactory queryFactory, CartAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public CartSliceResult execute(CartSearchParams params) {
        CartSearchCriteria criteria = queryFactory.createCriteria(params);
        List<CartItemResult> items = readManager.fetchCarts(criteria);
        long totalElements = readManager.countCarts(criteria);
        return assembler.toSliceResult(items, criteria, totalElements);
    }
}
