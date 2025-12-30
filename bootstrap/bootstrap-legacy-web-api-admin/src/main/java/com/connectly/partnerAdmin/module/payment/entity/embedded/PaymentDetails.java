package com.connectly.partnerAdmin.module.payment.entity.embedded;


import com.connectly.partnerAdmin.module.generic.money.converter.MoneyConverter;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.enums.PaymentStatus;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class PaymentDetails {

    
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private SiteName siteName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceledDate;

    public void paymentPartialRefund(){
        paymentStatus = PaymentStatus.PAYMENT_PARTIAL_REFUNDED;
    }

    public void paymentRefunded(){
        paymentStatus = PaymentStatus.PAYMENT_REFUNDED;
        canceledDate = LocalDateTime.now();
    }

}
