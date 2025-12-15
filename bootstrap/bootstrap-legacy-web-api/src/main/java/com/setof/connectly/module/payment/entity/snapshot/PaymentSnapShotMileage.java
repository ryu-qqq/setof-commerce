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
@Table(name = "PAYMENT_SNAPSHOT_MILEAGE")
@Entity
public class PaymentSnapShotMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_SNAPSHOT_MILEAGE_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "USED_MILEAGE_AMOUNT")
    private double usedMileageAmount;

    @Column(name = "MILEAGE_BALANCE")
    private double mileageBalance;
}
