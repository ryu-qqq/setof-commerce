package com.ryuqq.setof.domain.cancel.aggregate;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderItemId;

/**
 * 취소 아이템 Value Object.
 *
 * <p>취소에 포함되는 주문 아이템 정보를 나타냅니다.
 *
 * @param orderItemId 주문 아이템 ID (필수)
 * @param itemAmount 아이템 금액 (필수)
 * @param quantity 취소 수량 (1 이상)
 */
public record CancelItem(OrderItemId orderItemId, Money itemAmount, int quantity) {

    public CancelItem {
        if (orderItemId == null) {
            throw new IllegalArgumentException("주문 아이템 ID는 필수입니다");
        }
        if (itemAmount == null) {
            throw new IllegalArgumentException("아이템 금액은 필수입니다");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("취소 수량은 1 이상이어야 합니다");
        }
    }

    public static CancelItem of(OrderItemId orderItemId, Money itemAmount, int quantity) {
        return new CancelItem(orderItemId, itemAmount, quantity);
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public int itemAmountValue() {
        return itemAmount.value();
    }
}
