package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyOrderSnapshotOptionGroupEntity - 주문 스냅샷 옵션 그룹 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "order_snapshot_option_group")
public class LegacyOrderSnapshotOptionGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_option_group_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "option_group_id")
    private long optionGroupId;

    @Column(name = "option_name")
    private String optionName;

    protected LegacyOrderSnapshotOptionGroupEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getOptionGroupId() {
        return optionGroupId;
    }

    public String getOptionName() {
        return optionName;
    }
}
