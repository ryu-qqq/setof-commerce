package com.ryuqq.setof.application.mileage.port.in.query;

import com.ryuqq.setof.application.mileage.dto.query.MileageHistorySearchParams;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryPageResult;

/**
 * GetMileageHistoriesUseCase - 마일리지 이력 페이지 조회 UseCase.
 *
 * <p>APP-PORT-001: Inbound Port는 UseCase 네이밍.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetMileageHistoriesUseCase {

    /**
     * 마일리지 이력 페이지 조회 실행.
     *
     * @param params 검색 파라미터
     * @return 마일리지 이력 페이지 결과
     */
    MileageHistoryPageResult execute(MileageHistorySearchParams params);
}
