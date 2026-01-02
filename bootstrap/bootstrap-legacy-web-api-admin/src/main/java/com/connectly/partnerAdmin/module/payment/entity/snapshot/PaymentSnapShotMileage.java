package com.connectly.partnerAdmin.module.payment.entity.snapshot;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.generic.money.converter.MoneyConverter;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PAYMENT_SNAPSHOT_MILEAGE")
@Entity
public class PaymentSnapShotMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_SNAPSHOT_MILEAGE_ID")
    private long id;

    
    @Column(name = "USED_MILEAGE_AMOUNT")
    private BigDecimal usedMileageAmount;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

}
