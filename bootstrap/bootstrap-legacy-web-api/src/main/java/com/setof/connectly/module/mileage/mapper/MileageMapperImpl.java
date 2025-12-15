package com.setof.connectly.module.mileage.mapper;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.mileage.dto.MileageDto;
import com.setof.connectly.module.mileage.entity.Mileage;
import com.setof.connectly.module.mileage.entity.MileageHistory;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import com.setof.connectly.module.mileage.enums.Reason;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
import com.setof.connectly.module.user.entity.Users;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class MileageMapperImpl implements MileageMapper {

    private static final double DEFAULT_USED_MILEAGE_AMOUNT = 0.0;
    private static final long DEFAULT_TARGET_ID = 0L;

    @Override
    public MileageTransaction toMileageTransaction(
            long userId,
            MileageIssueType issueType,
            long targetId,
            double mileageAmount,
            MileageStatus mileageStatus) {

        return MileageTransaction.builder()
                .userId(userId)
                .issueType(issueType)
                .targetId(targetId)
                .expectedMileageAmount(mileageAmount)
                .status(mileageStatus)
                .build();
    }

    @Override
    public MileageHistory toMileageHistory(
            long userId,
            long targetId,
            Reason reason,
            MileageIssueType issueType,
            MileageDto mileage) {
        return MileageHistory.builder()
                .userId(userId)
                .mileageId(mileage.getMileageId())
                .changeAmount(
                        reason.isPlus()
                                ? mileage.getOrderPerChangeAmount()
                                : mileage.getOrderPerChangeAmount() * -1)
                .reason(reason)
                .issueType(issueType)
                .targetId(targetId)
                .build();
    }

    @Override
    public MileageHistory toMileageHistory(Mileage mileage) {
        return MileageHistory.builder()
                .userId(mileage.getUserId())
                .mileageId(mileage.getId())
                .changeAmount(mileage.getMileageAmount())
                .reason(Reason.SAVE)
                .issueType(MileageIssueType.ETC)
                .targetId(DEFAULT_TARGET_ID)
                .build();
    }

    @Override
    public PaymentSnapShotMileage toSnapShot(
            long userId,
            long paymentId,
            double usedMileageAmount,
            double currentUsingMileageAmount) {
        return PaymentSnapShotMileage.builder()
                .paymentId(paymentId)
                .userId(userId)
                .usedMileageAmount(usedMileageAmount)
                .mileageBalance(currentUsingMileageAmount)
                .build();
    }

    @Override
    public Mileage joinMileageForOpenEvent(Users users) {
        LocalDate endOfMonth = LocalDate.now().plusDays(30);
        LocalDateTime expirationDateTime = endOfMonth.atTime(23, 59, 59);

        return Mileage.builder()
                .userId(users.getId())
                .issuedDate(LocalDateTime.now())
                .expirationDate(expirationDateTime)
                .mileageAmount(users.getUserMileage().getCurrentMileage())
                .usedMileageAmount(DEFAULT_USED_MILEAGE_AMOUNT)
                .activeYn(Yn.Y)
                .title("리뉴얼기념 신규회원 적립금")
                .build();
    }
}
