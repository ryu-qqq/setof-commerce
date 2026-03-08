package com.ryuqq.setof.storage.legacy.product.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyProductGroupImageEntity - 레거시 상품그룹 이미지 엔티티.
 *
 * <p>레거시 DB의 product_group_image 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group_image")
public class LegacyProductGroupImageEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "product_group_image_id")
    private Long id;

    @Column(name = "product_group_id")
    private Long productGroupId;

    @Column(name = "product_group_image_type")
    @Enumerated(EnumType.STRING)
    private ProductGroupImageType productGroupImageType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyProductGroupImageEntity() {}

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public ProductGroupImageType getProductGroupImageType() {
        return productGroupImageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    /** ProductGroupImageType - 상품그룹 이미지 타입 Enum. */
    public enum ProductGroupImageType {
        MAIN,
        SUB,
        DETAIL
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
