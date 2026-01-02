package com.connectly.partnerAdmin.module.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDERS_HISTORY")
@Entity
public class OrderHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_HISTORY_ID")
    private long id;

    @Column(name = "CHANGE_REASON", length = 200, nullable = true)
    private String changeReason;

    @Column(name = "CHANGE_DETAIL_REASON", length = 500, nullable = true)
    private String changeDetailReason;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    public OrderHistory(Order order) {
        this.order = order;
        this.changeReason = "";
        this.changeDetailReason = "";
        this.orderStatus = order.getOrderStatus();
    }

    public OrderHistory(Order order, String changeReason, String changeDetailReason, OrderStatus orderStatus) {
        this.order = order;
        this.changeReason = changeReason;
        this.changeDetailReason = changeDetailReason;
        this.orderStatus = orderStatus;
    }

    public String getChangeReason() {
        if(!StringUtils.hasText(changeReason)) return "";
        return changeReason;
    }

    public String getChangeDetailReason() {
        if(!StringUtils.hasText(changeDetailReason)) return "";
        return changeDetailReason;
    }

    @Override
    public int hashCode() {
        return  (orderStatus + changeReason + changeDetailReason).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OrderHistory) {
            OrderHistory oh = (OrderHistory)obj;
            return this.hashCode()==oh.hashCode();
        }
        return false;

    }
}
