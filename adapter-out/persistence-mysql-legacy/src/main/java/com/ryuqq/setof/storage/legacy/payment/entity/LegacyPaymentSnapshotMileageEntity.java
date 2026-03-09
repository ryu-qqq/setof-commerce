package com.ryuqq.setof.storage.legacy.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyPaymentSnapshotMileageEntity - 결제 시점 마일리지 스냅샷 엔티티.
 *
 * <p>결제 시점의 마일리지 사용 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "payment_snapshot_mileage")
public class LegacyPaymentSnapshotMileageEntity {

    @Id
    @Column(name = "payment_snapshot_mileage_id")
    private Long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "USED_MILEAGE_AMOUNT")
    private long usedMileageAmount;

    @Column(name = "MILEAGE_BALANCE")
    private long mileageBalance;

    protected LegacyPaymentSnapshotMileageEntity() {}

    public Long getId() {
        return id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public long getUserId() {
        return userId;
    }

    public long getUsedMileageAmount() {
        return usedMileageAmount;
    }

    public long getMileageBalance() {
        return mileageBalance;
    }
}
