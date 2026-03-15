package com.ryuqq.setof.adapter.out.persistence.mileage.adapter;

import com.ryuqq.setof.adapter.out.persistence.mileage.repository.MileageCompositeQueryDslRepository;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.port.out.query.MileageCompositeQueryPort;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MileageCompositeQueryAdapter - 마일리지 Composite 조회 Adapter.
 *
 * <p>새 스키마(setof) 기반 마일리지 복합 조회 구현.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Port 인터페이스 구현.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MileageCompositeQueryAdapter implements MileageCompositeQueryPort {

    private final MileageCompositeQueryDslRepository compositeRepository;

    public MileageCompositeQueryAdapter(MileageCompositeQueryDslRepository compositeRepository) {
        this.compositeRepository = compositeRepository;
    }

    @Override
    public List<MileageHistoryItemResult> fetchMileageHistories(
            MileageHistorySearchCriteria criteria) {
        return compositeRepository.fetchMileageHistories(criteria);
    }

    @Override
    public long countMileageHistories(MileageHistorySearchCriteria criteria) {
        return compositeRepository.countMileageHistories(criteria);
    }

    @Override
    public MileageSummary fetchMileageSummary(long userId) {
        return compositeRepository.fetchMileageSummary(userId);
    }
}
