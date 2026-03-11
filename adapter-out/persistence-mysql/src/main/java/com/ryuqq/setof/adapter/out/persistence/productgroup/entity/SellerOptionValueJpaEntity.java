package com.ryuqq.setof.adapter.out.persistence.productgroup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * SellerOptionValueJpaEntity - 셀러 옵션 값 JPA 엔티티.
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
@Table(name = "seller_option_values")
public class SellerOptionValueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_option_group_id", nullable = false)
    private Long sellerOptionGroupId;

    @Column(name = "option_value_name", nullable = false, length = 100)
    private String optionValueName;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SellerOptionValueJpaEntity() {}

    private SellerOptionValueJpaEntity(
            Long id,
            Long sellerOptionGroupId,
            String optionValueName,
            int sortOrder,
            boolean deleted,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.sellerOptionGroupId = sellerOptionGroupId;
        this.optionValueName = optionValueName;
        this.sortOrder = sortOrder;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SellerOptionValueJpaEntity create(
            Long id,
            Long sellerOptionGroupId,
            String optionValueName,
            int sortOrder,
            boolean deleted,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new SellerOptionValueJpaEntity(
                id,
                sellerOptionGroupId,
                optionValueName,
                sortOrder,
                deleted,
                deletedAt,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getSellerOptionGroupId() {
        return sellerOptionGroupId;
    }

    public String getOptionValueName() {
        return optionValueName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
