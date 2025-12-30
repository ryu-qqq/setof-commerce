package com.connectly.partnerAdmin.module.order.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Setter
    @Column(name = "EXPECTED_SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedSettlementDate;

    @Setter
    @Column(name = "SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    public void settlementCompleted(){
        this.settlementDate = LocalDateTime.now();
    }

    public void setOrder(Order order) {
        if (this.order != null && this.order.equals(order)) {
            return;
        }
        this.order = order;
        if (order != null && !order.getSettlement().equals(this)) {
            order.setSettlement(this);
        }
    }

}
