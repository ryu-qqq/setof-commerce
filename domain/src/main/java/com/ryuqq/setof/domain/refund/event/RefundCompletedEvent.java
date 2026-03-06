package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.refund.id.RefundId;
import java.time.Instant;

/**
 * 반품 완료 이벤트.
 *
 * <p>환불 처리가 완료되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param orderId 주문 ID
 * @param finalRefundAmount 최종 환불 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundCompletedEvent(
        RefundId refundId, OrderId orderId, Money finalRefundAmount, Instant occurredAt)
        implements DomainEvent {

    public static RefundCompletedEvent of(
            RefundId refundId, OrderId orderId, Money finalRefundAmount, Instant now) {
        return new RefundCompletedEvent(refundId, orderId, finalRefundAmount, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public int finalRefundAmountValue() {
        return finalRefundAmount.value();
    }
}
