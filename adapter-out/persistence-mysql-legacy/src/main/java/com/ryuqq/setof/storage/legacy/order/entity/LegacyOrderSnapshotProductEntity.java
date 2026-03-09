package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotProductEntity - 주문 스냅샷 상품 엔티티.
 *
 * <p>주문 시점의 상품(SKU) 정보를 스냅샷으로 보관합니다.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_product")
public class LegacyOrderSnapshotProductEntity {

    @Id
    @Column(name = "order_snapshot_product_id")
    private Long id;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    protected LegacyOrderSnapshotProductEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }
}
