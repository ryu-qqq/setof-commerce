package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.payment.enums.PaymentStatus;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import com.setof.connectly.module.user.enums.SiteName;
import com.setof.connectly.module.utils.NumberUtils;
import com.setof.connectly.module.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDetail {

    private long paymentId;
    private String paymentAgencyId;
    private PaymentStatus paymentStatus;
    private PaymentMethodEnum paymentMethodEnum;
    private String paymentMethod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceledDate;

    private long userId;
    private SiteName siteName;
    private long preDiscountAmount;
    private long paymentAmount;
    private double usedMileageAmount;
    private String cardName;
    private String cardNumber;
    private double totalExpectedMileageAmount;

    @JsonIgnore private Set<Long> orderIds = new HashSet<>();

    @QueryProjection
    public PaymentDetail(
            long paymentId,
            String paymentAgencyId,
            PaymentStatus paymentStatus,
            PaymentMethodEnum paymentMethodEnum,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            long userId,
            SiteName siteName,
            long paymentAmount,
            double usedMileageAmount,
            String cardName,
            String cardNumber,
            Set<Long> orderIds) {
        this.paymentId = paymentId;
        this.paymentAgencyId = paymentAgencyId;
        this.paymentStatus = paymentStatus;
        this.paymentMethodEnum = paymentMethodEnum;
        this.paymentMethod = paymentMethodEnum.getDisplayName();
        this.paymentDate = paymentDate;
        this.canceledDate = canceledDate;
        this.userId = userId;
        this.siteName = siteName;
        this.paymentAmount = paymentAmount;
        this.usedMileageAmount = usedMileageAmount;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.totalExpectedMileageAmount = setTotalExpectedMileageAmount();
        this.orderIds = orderIds;
    }

    public void setPreDiscountAmount(long preDiscountAmount) {
        this.preDiscountAmount = preDiscountAmount;
    }

    private double setTotalExpectedMileageAmount() {
        return NumberUtils.downDotNumber(
                SecurityUtils.getUserGrade().getMileageReserveRate(), paymentAmount);
    }
}
