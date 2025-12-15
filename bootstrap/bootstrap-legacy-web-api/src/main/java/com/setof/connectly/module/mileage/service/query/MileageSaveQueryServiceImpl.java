package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;
import com.setof.connectly.module.event.service.mileage.EventMileageFindService;
import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import com.setof.connectly.module.mileage.mapper.MileageMapper;
import com.setof.connectly.module.mileage.repository.query.MileageTransactionJdbcRepository;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import com.setof.connectly.module.utils.NumberUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MileageSaveQueryServiceImpl implements MileageSaveQueryService {

    private final MileageMapper mileageMapper;
    private final MileageTransactionJdbcRepository mileageTransactionJdbcRepository;
    private final EventMileageFindService eventMileageFindService;

    @Override
    public void saveOrderExpectedMileage(
            long userId, long paymentAmount, PendingMileageQueryDto pendingMileageQuery) {

        UserGradeEnum userGrade = pendingMileageQuery.getUserGrade();
        EventMileageDto eventMileage =
                eventMileageFindService.fetchEventMileage(EventMileageType.ORDER);

        double mileageRate = eventMileage.getMileageRate() * userGrade.getMileageReserveRate();

        List<MileageTransaction> mileageTransactions =
                pendingMileageQuery.getOrderAmounts().stream()
                        .map(
                                orderAmountDto -> {
                                    long orderId = orderAmountDto.getOrderId();
                                    double orderAmount =
                                            orderAmountDto.getOrderAmount()
                                                    - orderAmountDto.getRealOrderAmount();
                                    long mileageAmount =
                                            NumberUtils.downDotNumber(mileageRate, orderAmount);

                                    return mileageMapper.toMileageTransaction(
                                            userId,
                                            MileageIssueType.ORDER,
                                            orderId,
                                            mileageAmount,
                                            MileageStatus.PENDING);
                                })
                        .collect(Collectors.toList());

        mileageTransactionJdbcRepository.saveAll(mileageTransactions);
    }
}
