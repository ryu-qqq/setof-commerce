package com.setof.connectly.module.payment.entity.snapshot;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.user.entity.embedded.ShippingDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "PAYMENT_SNAPSHOT_SHIPPING_ADDRESS")
@Entity
public class PaymentSnapShotShippingAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_SNAPSHOT_SHIPPING_ADDRESS_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Embedded private ShippingDetails shippingDetails;
}
