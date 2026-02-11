package com.ryuqq.setof.storage.legacy.composite.web.user.adapter;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyRefundAccountResult;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyRefundAccountSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.mapper.LegacyWebRefundAccountMapper;
import com.ryuqq.setof.storage.legacy.composite.web.user.repository.LegacyWebRefundAccountQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 레거시 환불 계좌 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyRefundAccountQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebRefundAccountQueryAdapter {

    private final LegacyWebRefundAccountQueryDslRepository repository;
    private final LegacyWebRefundAccountMapper mapper;

    public LegacyWebRefundAccountQueryAdapter(
            LegacyWebRefundAccountQueryDslRepository repository,
            LegacyWebRefundAccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 사용자의 환불 계좌 조회.
     *
     * @param condition 검색 조건
     * @return 환불 계좌 Optional
     */
    public Optional<LegacyRefundAccountResult> fetchRefundAccount(
            LegacyRefundAccountSearchCondition condition) {
        return repository.fetchRefundAccount(condition).map(mapper::toResult);
    }
}
