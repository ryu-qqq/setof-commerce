package com.ryuqq.setof.application.order.internal;

import com.ryuqq.setof.application.order.manager.OrderCompositeReadManager;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderReadFacade - 주문 Read Facade.
 *
 * <p>OrderReadManager와 OrderCompositeReadManager를 조합하여 주문 조회 결과를 반환합니다. 조립(Assembling)은 Service에서
 * Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderReadFacade {

    private final OrderReadManager orderReadManager;
    private final OrderCompositeReadManager orderCompositeReadManager;

    public OrderReadFacade(
            OrderReadManager orderReadManager,
            OrderCompositeReadManager orderCompositeReadManager) {
        this.orderReadManager = orderReadManager;
        this.orderCompositeReadManager = orderCompositeReadManager;
    }

    /**
     * 커서 기반 주문 ID 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 주문 ID 목록 (fetchSize = size + 1)
     */
    @Transactional(readOnly = true)
    public List<Long> fetchOrderIds(OrderSearchCriteria criteria) {
        return orderReadManager.fetchOrderIds(criteria);
    }

    /**
     * 주문 ID 목록으로 주문 상세 Composite 조회.
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<OrderDetail> fetchOrderDetails(List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return List.of();
        }
        return orderCompositeReadManager.fetchOrderDetails(orderIds);
    }

    /**
     * 상태별 주문 건수 조회.
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return 상태별 건수 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<OrderStatusCount> countByStatus(long userId, List<String> orderStatuses) {
        return orderReadManager.countByStatus(userId, orderStatuses);
    }
}
