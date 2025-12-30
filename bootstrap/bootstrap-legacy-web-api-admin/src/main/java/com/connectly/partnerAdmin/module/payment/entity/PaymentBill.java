package com.connectly.partnerAdmin.module.payment.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.generic.money.converter.MoneyConverter;
import com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo;
import com.connectly.partnerAdmin.module.payment.enums.PaymentChannel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "PAYMENT_BILL")
public class PaymentBill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_BILL_ID")
    private long id;

    @Column(name = "PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;

    
    @Column(name = "USED_MILEAGE_AMOUNT")
    private BigDecimal usedMileageAmount;

    @Setter
    @Embedded
    private BuyerInfo buyerInfo;

    @Column(name = "PAYMENT_AGENCY_ID")
    private String paymentAgencyId;

    @Column(name = "PAYMENT_UNIQUE_ID")
    private String paymentUniqueId;

    @Column(name = "RECEIPT_URL")
    private String receiptUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_CHANNEL")
    private PaymentChannel paymentChannel;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "USER_ID")
    private long userId;


    public void setPayment(Payment payment) {
        if (this.payment != null && this.payment.equals(payment)) {
            return;
        }
        this.payment = payment;
        if (payment != null && !payment.getPaymentBill().equals(this)) {
            payment.setPaymentBill(this);
        }
    }


}
