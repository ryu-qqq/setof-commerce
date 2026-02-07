package com.ryuqq.setof.storage.legacy.composite.web.mileage.adapter;

import com.ryuqq.setof.application.legacy.mileage.dto.response.LegacyMileageHistoryResult;
import com.ryuqq.setof.application.legacy.mileage.dto.response.LegacyMileagePageResult;
import com.ryuqq.setof.application.legacy.mileage.dto.response.LegacyUserMileageResult;
import com.ryuqq.setof.domain.legacy.mileage.dto.query.LegacyMileageHistorySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.mileage.dto.LegacyWebMileageHistoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.mileage.dto.LegacyWebUserMileageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.mileage.mapper.LegacyWebMileageMapper;
import com.ryuqq.setof.storage.legacy.composite.web.mileage.repository.LegacyWebMileageCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Mileage Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyWebMileageCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebMileageCompositeQueryAdapter {

    private final LegacyWebMileageCompositeQueryDslRepository repository;
    private final LegacyWebMileageMapper mapper;

    public LegacyWebMileageCompositeQueryAdapter(
            LegacyWebMileageCompositeQueryDslRepository repository, LegacyWebMileageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 마일리지 이력 페이지 조회.
     *
     * <p>fetchMyMileageHistories 대응.
     *
     * <p>마일리지 요약 + 이력 목록을 함께 반환.
     *
     * @param condition 검색 조건
     * @return 마일리지 페이지 결과
     */
    public LegacyMileagePageResult fetchMileageHistories(
            LegacyMileageHistorySearchCondition condition) {

        List<LegacyWebMileageHistoryQueryDto> historyDtos =
                repository.fetchMileageHistories(condition);

        if (historyDtos.isEmpty()) {
            LegacyUserMileageResult emptyUserMileage = fetchUserMileage(condition.userId(), 0.0);
            return LegacyMileagePageResult.of(
                    emptyUserMileage, List.of(), condition.pageNumber(), condition.pageSize(), 0L);
        }

        List<LegacyMileageHistoryResult> histories = mapper.toHistoryResults(historyDtos);

        long totalElements = repository.countMileageHistories(condition);
        LegacyUserMileageResult userMileage = fetchUserMileage(condition.userId(), 0.0);

        return LegacyMileagePageResult.of(
                userMileage,
                histories,
                condition.pageNumber(),
                condition.pageSize(),
                totalElements);
    }

    /**
     * 마일리지 이력 목록 조회 (페이징 없이).
     *
     * @param condition 검색 조건
     * @return 마일리지 이력 목록
     */
    public List<LegacyMileageHistoryResult> fetchMileageHistoriesOnly(
            LegacyMileageHistorySearchCondition condition) {

        List<LegacyWebMileageHistoryQueryDto> dtos = repository.fetchMileageHistories(condition);
        return mapper.toHistoryResults(dtos);
    }

    /**
     * 사용자 마일리지 요약 조회.
     *
     * @param userId 사용자 ID
     * @param pendingMileages 적립 예정 마일리지 (외부에서 계산)
     * @return 사용자 마일리지 요약
     */
    public LegacyUserMileageResult fetchUserMileage(long userId, double pendingMileages) {
        List<LegacyWebUserMileageQueryDto> mileageDtos = repository.fetchUserMileages(userId);
        return mapper.toUserMileageResult(userId, mileageDtos, pendingMileages);
    }

    /**
     * 마일리지 이력 카운트 조회.
     *
     * @param condition 검색 조건
     * @return 이력 카운트
     */
    public long countMileageHistories(LegacyMileageHistorySearchCondition condition) {
        return repository.countMileageHistories(condition);
    }
}
