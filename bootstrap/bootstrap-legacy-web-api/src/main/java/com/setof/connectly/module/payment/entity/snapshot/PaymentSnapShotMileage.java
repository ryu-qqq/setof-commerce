package com.setof.connectly.module.payment.entity.snapshot;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "used_mileage_amount")
    private double usedMileageAmount;

    @Column(name = "mileage_balance")
    private double mileageBalance;
}
