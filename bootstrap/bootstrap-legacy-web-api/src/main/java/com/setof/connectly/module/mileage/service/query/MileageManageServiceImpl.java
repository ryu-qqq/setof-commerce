package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.mileage.dto.*;
import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MileageManageServiceImpl implements MileageManageService {

    public MileageDeductionResult calculateMileageDeduction(
            MileageQueryDto mileageQueryDto, double usedMileageAmount) {
        List<MileageDto> usedMileages = new ArrayList<>();
        double remainingMileageToUse = usedMileageAmount;

        for (MileageDto mileageDto : mileageQueryDto.getMileages()) {
            if (remainingMileageToUse <= 0) break;
            double availableMileage = mileageDto.getAvailableMileage();
            double mileageToUse = Math.min(availableMileage, remainingMileageToUse);
            mileageDto.setCurrentUsedMileageAmount(mileageToUse);
            usedMileages.add(mileageDto);
            remainingMileageToUse -= mileageToUse;
        }

        double currentRemainMileageAmount = mileageQueryDto.getCurrentMileage() - usedMileageAmount;
        return new MileageDeductionResult(usedMileages, currentRemainMileageAmount);
    }

    public MileageRefundResult calculateRefundMileages(
            MileageRefundQueryDto mileageRefundQueryDto, double expectedRefundMileage) {
        List<MileageRefundDto> refundMileages = new ArrayList<>();
        List<MileageRefundDto> expiredMileages = new ArrayList<>();
        double totalRefundMileage = 0;

        LocalDateTime now = LocalDateTime.now();

        for (MileageRefundDto mileageRefundDto : mileageRefundQueryDto.getMileages()) {
            if (!mileageRefundDto.getExpirationDate().isAfter(now)) {
                expiredMileages.add(mileageRefundDto);
            }
        }

        return new MileageRefundResult(refundMileages, expiredMileages, totalRefundMileage);
    }

    public List<OrderSnapShotMileageDetail> distributeUsedMileage(
            long orderSnapShotMileageId,
            List<MileageDto> usedMileages,
            double usedMileageForOrder) {
        usedMileages.sort(Comparator.comparing(MileageDto::getExpirationDate));

        double remainingMileageToDistribute = usedMileageForOrder;

        List<OrderSnapShotMileageDetail> details = new ArrayList<>();

        for (MileageDto mileage : usedMileages) {
            if (remainingMileageToDistribute <= 0) break;

            double availableForUse =
                    mileage.getCurrentUsedMileageAmount() - mileage.getUsedMileageForCalculate();
            if (availableForUse <= 0) {
                mileage.deActiveYn();
                break;
            }

            double usedAmount = Math.min(remainingMileageToDistribute, availableForUse);

            remainingMileageToDistribute -= usedAmount;

            OrderSnapShotMileageDetail detail =
                    OrderSnapShotMileageDetail.builder()
                            .orderSnapShotMileageId(orderSnapShotMileageId)
                            .mileageId(mileage.getMileageId())
                            .usedAmount(usedAmount)
                            .mileageBalance(mileage.getAvailableMileage() - usedAmount)
                            .build();

            mileage.setUsedMileageForCalculate(usedAmount);
            mileage.setUsedMileageAmount(usedAmount);
            mileage.setOrderPerChangeAmount(usedAmount);
            details.add(detail);
        }

        return details;
    }
}
