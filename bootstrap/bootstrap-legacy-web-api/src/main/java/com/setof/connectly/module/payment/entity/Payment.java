package com.setof.connectly.module.payment.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.payment.entity.embedded.PaymentDetails;
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
@Table(name = "payment")
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long id;

    @Embedded private PaymentDetails paymentDetails;

    public void payCompleted() {
        paymentDetails.paymentCompleted();
    }

    public void payFailed() {
        paymentDetails.paymentFailed();
    }

    public boolean isAvailableRefundPayment() {
        return this.paymentDetails.getPaymentStatus().isCompleted()
                || this.paymentDetails.getPaymentStatus().isPartialCanceled();
    }

    public void paymentPartialRefunded() {
        paymentDetails.paymentPartialRefund();
    }

    public void paymentRefunded() {
        paymentDetails.paymentRefunded();
    }
}
