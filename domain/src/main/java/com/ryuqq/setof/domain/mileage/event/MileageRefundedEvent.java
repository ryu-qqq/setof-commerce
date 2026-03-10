package com.ryuqq.setof.domain.mileage.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.mileage.id.MileageEntryId;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import java.time.Instant;

/**
 * 마일리지 환불 이벤트. 전체 취소 시 사용 마일리지 반환 시 발행됩니다.
 *
 * @param ledgerId 원장 ID
 * @param refundEntryId 환불로 생성된 Entry ID
 * @param refundAmount 환불 금액
 * @param orderId 관련 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record MileageRefundedEvent(
        MileageLedgerId ledgerId,
        MileageEntryId refundEntryId,
        long refundAmount,
        Long orderId,
        Instant occurredAt)
        implements DomainEvent {

    public static MileageRefundedEvent of(
            MileageLedgerId ledgerId,
            MileageEntryId refundEntryId,
            long refundAmount,
            Long orderId,
            Instant now) {
        return new MileageRefundedEvent(ledgerId, refundEntryId, refundAmount, orderId, now);
    }
}
