package com.connectly.partnerAdmin.module.payment.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus implements EnumType {

    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    PAYMENT_CANCELLED,
    PAYMENT_PARTIAL_REFUNDED,
    PAYMENT_REFUNDED,
    PAYMENT_FAILED;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return this.name();
    }


    public boolean isCompleted(){
        return this.equals(PAYMENT_COMPLETED);
    }

    public boolean isPartialCanceled(){
        return this.equals(PAYMENT_PARTIAL_REFUNDED);
    }

    public boolean isCanceled(){
        return this.equals(PAYMENT_CANCELLED);
    }

    public boolean isProcessing(){
        return this.equals(PAYMENT_PROCESSING);
    }
}
