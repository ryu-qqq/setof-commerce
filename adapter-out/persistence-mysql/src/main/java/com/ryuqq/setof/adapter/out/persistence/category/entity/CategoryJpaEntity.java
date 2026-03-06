package com.ryuqq.setof.adapter.out.persistence.category.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CategoryJpaEntity - 카테고리 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Entity
@Table(name = "category")
public class CategoryJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "category_depth", nullable = false)
    private int categoryDepth;

    @Column(name = "parent_category_id", nullable = false)
    private Long parentCategoryId;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "displayed", nullable = false)
    private boolean displayed;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_group", nullable = false, length = 20)
    private TargetGroup targetGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, length = 20)
    private CategoryType categoryType;

    @Column(name = "path", nullable = false, length = 500)
    private String path;

    protected CategoryJpaEntity() {
        super();
    }

    private CategoryJpaEntity(
            Long id,
            String categoryName,
            int categoryDepth,
            Long parentCategoryId,
            String displayName,
            boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            String path,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentCategoryId = parentCategoryId;
        this.displayName = displayName;
        this.displayed = displayed;
        this.targetGroup = targetGroup;
        this.categoryType = categoryType;
        this.path = path;
    }

    public static CategoryJpaEntity create(
            Long id,
            String categoryName,
            int categoryDepth,
            Long parentCategoryId,
            String displayName,
            boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            String path,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new CategoryJpaEntity(
                id,
                categoryName,
                categoryDepth,
                parentCategoryId,
                displayName,
                displayed,
                targetGroup,
                categoryType,
                path,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryDepth() {
        return categoryDepth;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isDisplayed() {
        return displayed;
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
}
