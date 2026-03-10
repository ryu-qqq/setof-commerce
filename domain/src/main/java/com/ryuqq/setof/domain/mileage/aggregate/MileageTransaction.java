package com.ryuqq.setof.domain.mileage.aggregate;

import com.ryuqq.setof.domain.mileage.id.MileageEntryId;
import com.ryuqq.setof.domain.mileage.id.MileageTransactionId;
import com.ryuqq.setof.domain.mileage.vo.MileageReason;
import java.time.Instant;

/**
 * 마일리지 변동 이력. 불변 이벤트 로그입니다.
 *
 * <p>모든 마일리지 변동(적립, 사용, 환불, 만료, 회수)을 기록합니다.
 *
 * <p>레거시 mileage_history 테이블에 대응합니다.
 */
public class MileageTransaction {

    private final MileageTransactionId id;
    private final MileageEntryId entryId;
    private final long changeAmount;
    private final MileageReason reason;
    private final Long relatedOrderId;
    private final Long relatedPaymentId;
    private final String description;
    private final Instant createdAt;

    private MileageTransaction(
            MileageTransactionId id,
            MileageEntryId entryId,
            long changeAmount,
            MileageReason reason,
            Long relatedOrderId,
            Long relatedPaymentId,
            String description,
            Instant createdAt) {
        this.id = id;
        this.entryId = entryId;
        this.changeAmount = changeAmount;
        this.reason = reason;
        this.relatedOrderId = relatedOrderId;
        this.relatedPaymentId = relatedPaymentId;
        this.description = description;
        this.createdAt = createdAt;
    }

    /** 새 이력 생성. */
    public static MileageTransaction create(
            MileageEntryId entryId,
            long changeAmount,
            MileageReason reason,
            Long relatedOrderId,
            Long relatedPaymentId,
            String description,
            Instant now) {
        return new MileageTransaction(
                MileageTransactionId.forNew(),
                entryId,
                changeAmount,
                reason,
                relatedOrderId,
                relatedPaymentId,
                description,
                now);
    }

    /** DB 복원용 팩토리 메서드. */
    public static MileageTransaction reconstitute(
            MileageTransactionId id,
            MileageEntryId entryId,
            long changeAmount,
            MileageReason reason,
            Long relatedOrderId,
            Long relatedPaymentId,
            String description,
            Instant createdAt) {
        return new MileageTransaction(
                id,
                entryId,
                changeAmount,
                reason,
                relatedOrderId,
                relatedPaymentId,
                description,
                createdAt);
    }

    // Getters
    public MileageTransactionId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MileageEntryId entryId() {
        return entryId;
    }

    public long changeAmount() {
        return changeAmount;
    }

    public MileageReason reason() {
        return reason;
    }

    public Long relatedOrderId() {
        return relatedOrderId;
    }

    public Long relatedPaymentId() {
        return relatedPaymentId;
    }

    public String description() {
        return description;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
