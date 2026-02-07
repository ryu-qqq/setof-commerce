package com.ryuqq.setof.storage.legacy.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyUserMileageEntity - 레거시 사용자 마일리지 엔티티.
 *
 * <p>레거시 DB의 user_mileage 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "user_mileage")
public class LegacyUserMileageEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "current_mileage")
    private double currentMileage;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyUserMileageEntity() {}

    public Long getId() {
        return id;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
