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
@Table(name = "settlement")
@Entity
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private long id;

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

    @Setter
    @Column(name = "expected_settlement_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedSettlementDate;

    @Setter
    @Column(name = "settlement_date")
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
