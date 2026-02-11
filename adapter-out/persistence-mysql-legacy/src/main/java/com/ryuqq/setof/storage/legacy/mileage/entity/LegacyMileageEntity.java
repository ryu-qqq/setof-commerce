package com.ryuqq.setof.storage.legacy.mileage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyMileageEntity - 레거시 마일리지 엔티티.
 *
 * <p>레거시 DB의 mileage 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "mileage")
public class LegacyMileageEntity {

    @Id
    @Column(name = "mileage_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "mileage_amount")
    private double mileageAmount;

    @Column(name = "used_mileage_amount")
    private double usedMileageAmount;

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "active_yn")
    @Enumerated(EnumType.STRING)
    private ActiveYn activeYn;

    @Column(name = "title")
    private String title;

    protected LegacyMileageEntity() {}

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public double getMileageAmount() {
        return mileageAmount;
    }

    public double getUsedMileageAmount() {
        return usedMileageAmount;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public ActiveYn getActiveYn() {
        return activeYn;
    }

    public String getTitle() {
        return title;
    }

    /**
     * 현재 잔여 마일리지 계산.
     *
     * @return 잔여 마일리지 (적립 - 사용)
     */
    public double getCurrentMileage() {
        return mileageAmount - usedMileageAmount;
    }

    /** ActiveYn - Y/N 구분 Enum. */
    public enum ActiveYn {
        Y,
        N
    }
}
