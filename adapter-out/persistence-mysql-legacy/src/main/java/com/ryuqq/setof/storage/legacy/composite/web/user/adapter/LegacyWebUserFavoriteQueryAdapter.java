package com.ryuqq.setof.storage.legacy.composite.web.user.adapter;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyUserFavoriteSliceResult;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyUserFavoriteSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebUserFavoriteQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.user.mapper.LegacyWebUserFavoriteMapper;
import com.ryuqq.setof.storage.legacy.composite.web.user.repository.LegacyWebUserFavoriteQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 찜 목록 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyUserFavoriteQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserFavoriteQueryAdapter {

    private final LegacyWebUserFavoriteQueryDslRepository repository;
    private final LegacyWebUserFavoriteMapper mapper;

    public LegacyWebUserFavoriteQueryAdapter(
            LegacyWebUserFavoriteQueryDslRepository repository,
            LegacyWebUserFavoriteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 찜 목록 조회 (커서 기반 페이징).
     *
     * @param condition 검색 조건
     * @return 찜 목록 Slice 결과
     */
    public LegacyUserFavoriteSliceResult fetchUserFavorites(
            LegacyUserFavoriteSearchCondition condition) {
        List<LegacyWebUserFavoriteQueryDto> dtos = repository.fetchUserFavorites(condition);
        long totalElements = repository.countUserFavorites(condition);
        return mapper.toSliceResult(dtos, condition.pageSize(), totalElements);
    }

    /**
     * 찜 총 개수 조회.
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long countUserFavorites(LegacyUserFavoriteSearchCondition condition) {
        return repository.countUserFavorites(condition);
    }
}
