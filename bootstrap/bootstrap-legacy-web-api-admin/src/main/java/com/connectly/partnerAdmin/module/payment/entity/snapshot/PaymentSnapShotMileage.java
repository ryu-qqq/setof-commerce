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
@Table(name = "payment_snapshot_mileage")
@Entity
public class PaymentSnapShotMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_snapshot_mileage_id")
    private long id;

    
    @Column(name = "used_mileage_amount")
    private BigDecimal usedMileageAmount;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

}
