package com.setof.connectly.module.payment.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.portone.dto.PgProviderTransDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PAYMENT_BILL")
@Entity
public class PaymentBill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_BILL_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "PAYMENT_METHOD_ID")
    private long paymentMethodId;

    @Column(name = "PAYMENT_AMOUNT")
    private long paymentAmount;

    @Column(name = "USED_MILEAGE_AMOUNT")
    private double usedMileageAmount;

    @Embedded private BuyerInfo buyerInfo;

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

    public void processWebHook(PgProviderTransDto pgProviderTransDto) {
        PaymentChannel paymentChannel = pgProviderTransDto.getPaymentChannel();
        BuyerInfo buyerInfo = pgProviderTransDto.getBuyerInfo();
        String receiptUrl = pgProviderTransDto.getReceiptUrl();

        updateCartInfo(pgProviderTransDto);
        updatePaymentAgencyId(pgProviderTransDto.getPgPaymentId());
        updatePaymentChannel(paymentChannel);
        updateBuyerInfo(buyerInfo);
        if (StringUtils.hasText(receiptUrl)) updateReceiptUrl(receiptUrl);
    }

    private void updateCartInfo(PgProviderTransDto pgProviderTransDto) {
        this.cardName = pgProviderTransDto.getCardName();
        this.cardNumber = pgProviderTransDto.getCardNumber();
    }

    private void updateBuyerInfo(BuyerInfo buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    private void updatePaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    private void updateReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    private void updatePaymentAgencyId(String paymentAgencyId) {
        this.paymentAgencyId = paymentAgencyId;
    }

    public void refundPay(long paymentAmount, long usedMileageAmount) {
        setPaymentAmount(paymentAmount);
        setUsedMileageAmount(usedMileageAmount);
    }

    private void setPaymentAmount(long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    private void setUsedMileageAmount(long usedMileageAmount) {
        this.usedMileageAmount = usedMileageAmount;
    }
}
