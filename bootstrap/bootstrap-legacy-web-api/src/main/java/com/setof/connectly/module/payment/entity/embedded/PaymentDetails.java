package com.setof.connectly.module.payment.entity.embedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.user.enums.SiteName;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class PaymentDetails {

    private long userId;
    private long paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private SiteName siteName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceledDate;

    public void paymentCompleted() {
        paymentStatus = PaymentStatus.PAYMENT_COMPLETED;
        paymentDate = LocalDateTime.now();
    }

    public void paymentFailed() {
        paymentStatus = PaymentStatus.PAYMENT_FAILED;
        canceledDate = LocalDateTime.now();
    }

    public void paymentPartialRefund() {
        paymentStatus = PaymentStatus.PAYMENT_PARTIAL_REFUNDED;
    }

    public void paymentRefunded() {
        paymentStatus = PaymentStatus.PAYMENT_REFUNDED;
        canceledDate = LocalDateTime.now();
    }
}
