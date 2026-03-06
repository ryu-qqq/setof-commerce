package com.ryuqq.setof.domain.refund.aggregate;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderItemId;

/**
 * 반품 아이템 Value Object.
 *
 * <p>반품 대상 주문 아이템의 정보를 나타냅니다.
 *
 * @param orderItemId 주문 아이템 ID (필수)
 * @param itemAmount 아이템 금액 (필수)
 * @param quantity 반품 수량 (1 이상)
 */
public record RefundItem(OrderItemId orderItemId, Money itemAmount, int quantity) {

    public RefundItem {
        if (orderItemId == null) {
            throw new IllegalArgumentException("주문 아이템 ID는 필수입니다");
        }
        if (itemAmount == null) {
            throw new IllegalArgumentException("아이템 금액은 필수입니다");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("반품 수량은 1 이상이어야 합니다");
        }
    }

    /**
     * 반품 아이템 생성.
     *
     * @param orderItemId 주문 아이템 ID
     * @param itemAmount 아이템 금액
     * @param quantity 반품 수량
     * @return RefundItem 인스턴스
     */
    public static RefundItem of(OrderItemId orderItemId, Money itemAmount, int quantity) {
        return new RefundItem(orderItemId, itemAmount, quantity);
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public int itemAmountValue() {
        return itemAmount.value();
    }
}
