package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotProductGroupImageEntity - 주문 스냅샷 상품그룹 이미지 엔티티.
 *
 * <p>주문 시점의 상품그룹 이미지 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_product_group_image")
public class LegacyOrderSnapshotProductGroupImageEntity {

    @Id
    @Column(name = "order_snapshot_product_group_image_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "product_group_image_type")
    @Enumerated(EnumType.STRING)
    private ProductGroupImageType productGroupImageType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyOrderSnapshotProductGroupImageEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public ProductGroupImageType getProductGroupImageType() {
        return productGroupImageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    /** 상품그룹 이미지 유형. */
    public enum ProductGroupImageType {
        MAIN,
        SUB,
        DETAIL
    }

    /** Y/N Enum. */
    public enum Yn {
        Y,
        N
    }
}
