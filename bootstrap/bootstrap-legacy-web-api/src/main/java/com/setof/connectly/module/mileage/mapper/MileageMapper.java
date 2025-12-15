package com.setof.connectly.module.mileage.mapper;

import com.setof.connectly.module.mileage.dto.MileageDto;
import com.setof.connectly.module.mileage.entity.Mileage;
import com.setof.connectly.module.mileage.entity.MileageHistory;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import com.setof.connectly.module.mileage.enums.Reason;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
import com.setof.connectly.module.user.entity.Users;

public interface MileageMapper {

    MileageTransaction toMileageTransaction(
            long userId,
            MileageIssueType issueType,
            long targetId,
            double mileageAmount,
            MileageStatus mileageStatus);

    MileageHistory toMileageHistory(
            long userId,
            long targetId,
            Reason reason,
            MileageIssueType issueType,
            MileageDto mileages);

    MileageHistory toMileageHistory(Mileage mileage);

    PaymentSnapShotMileage toSnapShot(
            long userId,
            long paymentId,
            double usedMileageAmount,
            double currentUsingMileageAmount);

    Mileage joinMileageForOpenEvent(Users users);
}
