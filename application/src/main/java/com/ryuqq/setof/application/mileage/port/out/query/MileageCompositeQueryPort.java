package com.ryuqq.setof.application.mileage.port.out.query;

import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.util.List;

/**
 * MileageCompositeQueryPort - 마일리지 복합 조회 Outbound Port.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MileageCompositeQueryPort {

    List<MileageHistoryItemResult> fetchMileageHistories(MileageHistorySearchCriteria criteria);

    long countMileageHistories(MileageHistorySearchCriteria criteria);

    MileageSummary fetchMileageSummary(long userId);
}
