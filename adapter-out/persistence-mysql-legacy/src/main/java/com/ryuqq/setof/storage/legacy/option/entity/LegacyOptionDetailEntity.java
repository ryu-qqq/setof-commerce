package com.ryuqq.setof.storage.legacy.option.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyOptionDetailEntity - 레거시 옵션 상세 엔티티.
 *
 * <p>레거시 DB의 option_detail 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "option_detail")
public class LegacyOptionDetailEntity {

    @Id
    @Column(name = "option_detail_id")
    private Long id;

    @Column(name = "option_group_id")
    private Long optionGroupId;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyOptionDetailEntity() {}

    public Long getId() {
        return id;
    }

    public Long getOptionGroupId() {
        return optionGroupId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
