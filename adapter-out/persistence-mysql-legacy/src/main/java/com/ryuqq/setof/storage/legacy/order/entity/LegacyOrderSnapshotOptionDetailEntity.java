package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotOptionDetailEntity - 주문 스냅샷 옵션 상세 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_option_detail")
public class LegacyOrderSnapshotOptionDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_option_detail_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "option_detail_id")
    private long optionDetailId;

    @Column(name = "option_group_id")
    private long optionGroupId;

    @Column(name = "option_value")
    private String optionValue;

    protected LegacyOrderSnapshotOptionDetailEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getOptionDetailId() {
        return optionDetailId;
    }

    public long getOptionGroupId() {
        return optionGroupId;
    }

    public String getOptionValue() {
        return optionValue;
    }
}
