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
@Table(name = "payment_bill")
@Entity
public class PaymentBill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_bill_id")
    private long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "payment_method_id")
    private long paymentMethodId;

    @Column(name = "payment_amount")
    private long paymentAmount;

    @Column(name = "used_mileage_amount")
    private double usedMileageAmount;

    @Embedded private BuyerInfo buyerInfo;

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
