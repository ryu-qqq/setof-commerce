package com.setof.connectly.module.user.dto.mileage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.mileage.enums.Reason;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMileageHistoryDto {
    @JsonIgnore private long mileageHistoryId;
    private long mileageId;
    private String title;
    private long paymentId;
    private long orderId;
    private double changeAmount;

    @Enumerated(value = EnumType.STRING)
    private Reason reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;

    @QueryProjection
    public UserMileageHistoryDto(
            long mileageHistoryId,
            long mileageId,
            String title,
            long paymentId,
            long orderId,
            double changeAmount,
            Reason reason,
            LocalDateTime usedDate,
            LocalDateTime expirationDate) {
        this.mileageHistoryId = mileageHistoryId;
        this.mileageId = mileageId;
        this.title = title;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.changeAmount = changeAmount;
        this.reason = reason;
        this.usedDate = usedDate;
        this.expirationDate = expirationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
