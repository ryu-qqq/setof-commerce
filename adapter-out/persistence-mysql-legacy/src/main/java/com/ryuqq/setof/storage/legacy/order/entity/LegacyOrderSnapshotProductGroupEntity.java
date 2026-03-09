package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotProductGroupEntity - 주문 스냅샷 상품그룹 엔티티.
 *
 * <p>주문 시점의 상품그룹 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_product_group")
public class LegacyOrderSnapshotProductGroupEntity {

    @Id
    @Column(name = "order_snapshot_product_group_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "product_group_name")
    private String productGroupName;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "brand_id")
    private long brandId;

    @Column(name = "category_id")
    private long categoryId;

    @Column(name = "REGULAR_PRICE")
    private long regularPrice;

    @Column(name = "SALE_PRICE")
    private long salePrice;

    @Column(name = "DIRECT_DISCOUNT_PRICE")
    private long directDiscountPrice;

    protected LegacyOrderSnapshotProductGroupEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public long getBrandId() {
        return brandId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getRegularPrice() {
        return regularPrice;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public long getDirectDiscountPrice() {
        return directDiscountPrice;
    }
}
