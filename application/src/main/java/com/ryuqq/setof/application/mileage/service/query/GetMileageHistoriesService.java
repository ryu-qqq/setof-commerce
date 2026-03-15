package com.ryuqq.setof.application.mileage.service.query;

import com.ryuqq.setof.application.mileage.assembler.MileageAssembler;
import com.ryuqq.setof.application.mileage.dto.query.MileageHistorySearchParams;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryPageResult;
import com.ryuqq.setof.application.mileage.factory.MileageQueryFactory;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.application.mileage.port.in.query.GetMileageHistoriesUseCase;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetMileageHistoriesService - 마일리지 이력 페이지 조회 서비스.
 *
 * <p>APP-SVC-001: Service는 UseCase를 구현하며 비즈니스 흐름을 조율.
 *
 * <p>APP-SVC-002: Service는 Manager/Factory/Assembler를 조합하여 UseCase를 완성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetMileageHistoriesService implements GetMileageHistoriesUseCase {

    private final MileageCompositeReadManager readManager;
    private final MileageQueryFactory queryFactory;
    private final MileageAssembler assembler;

    public GetMileageHistoriesService(
            MileageCompositeReadManager readManager,
            MileageQueryFactory queryFactory,
            MileageAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public MileageHistoryPageResult execute(MileageHistorySearchParams params) {
        MileageHistorySearchCriteria criteria = queryFactory.createCriteria(params);
        List<MileageHistoryItemResult> histories = readManager.fetchMileageHistories(criteria);
        long totalElements = readManager.countMileageHistories(criteria);
        MileageSummary summary = readManager.getMileageSummary(criteria.userId());
        return assembler.toPageResult(
                criteria.userId(),
                summary,
                histories,
                criteria.page(),
                criteria.size(),
                totalElements);
    }
}
