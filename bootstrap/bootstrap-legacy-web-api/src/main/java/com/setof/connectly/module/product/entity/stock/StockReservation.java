package com.setof.connectly.module.product.entity.stock;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.enums.stock.ReservationStatus;
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
@Table(name = "stock_reservation")
@Entity
public class StockReservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_reservation_id")
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
