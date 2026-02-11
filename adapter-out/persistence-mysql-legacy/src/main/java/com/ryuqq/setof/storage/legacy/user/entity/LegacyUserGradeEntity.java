package com.ryuqq.setof.storage.legacy.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyUserGradeEntity - 레거시 사용자 등급 엔티티.
 *
 * <p>레거시 DB의 user_grade 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "user_grade")
public class LegacyUserGradeEntity {

    @Id
    @Column(name = "user_grade_id")
    private Long id;

    @Column(name = "grade_name")
    @Enumerated(EnumType.STRING)
    private UserGradeEnum gradeName;

    @Column(name = "mileage_rate")
    private double mileageRate;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyUserGradeEntity() {}

    public Long getId() {
        return id;
    }

    public UserGradeEnum getGradeName() {
        return gradeName;
    }

    public double getMileageRate() {
        return mileageRate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** UserGradeEnum - 사용자 등급. */
    public enum UserGradeEnum {
        NORMAL_GRADE,
        SILVER,
        GOLD,
        PLATINUM,
        VIP
    }
}
