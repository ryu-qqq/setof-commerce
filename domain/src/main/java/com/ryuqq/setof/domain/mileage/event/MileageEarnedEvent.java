package com.ryuqq.setof.domain.mileage.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.mileage.id.MileageEntryId;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import com.ryuqq.setof.domain.mileage.vo.MileageIssueType;
import java.time.Instant;

/**
 * 마일리지 적립 이벤트.
 *
 * @param ledgerId 원장 ID
 * @param entryId 적립 건 ID
 * @param amount 적립 금액
 * @param issueType 적립 유형
 * @param occurredAt 이벤트 발생 시각
 */
public record MileageEarnedEvent(
        MileageLedgerId ledgerId,
        MileageEntryId entryId,
        long amount,
        MileageIssueType issueType,
        Instant occurredAt)
        implements DomainEvent {

    public static MileageEarnedEvent of(
            MileageLedgerId ledgerId,
            MileageEntryId entryId,
            long amount,
            MileageIssueType issueType,
            Instant now) {
        return new MileageEarnedEvent(ledgerId, entryId, amount, issueType, now);
    }
}
