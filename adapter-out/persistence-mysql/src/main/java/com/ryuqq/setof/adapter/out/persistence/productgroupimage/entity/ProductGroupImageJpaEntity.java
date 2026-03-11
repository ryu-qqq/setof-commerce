package com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductGroupImageJpaEntity - 상품그룹 이미지 JPA 엔티티.
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
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group_images")
public class ProductGroupImageJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected ProductGroupImageJpaEntity() {
        super();
    }

    private ProductGroupImageJpaEntity(
            Long id,
            Long productGroupId,
            String imageType,
            String imageUrl,
            int sortOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public static ProductGroupImageJpaEntity create(
            Long id,
            Long productGroupId,
            String imageType,
            String imageUrl,
            int sortOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductGroupImageJpaEntity(
                id,
                productGroupId,
                imageType,
                imageUrl,
                sortOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getImageType() {
        return imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
