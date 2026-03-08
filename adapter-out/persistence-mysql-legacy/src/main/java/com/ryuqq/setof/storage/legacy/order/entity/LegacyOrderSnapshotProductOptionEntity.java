package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotProductOptionEntity - 주문 스냅샷 상품 옵션 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_product_option")
public class LegacyOrderSnapshotProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_option_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "option_group_id")
    private long optionGroupId;

    @Column(name = "option_detail_id")
    private long optionDetailId;

    protected LegacyOrderSnapshotProductOptionEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getOptionGroupId() {
        return optionGroupId;
    }

    public long getOptionDetailId() {
        return optionDetailId;
    }
}
