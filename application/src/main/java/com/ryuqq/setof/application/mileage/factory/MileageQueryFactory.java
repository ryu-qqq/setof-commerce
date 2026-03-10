package com.ryuqq.setof.application.mileage.factory;

import com.ryuqq.setof.application.mileage.dto.query.MileageHistorySearchParams;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import org.springframework.stereotype.Component;

/**
 * MileageQueryFactory - 마일리지 검색 기준 생성 팩토리.
 *
 * <p>APP-FAC-001: Factory는 도메인 객체 생성 책임만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MileageQueryFactory {

    /**
     * MileageHistorySearchParams → MileageHistorySearchCriteria 변환.
     *
     * @param params 검색 파라미터
     * @return 도메인 검색 기준
     */
    public MileageHistorySearchCriteria createCriteria(MileageHistorySearchParams params) {
        return MileageHistorySearchCriteria.of(
                params.userId(), params.reasons(), params.page(), params.size());
    }
}
