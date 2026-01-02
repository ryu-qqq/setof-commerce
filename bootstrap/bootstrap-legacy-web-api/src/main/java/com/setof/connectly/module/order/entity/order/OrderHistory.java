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
    @Column(name = "ORDER_HISTORY_ID")
    private long id;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "CHANGE_REASON")
    private String changeReason;

    @Column(name = "CHANGE_DETAIL_REASON")
    private String changeDetailReason;

    @Column(name = "ORDER_STATUS")
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
