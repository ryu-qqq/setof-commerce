package com.ryuqq.setof.adapter.out.persistence.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductOptionMappingJpaEntity - 상품-옵션 매핑 JPA 엔티티.
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
@Table(name = "product_option_mappings")
public class ProductOptionMappingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "seller_option_value_id", nullable = false)
    private Long sellerOptionValueId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected ProductOptionMappingJpaEntity() {}

    private ProductOptionMappingJpaEntity(
            Long id, Long productId, Long sellerOptionValueId, boolean deleted, Instant deletedAt) {
        this.id = id;
        this.productId = productId;
        this.sellerOptionValueId = sellerOptionValueId;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public static ProductOptionMappingJpaEntity create(
            Long id, Long productId, Long sellerOptionValueId, boolean deleted, Instant deletedAt) {
        return new ProductOptionMappingJpaEntity(
                id, productId, sellerOptionValueId, deleted, deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getSellerOptionValueId() {
        return sellerOptionValueId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
