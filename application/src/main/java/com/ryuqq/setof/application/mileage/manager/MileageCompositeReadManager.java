package com.ryuqq.setof.application.mileage.manager;

import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageSummaryResult;
import com.ryuqq.setof.application.mileage.port.out.query.MileageCompositeQueryPort;
import com.ryuqq.setof.domain.mileage.query.MileageHistorySearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MileageCompositeReadManager - 마일리지 복합 조회 ReadManager.
 *
 * <p>APP-MGR-001: ReadManager는 Port를 통해 데이터 조회만 담당.
 *
 * <p>APP-TRX-002: 읽기 전용 트랜잭션 사용 (@Transactional(readOnly = true)).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MileageCompositeReadManager {

    private final MileageCompositeQueryPort compositeQueryPort;

    public MileageCompositeReadManager(MileageCompositeQueryPort compositeQueryPort) {
        this.compositeQueryPort = compositeQueryPort;
    }

    /**
     * 마일리지 이력 목록 조회.
     *
     * @param criteria 검색 기준
     * @return 마일리지 이력 목록
     */
    @Transactional(readOnly = true)
    public List<MileageHistoryItemResult> fetchMileageHistories(
            MileageHistorySearchCriteria criteria) {
        return compositeQueryPort.fetchMileageHistories(criteria);
    }

    /**
     * 마일리지 이력 카운트 조회.
     *
     * @param criteria 검색 기준
     * @return 전체 이력 개수
     */
    @Transactional(readOnly = true)
    public long countMileageHistories(MileageHistorySearchCriteria criteria) {
        return compositeQueryPort.countMileageHistories(criteria);
    }

    /**
     * 사용자 마일리지 요약 조회.
     *
     * @param userId 사용자 ID
     * @return 마일리지 요약 결과
     */
    @Transactional(readOnly = true)
    public MileageSummaryResult fetchMileageSummary(long userId) {
        return compositeQueryPort.fetchMileageSummary(userId);
    }
}
