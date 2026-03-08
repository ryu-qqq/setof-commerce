package com.ryuqq.setof.application.cart.factory;

import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.springframework.stereotype.Component;

/**
 * CartQueryFactory - 장바구니 조회 Criteria 생성 Factory.
 *
 * <p>CartSearchParams(Application DTO) → CartSearchCriteria(Domain) 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public CartQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public CartSearchCriteria createCriteria(CartSearchParams params) {
        CursorPageRequest<Long> cursorPageRequest =
                commonVoFactory.createCursorPageRequestAfterCursor(
                        params.lastCartId(), params.pageSize());

        CursorQueryContext<CartSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        CartSortKey.defaultKey(), SortDirection.DESC, cursorPageRequest);

        return CartSearchCriteria.of(params.memberId(), params.userId(), queryContext);
    }
}
