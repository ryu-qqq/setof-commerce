package com.setof.connectly.module.mileage.service.fetch;

import com.setof.connectly.module.exception.mileage.MileageNotFoundException;
import com.setof.connectly.module.mileage.dto.MileageQueryDto;
import com.setof.connectly.module.mileage.dto.MileageRefundQueryDto;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.repository.fetch.MileageFindRepository;
import com.setof.connectly.module.mileage.repository.fetch.MileageTransactionFindRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MileageFindServiceImpl implements MileageFindService {
    private final MileageFindRepository mileageFindRepository;
    private final MileageTransactionFindRepository mileageTransactionFindRepository;

    @Override
    public Optional<MileageQueryDto> fetchUsableMileage(long userId) {
        return mileageFindRepository.fetchMileages(userId);
    }

    @Override
    public MileageRefundQueryDto fetchUsedMileage(long paymentId, long orderId, long userId) {
        return mileageFindRepository
                .fetchUsedMileage(paymentId, orderId, userId)
                .orElseThrow(MileageNotFoundException::new);
    }

    @Override
    public Optional<MileageTransaction> fetchMileageTransactionEntity(long orderId) {
        return mileageTransactionFindRepository.fetchMileageTransactionEntity(orderId);
    }
}
