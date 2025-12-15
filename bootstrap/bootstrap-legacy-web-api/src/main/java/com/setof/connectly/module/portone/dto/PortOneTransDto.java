package com.setof.connectly.module.portone.dto;

import com.setof.connectly.module.payment.entity.VBankAccount;
import com.setof.connectly.module.payment.entity.embedded.BuyerInfo;
import com.setof.connectly.module.payment.enums.PaymentChannel;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.portone.enums.PortOnePaymentStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PortOneTransDto implements PgProviderTransDto {

    private long paymentId;
    private PaymentMethodEnum payMethod;
    private PaymentChannel paymentChannel;
    private VBankAccount vBankAccount;
    private BuyerInfo buyerInfo;
    private String receiptUrl;
    private String paymentUniqueId;
    private PortOnePaymentStatus portOnePaymentStatus;
    private String pgPaymentId;
    private long payAmount;
    private String cardName;
    private String cardNumber;

    @Override
    public PaymentStatus getPaymentStatus() {
        switch (portOnePaymentStatus) {
            case paid:
                return PaymentStatus.PAYMENT_COMPLETED;
            case ready:
                return PaymentStatus.PAYMENT_PROCESSING;
            case cancelled:
                return PaymentStatus.PAYMENT_CANCELLED;
            case failed:
                return PaymentStatus.PAYMENT_FAILED;
            default:
                throw new IllegalArgumentException("지원하지 않는 주문 상태입니다. -->" + portOnePaymentStatus);
        }
    }
}
