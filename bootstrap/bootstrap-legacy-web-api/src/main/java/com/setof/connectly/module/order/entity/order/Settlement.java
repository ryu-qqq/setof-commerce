package com.setof.connectly.module.order.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "SETTLEMENT")
@Entity
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SETTLEMENT_ID")
    private long id;

    @Column(name = "SELLER_COMMISSION_RATE")
    private double sellerCommissionRate;

    @Column(name = "DIRECT_DISCOUNT_PRICE")
    private BigDecimal directDiscountPrice;

    @Column(name = "USE_MILEAGE_AMOUNT")
    private BigDecimal useMileageAmount;

    @Column(name = "DIRECT_DISCOUNT_SELLER_BURDEN_RATIO")
    private double directDiscountSellerBurdenRatio;

    @Column(name = "MILEAGE_SELLER_BURDEN_RATIO")
    private double mileageSellerBurdenRatio;

    @Column(name = "EXPECTED_SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedSettlementDate;

    @Column(name = "SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;

    @Column(name = "ORDER_ID")
    private long orderId;
}
