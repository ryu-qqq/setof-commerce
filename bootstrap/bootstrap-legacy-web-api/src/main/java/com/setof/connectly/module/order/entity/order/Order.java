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
    @Column(name = "order_id")
    private Long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "seller_id")
    private long sellerId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "order_amount")
    private long orderAmount;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "settlement_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;

    @Column(name = "review_yn")
    @Enumerated(EnumType.STRING)
    private Yn reviewYn;

    @Column(name = "quantity")
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
