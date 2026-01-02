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
@Table(name = "payment_bill")
public class PaymentBill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_bill_id")
    private long id;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    
    @Column(name = "used_mileage_amount")
    private BigDecimal usedMileageAmount;

    @Setter
    @Embedded
    private BuyerInfo buyerInfo;

    @Column(name = "payment_agency_id")
    private String paymentAgencyId;

    @Column(name = "payment_unique_id")
    private String paymentUniqueId;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_channel")
    private PaymentChannel paymentChannel;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_number")
    private String cardNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_METHOD_ID", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "user_id")
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
