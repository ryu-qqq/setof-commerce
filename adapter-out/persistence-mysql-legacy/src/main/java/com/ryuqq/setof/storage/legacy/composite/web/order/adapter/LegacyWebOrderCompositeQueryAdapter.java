package com.ryuqq.setof.storage.legacy.composite.web.order.adapter;

import com.ryuqq.setof.application.legacy.order.dto.response.LegacyOrderResult;
import com.ryuqq.setof.domain.legacy.order.dto.query.LegacyOrderSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.order.dto.LegacyWebOrderQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.order.dto.LegacyWebOrderStatusCountDto;
import com.ryuqq.setof.storage.legacy.composite.web.order.mapper.LegacyWebOrderMapper;
import com.ryuqq.setof.storage.legacy.composite.web.order.repository.LegacyWebOrderCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebOrderCompositeQueryAdapter - 레거시 주문 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyOrderCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebOrderCompositeQueryAdapter {

    private final LegacyWebOrderCompositeQueryDslRepository repository;
    private final LegacyWebOrderMapper mapper;

    public LegacyWebOrderCompositeQueryAdapter(
            LegacyWebOrderCompositeQueryDslRepository repository, LegacyWebOrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 주문 목록 조회.
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 목록
     */
    public List<LegacyOrderResult> fetchOrders(LegacyOrderSearchCondition condition, int limit) {
        List<LegacyWebOrderQueryDto> dtos = repository.fetchOrders(condition, limit);
        return mapper.toResults(dtos);
    }

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징용).
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 주문 ID 목록
     */
    public List<Long> fetchOrderIds(LegacyOrderSearchCondition condition, int limit) {
        return repository.fetchOrderIds(condition, limit);
    }

    /**
     * 주문 개수 조회.
     *
     * @param condition 검색 조건
     * @return 주문 개수
     */
    public long countOrders(LegacyOrderSearchCondition condition) {
        return repository.countOrders(condition);
    }

    /**
     * 상태별 주문 개수 조회 (최근 3개월).
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return 상태별 주문 개수 목록
     */
    public List<LegacyWebOrderStatusCountDto> countOrdersByStatus(
            long userId, List<String> orderStatuses) {
        return repository.countOrdersByStatus(userId, orderStatuses);
    }
}
