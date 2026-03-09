package com.ryuqq.setof.storage.legacy.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyUserGradeEntity - 레거시 회원 등급 엔티티.
 *
 * <p>레거시 DB의 user_grade 테이블 매핑. fetchUser JOIN 쿼리에서 사용됩니다.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Entity
@Table(name = "user_grade")
public class LegacyUserGradeEntity {

    @Id
    @Column(name = "user_grade_id")
    private Long id;

    @Column(name = "grade_name")
    private String gradeName;

    protected LegacyUserGradeEntity() {}

    public Long getId() {
        return id;
    }

    public String getGradeName() {
        return gradeName;
    }
}
