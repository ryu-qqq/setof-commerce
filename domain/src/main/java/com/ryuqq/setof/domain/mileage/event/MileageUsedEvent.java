package com.ryuqq.setof.domain.mileage.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import java.time.Instant;

/**
 * 마일리지 사용 이벤트.
 *
 * @param ledgerId 원장 ID
 * @param usedAmount 사용 금액
 * @param orderId 주문 ID
 * @param paymentId 결제 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record MileageUsedEvent(
        MileageLedgerId ledgerId, long usedAmount, Long orderId, Long paymentId, Instant occurredAt)
        implements DomainEvent {

    public static MileageUsedEvent of(
            MileageLedgerId ledgerId, long usedAmount, Long orderId, Long paymentId, Instant now) {
        return new MileageUsedEvent(ledgerId, usedAmount, orderId, paymentId, now);
    }
}
