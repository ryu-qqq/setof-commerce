package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * DescriptionImageJpaEntity - 상세설명 이미지 JPA 엔티티.
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
@Table(name = "description_images")
public class DescriptionImageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "description_image_id")
    private Long id;

    @Column(name = "product_group_description_id", nullable = false)
    private Long productGroupDescriptionId;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected DescriptionImageJpaEntity() {}

    private DescriptionImageJpaEntity(
            Long id, Long productGroupDescriptionId, String imageUrl, int sortOrder) {
        this.id = id;
        this.productGroupDescriptionId = productGroupDescriptionId;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public static DescriptionImageJpaEntity create(
            Long id, Long productGroupDescriptionId, String imageUrl, int sortOrder) {
        return new DescriptionImageJpaEntity(id, productGroupDescriptionId, imageUrl, sortOrder);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupDescriptionId() {
        return productGroupDescriptionId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
