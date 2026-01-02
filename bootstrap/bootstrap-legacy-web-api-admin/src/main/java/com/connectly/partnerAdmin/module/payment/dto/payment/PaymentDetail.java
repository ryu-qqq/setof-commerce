package com.connectly.partnerAdmin.module.payment.dto.payment;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.payment.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDetail {

    private long paymentId;
    private String paymentAgencyId;
    private PaymentStatus paymentStatus;
    private PaymentMethodEnum paymentMethod;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceledDate;
    private long userId;
    private SiteName siteName;
    private Money billAmount;
    @Setter
    private Money paymentAmount;
    private Money usedMileageAmount;

    @QueryProjection
    public PaymentDetail(long paymentId, String paymentAgencyId, PaymentStatus paymentStatus, PaymentMethodEnum paymentMethod, LocalDateTime paymentDate, LocalDateTime canceledDate, long userId, SiteName siteName, BigDecimal paymentAmount, BigDecimal usedMileageAmount) {
        this.paymentId = paymentId;
        this.paymentAgencyId = paymentAgencyId;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.canceledDate = canceledDate;
        this.userId = userId;
        this.siteName = siteName;
        this.billAmount = Money.wons(paymentAmount).plus(Money.wons(usedMileageAmount));
        this.paymentAmount = Money.wons(paymentAmount);
        this.usedMileageAmount = Money.wons(usedMileageAmount);
    }
}
