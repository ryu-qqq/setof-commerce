package com.setof.connectly.module.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.exception.mileage.ExceedUserMileageException;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageDto {

    private long mileageId;
    private double mileageAmount;
    private double usedMileageAmount;
    private LocalDateTime issuedDate;
    private LocalDateTime expirationDate;
    private Yn activeYn;
    private double currentUsedMileageAmount; // 결제에서 차감되어야하는 마일리지 금액
    private double usedMileageForCalculate;
    private double orderPerChangeAmount;
    private static final double EPSILON = 0.0001;

    @QueryProjection
    public MileageDto(
            long mileageId,
            double mileageAmount,
            double usedMileageAmount,
            LocalDateTime issuedDate,
            LocalDateTime expirationDate,
            Yn activeYn) {
        this.mileageId = mileageId;
        this.mileageAmount = mileageAmount;
        this.usedMileageAmount = usedMileageAmount;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.activeYn = activeYn;
    }

    public double getAvailableMileage() {
        double availableMileage = mileageAmount - usedMileageAmount;
        if (availableMileage <= 0) throw new ExceedUserMileageException();
        return availableMileage;
    }

    public void deActiveYn() {
        this.activeYn = Yn.N;
    }

    public void activeYn() {
        this.activeYn = Yn.Y;
    }

    public void setCurrentUsedMileageAmount(double usedMileageAmount) {
        this.currentUsedMileageAmount = usedMileageAmount;
    }

    public void setUsedMileageForCalculate(double usedMileageForCalculate) {
        this.usedMileageForCalculate = this.usedMileageForCalculate + usedMileageForCalculate;
    }

    public void setUsedMileageAmount(double usedMileageAmount) {
        this.usedMileageAmount = this.usedMileageAmount + usedMileageAmount;
    }

    public void setOrderPerChangeAmount(double orderPerChangeAmount) {
        this.orderPerChangeAmount = orderPerChangeAmount;
    }
}
