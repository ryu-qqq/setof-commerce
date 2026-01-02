package com.setof.connectly.module.order.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "ORDER_AMOUNT")
    private long orderAmount;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;

    @Column(name = "REVIEW_YN")
    @Enumerated(EnumType.STRING)
    private Yn reviewYn;

    @Column(name = "QUANTITY")
    private int quantity;

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isAvailableRefundOrder() {
        return this.orderStatus.isOrderCompleted()
                || this.orderStatus.isDeliveryStep()
                || this.orderStatus.isRejectedClaimStep();
    }

    public void writeReview() {
        this.reviewYn = Yn.Y;
    }

    public void deleteReview() {
        this.reviewYn = Yn.N;
    }
}
