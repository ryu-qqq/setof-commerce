package com.ryuqq.setof.adapter.out.persistence.mileage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * MileageTransactionJpaEntity - 마일리지 거래 이력 JPA 엔티티.
 *
 * <p>불변 이벤트 로그로 updated_at이 없습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "mileage_transactions")
public class MileageTransactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mileage_entry_id", nullable = false)
    private Long mileageEntryId;

    @Column(name = "change_amount", nullable = false)
    private long changeAmount;

    @Column(name = "reason", nullable = false, length = 30)
    private String reason;

    @Column(name = "related_order_id")
    private Long relatedOrderId;

    @Column(name = "related_payment_id")
    private Long relatedPaymentId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected MileageTransactionJpaEntity() {}

    private MileageTransactionJpaEntity(
            Long id,
            Long mileageEntryId,
            long changeAmount,
            String reason,
            Long relatedOrderId,
            Long relatedPaymentId,
            String description,
            Instant createdAt) {
        this.id = id;
        this.mileageEntryId = mileageEntryId;
        this.changeAmount = changeAmount;
        this.reason = reason;
        this.relatedOrderId = relatedOrderId;
        this.relatedPaymentId = relatedPaymentId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static MileageTransactionJpaEntity create(
            Long id,
            Long mileageEntryId,
            long changeAmount,
            String reason,
            Long relatedOrderId,
            Long relatedPaymentId,
            String description,
            Instant createdAt) {
        return new MileageTransactionJpaEntity(
                id,
                mileageEntryId,
                changeAmount,
                reason,
                relatedOrderId,
                relatedPaymentId,
                description,
                createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMileageEntryId() {
        return mileageEntryId;
    }

    public long getChangeAmount() {
        return changeAmount;
    }

    public String getReason() {
        return reason;
    }

    public Long getRelatedOrderId() {
        return relatedOrderId;
    }

    public Long getRelatedPaymentId() {
        return relatedPaymentId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
