package com.ryuqq.setof.adapter.out.persistence.productgroup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * SellerOptionGroupJpaEntity - 셀러 옵션 그룹 JPA 엔티티.
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
@Table(name = "seller_option_group")
public class SellerOptionGroupJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "option_group_name", nullable = false, length = 100)
    private String optionGroupName;

    @Column(name = "canonical_option_group_id")
    private Long canonicalOptionGroupId;

    @Column(name = "input_type", nullable = false, length = 50)
    private String inputType;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected SellerOptionGroupJpaEntity() {}

    private SellerOptionGroupJpaEntity(
            Long id,
            Long productGroupId,
            String optionGroupName,
            Long canonicalOptionGroupId,
            String inputType,
            int sortOrder,
            boolean deleted,
            Instant deletedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.optionGroupName = optionGroupName;
        this.canonicalOptionGroupId = canonicalOptionGroupId;
        this.inputType = inputType;
        this.sortOrder = sortOrder;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public static SellerOptionGroupJpaEntity create(
            Long id,
            Long productGroupId,
            String optionGroupName,
            Long canonicalOptionGroupId,
            String inputType,
            int sortOrder,
            boolean deleted,
            Instant deletedAt) {
        return new SellerOptionGroupJpaEntity(
                id,
                productGroupId,
                optionGroupName,
                canonicalOptionGroupId,
                inputType,
                sortOrder,
                deleted,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getOptionGroupName() {
        return optionGroupName;
    }

    public Long getCanonicalOptionGroupId() {
        return canonicalOptionGroupId;
    }

    public String getInputType() {
        return inputType;
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
}
