package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyOrderHistoryEntity - 레거시 주문 이력 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "orders_history")
public class LegacyOrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "change_reason")
    private String changeReason;

    @Column(name = "change_detail_reason")
    private String changeDetailReason;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private LegacyOrderStatus orderStatus;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyOrderHistoryEntity() {}

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public String getChangeDetailReason() {
        return changeDetailReason;
    }

    public LegacyOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
