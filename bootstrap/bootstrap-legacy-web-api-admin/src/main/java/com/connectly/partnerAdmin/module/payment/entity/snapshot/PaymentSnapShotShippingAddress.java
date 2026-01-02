package com.connectly.partnerAdmin.module.payment.entity.snapshot;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;
import jakarta.persistence.*;
import lombok.*;



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

    @Embedded
    private ShippingDetails shippingDetails;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    public PaymentSnapShotShippingAddress(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }
}

