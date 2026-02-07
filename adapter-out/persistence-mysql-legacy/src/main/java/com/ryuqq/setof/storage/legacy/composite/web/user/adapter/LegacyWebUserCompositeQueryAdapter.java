package com.ryuqq.setof.storage.legacy.composite.web.user.adapter;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyJoinedUserResult;
import com.ryuqq.setof.application.legacy.user.dto.response.LegacyMyPageResult;
import com.ryuqq.setof.application.legacy.user.dto.response.LegacyUserResult;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyMyPageSearchCondition;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyUserSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.mapper.LegacyWebUserMapper;
import com.ryuqq.setof.storage.legacy.composite.web.user.repository.LegacyWebUserCompositeQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 레거시 사용자 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyUserCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserCompositeQueryAdapter {

    private final LegacyWebUserCompositeQueryDslRepository repository;
    private final LegacyWebUserMapper mapper;

    public LegacyWebUserCompositeQueryAdapter(
            LegacyWebUserCompositeQueryDslRepository repository, LegacyWebUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 사용자 정보 조회 (fetchUser용).
     *
     * @param condition 검색 조건
     * @return 사용자 정보 Optional
     */
    public Optional<LegacyUserResult> fetchUser(LegacyUserSearchCondition condition) {
        return repository.fetchUser(condition).map(mapper::toResult);
    }

    /**
     * 가입 사용자 조회 (isExistUser용).
     *
     * @param condition 검색 조건
     * @return 가입 사용자 정보 Optional
     */
    public Optional<LegacyJoinedUserResult> fetchJoinedUser(LegacyUserSearchCondition condition) {
        return repository.fetchJoinedUser(condition).map(mapper::toJoinedResult);
    }

    /**
     * 마이페이지 정보 조회.
     *
     * @param condition 검색 조건
     * @return 마이페이지 정보 Optional
     */
    public Optional<LegacyMyPageResult> fetchMyPage(LegacyMyPageSearchCondition condition) {
        return repository.fetchMyPage(condition).map(mapper::toMyPageResult);
    }
}
