package com.ryuqq.setof.storage.legacy.composite.web.cart.adapter;

import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartCountResult;
import com.ryuqq.setof.application.legacy.cart.dto.response.LegacyCartResult;
import com.ryuqq.setof.domain.legacy.cart.dto.query.LegacyCartSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.cart.mapper.LegacyWebCartMapper;
import com.ryuqq.setof.storage.legacy.composite.web.cart.repository.LegacyWebCartCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCartCompositeQueryAdapter - 레거시 장바구니 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyCartCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCartCompositeQueryAdapter {

    private final LegacyWebCartCompositeQueryDslRepository repository;
    private final LegacyWebCartMapper mapper;

    public LegacyWebCartCompositeQueryAdapter(
            LegacyWebCartCompositeQueryDslRepository repository, LegacyWebCartMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 장바구니 목록 조회.
     *
     * @param condition 검색 조건
     * @return 장바구니 목록
     */
    public List<LegacyCartResult> fetchCarts(LegacyCartSearchCondition condition) {
        List<LegacyWebCartQueryDto> dtos = repository.fetchCarts(condition);
        return mapper.toResults(dtos);
    }

    /**
     * 장바구니 개수 조회 (사용자별).
     *
     * @param userId 사용자 ID
     * @return 장바구니 개수
     */
    public long countCarts(Long userId) {
        return repository.countCarts(userId);
    }

    /**
     * 장바구니 개수 조회 (카운트 API용).
     *
     * @param userId 사용자 ID
     * @return LegacyCartCountResult
     */
    public LegacyCartCountResult fetchCartCount(Long userId) {
        long count = repository.countCarts(userId);
        return mapper.toCountResult(userId, count);
    }

    /**
     * 장바구니 총 개수 조회 (목록 조회 시 totalElements 용).
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    public long countCartsForUser(LegacyCartSearchCondition condition) {
        return repository.countCartsForUser(condition);
    }
}
