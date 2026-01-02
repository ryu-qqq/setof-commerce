package com.setof.connectly.module.order.entity.order;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "orders_history")
@Entity
public class OrderHistory extends BaseEntity {

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
    private OrderStatus orderStatus;

    public OrderHistory(
            long orderId, String changeReason, String changeDetailReason, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.orderStatus = orderStatus;
    }
}
