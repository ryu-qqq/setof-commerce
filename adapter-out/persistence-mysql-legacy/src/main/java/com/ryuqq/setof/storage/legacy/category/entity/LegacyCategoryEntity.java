package com.ryuqq.setof.storage.legacy.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyCategoryEntity - 레거시 카테고리 엔티티.
 *
 * <p>레거시 DB의 category 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "category")
public class LegacyCategoryEntity {

    @Id
    @Column(name = "category_id")
    private Long id;

    @Column(name = "CATEGORY_NAME", length = 50)
    private String categoryName;

    @Column(name = "CATEGORY_DEPTH")
    private int categoryDepth;

    @Column(name = "PARENT_CATEGORY_ID")
    private long parentCategoryId;

    @Column(name = "DISPLAY_NAME", length = 50)
    private String displayName;

    @Column(name = "DISPLAY_YN", length = 1)
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "TARGET_GROUP", length = 10)
    @Enumerated(EnumType.STRING)
    private TargetGroup targetGroup;

    @Column(name = "CATEGORY_TYPE", length = 10)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(name = "PATH", length = 255)
    private String path;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyCategoryEntity() {}

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryDepth() {
        return categoryDepth;
    }

    public long getParentCategoryId() {
        return parentCategoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public TargetGroup getTargetGroup() {
        return targetGroup;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }

    /** TargetGroup - 대상 그룹 Enum. */
    public enum TargetGroup {
        MALE,
        FEMALE,
        KIDS,
        LIFE
    }

    /** CategoryType - 카테고리 타입 Enum. */
    public enum CategoryType {
        NONE,
        CLOTHING,
        SHOSE,
        BAG,
        ACC
    }
}
