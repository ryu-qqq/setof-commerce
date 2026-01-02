package com.connectly.partnerAdmin.module.payment.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.entity.embedded.PaymentDetails;
import com.connectly.partnerAdmin.module.payment.entity.snapshot.PaymentSnapShotMileage;
import com.connectly.partnerAdmin.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "PAYMENT")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private long id;

    @Embedded
    private PaymentDetails paymentDetails;

    @Column(name = "USER_ID")
    private long userId;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private PaymentBill paymentBill;

    @OneToOne(mappedBy = "payment",  fetch = FetchType.LAZY, optional = true)
    private PaymentSnapShotMileage paymentSnapShotMileage;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private PaymentSnapShotShippingAddress paymentSnapShotShippingAddress;

    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Order> orders = new LinkedHashSet<>();


    public Payment(long userId, PaymentDetails paymentDetails) {
        this.userId = userId;
        this.paymentDetails = paymentDetails;
    }

    public void setPaymentBill(PaymentBill paymentBill) {
        if (this.paymentBill != null) {
            this.paymentBill.setPayment(null);
        }
        this.paymentBill = paymentBill;
        if (paymentBill != null) {
            paymentBill.setPayment(this);
        }
    }


    public void setPaymentSnapShotShippingAddress(PaymentSnapShotShippingAddress paymentSnapShotShippingAddress) {

        if (this.paymentSnapShotShippingAddress != null) {
            this.paymentSnapShotShippingAddress.setPayment(null);
        }

        this.paymentSnapShotShippingAddress = paymentSnapShotShippingAddress;

        if (paymentSnapShotShippingAddress != null) {
            paymentSnapShotShippingAddress.setPayment(this);
        }
    }

    public void setPaymentSnapShotMileage(PaymentSnapShotMileage paymentSnapShotMileage) {
        this.paymentSnapShotMileage = paymentSnapShotMileage;
        if (paymentSnapShotMileage != null) {
            paymentSnapShotMileage.setPayment(this);
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setPayment(this);
    }

}
