package com.ryuqq.setof.storage.legacy.mileage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyMileageHistoryEntity - 레거시 마일리지 이력 엔티티.
 *
 * <p>레거시 DB의 mileage_history 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "mileage_history")
public class LegacyMileageHistoryEntity {

    @Id
    @Column(name = "mileage_history_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "mileage_id")
    private long mileageId;

    @Column(name = "change_amount")
    private double changeAmount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "issue_type")
    private String issueType;

    @Column(name = "target_id")
    private long targetId;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    protected LegacyMileageHistoryEntity() {}

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getMileageId() {
        return mileageId;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public String getReason() {
        return reason;
    }

    public String getIssueType() {
        return issueType;
    }

    public long getTargetId() {
        return targetId;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }
}
