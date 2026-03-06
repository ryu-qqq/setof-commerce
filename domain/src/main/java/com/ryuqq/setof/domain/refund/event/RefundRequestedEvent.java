package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.refund.id.RefundId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;

/**
 * 반품 요청 이벤트.
 *
 * <p>반품이 요청되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param orderId 주문 ID
 * @param sellerId 셀러 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundRequestedEvent(
        RefundId refundId, OrderId orderId, SellerId sellerId, Instant occurredAt)
        implements DomainEvent {

    public static RefundRequestedEvent of(
            RefundId refundId, OrderId orderId, SellerId sellerId, Instant now) {
        return new RefundRequestedEvent(refundId, orderId, sellerId, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }
}
