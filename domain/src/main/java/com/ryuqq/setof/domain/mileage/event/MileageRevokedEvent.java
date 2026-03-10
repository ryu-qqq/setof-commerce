package com.ryuqq.setof.domain.mileage.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import java.time.Instant;

/**
 * 마일리지 회수 이벤트. 취소된 주문의 적립분 회수 시 발행됩니다.
 *
 * @param ledgerId 원장 ID
 * @param revokedAmount 회수된 금액
 * @param orderId 취소된 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record MileageRevokedEvent(
        MileageLedgerId ledgerId, long revokedAmount, Long orderId, Instant occurredAt)
        implements DomainEvent {

    public static MileageRevokedEvent of(
            MileageLedgerId ledgerId, long revokedAmount, Long orderId, Instant now) {
        return new MileageRevokedEvent(ledgerId, revokedAmount, orderId, now);
    }
}
