package com.ryuqq.setof.storage.legacy.mileage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotMileageDetailEntity - 레거시 주문 스냅샷 마일리지 상세 엔티티.
 *
 * <p>레거시 DB의 order_snapshot_mileage_detail 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_mileage_detail")
public class LegacyOrderSnapshotMileageDetailEntity {

    @Id
    @Column(name = "order_snapshot_mileage_detail_id")
    private Long id;

    @Column(name = "order_snapshot_mileage_id")
    private long orderSnapshotMileageId;

    @Column(name = "mileage_id")
    private long mileageId;

    @Column(name = "used_amount")
    private double usedAmount;

    @Column(name = "mileage_balance")
    private double mileageBalance;

    protected LegacyOrderSnapshotMileageDetailEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderSnapshotMileageId() {
        return orderSnapshotMileageId;
    }

    public long getMileageId() {
        return mileageId;
    }

    public double getUsedAmount() {
        return usedAmount;
    }

    public double getMileageBalance() {
        return mileageBalance;
    }
}
