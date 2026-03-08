package com.ryuqq.setof.application.cart.assembler;

import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CartAssembler - 장바구니 Result DTO 조립.
 *
 * <p>Manager에서 조회한 데이터를 조합하여 최종 응답 객체를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartAssembler {

    public CartSliceResult toSliceResult(
            List<CartItemResult> items, CartSearchCriteria criteria, long totalElements) {
        boolean hasNext = items.size() > criteria.size();
        List<CartItemResult> content = hasNext ? items.subList(0, criteria.size()) : items;

        Long lastId = content.isEmpty() ? null : content.get(content.size() - 1).cartId();
        SliceMeta sliceMeta =
                SliceMeta.withCursor(lastId, criteria.size(), hasNext, content.size());

        return CartSliceResult.of(content, sliceMeta, totalElements);
    }
}
