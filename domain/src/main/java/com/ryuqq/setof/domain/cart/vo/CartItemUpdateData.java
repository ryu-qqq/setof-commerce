package com.ryuqq.setof.domain.cart.vo;

import java.time.Instant;

/**
 * 장바구니 아이템 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param quantity 새 수량
 * @param occurredAt 변경 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartItemUpdateData(CartQuantity quantity, Instant occurredAt) {

    public static CartItemUpdateData of(CartQuantity quantity, Instant occurredAt) {
        return new CartItemUpdateData(quantity, occurredAt);
    }
}
