package com.ryuqq.setof.domain.mileage.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import java.time.Instant;

/**
 * 마일리지 만료 이벤트. 스케줄러에 의한 일괄 만료 처리 시 발행됩니다.
 *
 * @param ledgerId 원장 ID
 * @param expiredAmount 만료된 총 금액
 * @param expiredEntryCount 만료된 Entry 수
 * @param occurredAt 이벤트 발생 시각
 */
public record MileageExpiredEvent(
        MileageLedgerId ledgerId, long expiredAmount, int expiredEntryCount, Instant occurredAt)
        implements DomainEvent {

    public static MileageExpiredEvent of(
            MileageLedgerId ledgerId, long expiredAmount, int expiredEntryCount, Instant now) {
        return new MileageExpiredEvent(ledgerId, expiredAmount, expiredEntryCount, now);
    }
}
