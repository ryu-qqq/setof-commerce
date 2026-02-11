package com.ryuqq.setof.storage.legacy.option.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyOptionGroupEntity - 레거시 옵션 그룹 엔티티.
 *
 * <p>레거시 DB의 option_group 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "option_group")
public class LegacyOptionGroupEntity {

    @Id
    @Column(name = "option_group_id")
    private Long id;

    @Column(name = "option_name")
    @Enumerated(EnumType.STRING)
    private OptionName optionName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected LegacyOptionGroupEntity() {}

    public Long getId() {
        return id;
    }

    public OptionName getOptionName() {
        return optionName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /** OptionName - 옵션명 Enum. */
    public enum OptionName {
        COLOR,
        SIZE,
        MATERIAL,
        STYLE,
        PATTERN,
        ETC
    }
}
