package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * CartNotFoundException - 장바구니 미존재 예외
 *
 * <p>요청한 장바구니가 존재하지 않는 경우 발생합니다.
 */
public class CartNotFoundException extends DomainException {

    public CartNotFoundException(CartId cartId) {
        super(
                CartErrorCode.CART_NOT_FOUND,
                String.format("장바구니를 찾을 수 없습니다: %d", cartId.value()),
                Map.of("cartId", cartId.value()));
    }

    public CartNotFoundException(Long memberId) {
        super(
                CartErrorCode.CART_NOT_FOUND,
                String.format("회원의 장바구니를 찾을 수 없습니다: memberId=%d", memberId),
                Map.of("memberId", memberId));
    }
}
