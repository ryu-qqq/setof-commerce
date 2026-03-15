package com.ryuqq.setof.adapter.out.persistence.mileage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * MileageEntryJpaEntity - 마일리지 항목 JPA 엔티티.
 *
 * <p>개별 마일리지 적립/차감 배치를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "mileage_entries")
public class MileageEntryJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mileage_ledger_id", nullable = false)
    private Long mileageLedgerId;

    @Column(name = "earned_amount", nullable = false)
    private long earnedAmount;

    @Column(name = "used_amount", nullable = false)
    private long usedAmount;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "issue_type", nullable = false, length = 30)
    private String issueType;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    protected MileageEntryJpaEntity() {
        super();
    }

    private MileageEntryJpaEntity(
            Long id,
            Long mileageLedgerId,
            long earnedAmount,
            long usedAmount,
            String status,
            String issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.mileageLedgerId = mileageLedgerId;
        this.earnedAmount = earnedAmount;
        this.usedAmount = usedAmount;
        this.status = status;
        this.issueType = issueType;
        this.title = title;
        this.targetId = targetId;
        this.issuedAt = issuedAt;
        this.expirationDate = expirationDate;
    }

    public static MileageEntryJpaEntity create(
            Long id,
            Long mileageLedgerId,
            long earnedAmount,
            long usedAmount,
            String status,
            String issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate,
            Instant createdAt,
            Instant updatedAt) {
        return new MileageEntryJpaEntity(
                id,
                mileageLedgerId,
                earnedAmount,
                usedAmount,
                status,
                issueType,
                title,
                targetId,
                issuedAt,
                expirationDate,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMileageLedgerId() {
        return mileageLedgerId;
    }

    public long getEarnedAmount() {
        return earnedAmount;
    }

    public long getUsedAmount() {
        return usedAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getTitle() {
        return title;
    }

    public Long getTargetId() {
        return targetId;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
