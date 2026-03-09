package com.ryuqq.setof.application.order.port.out.query;

import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import java.util.List;

/**
 * OrderDetailQueryPort - 주문 ID 조회 Port-Out.
 *
 * <p>커서 기반 페이징을 위한 주문 ID 목록 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface OrderDetailQueryPort {

    /**
     * 주문 ID 목록 조회 (커서 기반 페이징).
     *
     * @param criteria 검색 조건
     * @return 주문 ID 목록
     */
    List<Long> fetchOrderIds(OrderSearchCriteria criteria);
}
