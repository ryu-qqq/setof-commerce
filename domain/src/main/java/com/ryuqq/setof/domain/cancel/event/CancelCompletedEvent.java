package com.ryuqq.setof.domain.cancel.event;

import com.ryuqq.setof.domain.cancel.id.CancelId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderId;
import java.time.Instant;

/**
 * 취소 완료 이벤트.
 *
 * <p>취소가 완료되어 환불이 처리되었을 때 발행됩니다.
 *
 * @param cancelId 취소 ID
 * @param orderId 주문 ID
 * @param refundAmount 환불 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record CancelCompletedEvent(
        CancelId cancelId, OrderId orderId, Money refundAmount, Instant occurredAt)
        implements DomainEvent {

    public static CancelCompletedEvent of(
            CancelId cancelId, OrderId orderId, Money refundAmount, Instant occurredAt) {
        return new CancelCompletedEvent(cancelId, orderId, refundAmount, occurredAt);
    }
}
