package com.ryuqq.setof.domain.settlement.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.settlement.id.SettlementId;
import java.time.Instant;

/**
 * 정산 완료 이벤트.
 *
 * <p>정산이 완료되었을 때 발행됩니다.
 *
 * @param settlementId 정산 ID
 * @param orderItemId 주문 아이템 ID
 * @param sellerId 셀러 ID
 * @param settlementAmount 정산 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record SettlementCompletedEvent(
        SettlementId settlementId,
        OrderItemId orderItemId,
        SellerId sellerId,
        Money settlementAmount,
        Instant occurredAt)
        implements DomainEvent {

    public static SettlementCompletedEvent of(
            SettlementId settlementId,
            OrderItemId orderItemId,
            SellerId sellerId,
            Money settlementAmount,
            Instant now) {
        return new SettlementCompletedEvent(
                settlementId, orderItemId, sellerId, settlementAmount, now);
    }

    public Long settlementIdValue() {
        return settlementId.value();
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }

    public int settlementAmountValue() {
        return settlementAmount.value();
    }
}
