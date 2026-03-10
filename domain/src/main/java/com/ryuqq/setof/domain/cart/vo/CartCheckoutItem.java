package com.ryuqq.setof.domain.cart.vo;

/**
 * 장바구니 결제 시 상태 변경 대상 VO.
 *
 * @param cartId 장바구니 아이템 ID
 * @param userId 사용자 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartCheckoutItem(long cartId, long userId) {

    public CartCheckoutItem {
        if (cartId <= 0) {
            throw new IllegalArgumentException("cartId must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be positive");
        }
    }
}
