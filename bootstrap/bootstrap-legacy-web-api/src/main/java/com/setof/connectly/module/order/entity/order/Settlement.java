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
@Table(name = "settlement")
@Entity
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private Long id;

    @Column(name = "seller_commission_rate")
    private double sellerCommissionRate;

    @Column(name = "direct_discount_price")
    private BigDecimal directDiscountPrice;

    @Column(name = "use_mileage_amount")
    private BigDecimal useMileageAmount;

    @Column(name = "direct_discount_seller_burden_ratio")
    private double directDiscountSellerBurdenRatio;

    @Column(name = "mileage_seller_burden_ratio")
    private double mileageSellerBurdenRatio;

    @Column(name = "expected_settlement_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedSettlementDate;

    @Column(name = "settlement_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;

    @Column(name = "order_id")
    private long orderId;
}
