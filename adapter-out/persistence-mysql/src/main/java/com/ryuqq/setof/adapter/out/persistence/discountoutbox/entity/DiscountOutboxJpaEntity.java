package com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * DiscountOutboxJpaEntity - 할인 아웃박스 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지.
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "discount_outbox")
public class DiscountOutboxJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_type", nullable = false, length = 30)
    private String targetType;

    @Column(name = "target_id", nullable = false)
    private long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "fail_reason", length = 500)
    private String failReason;

    protected DiscountOutboxJpaEntity() {
        super();
    }

    private DiscountOutboxJpaEntity(
            Long id,
            String targetType,
            long targetId,
            Status status,
            int retryCount,
            String payload,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.status = status;
        this.retryCount = retryCount;
        this.payload = payload;
        this.failReason = failReason;
    }

    public static DiscountOutboxJpaEntity create(
            Long id,
            String targetType,
            long targetId,
            Status status,
            int retryCount,
            String payload,
            String failReason,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountOutboxJpaEntity(
                id,
                targetType,
                targetId,
                status,
                retryCount,
                payload,
                failReason,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getTargetType() {
        return targetType;
    }

    public long getTargetId() {
        return targetId;
    }

    public Status getStatus() {
        return status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getPayload() {
        return payload;
    }

    public String getFailReason() {
        return failReason;
    }

    /** Entity 내부 Status enum (domain OutboxStatus와 1:1 매핑) */
    public enum Status {
        PENDING,
        PUBLISHED,
        COMPLETED,
        FAILED
    }
}
