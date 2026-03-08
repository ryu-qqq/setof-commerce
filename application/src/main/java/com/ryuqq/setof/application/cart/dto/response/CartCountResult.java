package com.ryuqq.setof.application.cart.dto.response;

/**
 * 장바구니 개수 결과 DTO.
 *
 * @param cartQuantity 장바구니 아이템 개수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartCountResult(long cartQuantity) {

    public static CartCountResult of(long cartQuantity) {
        return new CartCountResult(cartQuantity);
    }
}
