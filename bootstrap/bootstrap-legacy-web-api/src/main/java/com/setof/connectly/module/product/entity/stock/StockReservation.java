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
    @Column(name = "STOCK_RESERVATION_ID")
    private long id;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    @Column(name = "RESERVED_AT")
    private LocalDateTime reservedAt;

    @Column(name = "RESERVATION_STATUS")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
