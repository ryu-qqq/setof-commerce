package com.ryuqq.setof.application.mileage.port.out.query;

import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageSummaryResult;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import java.util.List;

/**
 * MileageCompositeQueryPort - 마일리지 복합 조회 Outbound Port.
 *
 * <p>APP-PORT-002: Outbound Port는 application 레이어에 선언, adapter-out에서 구현.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface MileageCompositeQueryPort {

    /**
     * 마일리지 이력 목록 조회.
     *
     * @param criteria 검색 기준
     * @return 마일리지 이력 목록
     */
    List<MileageHistoryItemResult> fetchMileageHistories(MileageHistorySearchCriteria criteria);

    /**
     * 마일리지 이력 카운트 조회.
     *
     * @param criteria 검색 기준
     * @return 전체 이력 개수
     */
    long countMileageHistories(MileageHistorySearchCriteria criteria);

    /**
     * 사용자 마일리지 요약 조회.
     *
     * @param userId 사용자 ID
     * @return 마일리지 요약 결과
     */
    MileageSummaryResult fetchMileageSummary(long userId);
}
