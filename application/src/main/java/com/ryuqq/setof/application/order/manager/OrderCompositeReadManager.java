package com.ryuqq.setof.application.order.manager;

import com.ryuqq.setof.application.order.port.out.query.OrderCompositeQueryPort;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderCompositeReadManager - 주문 Composite 조회 Manager.
 *
 * <p>다중 테이블 JOIN이 필요한 주문 상세 조회를 래핑하여 트랜잭션 경계를 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderCompositeReadManager {

    private final OrderCompositeQueryPort orderCompositeQueryPort;

    public OrderCompositeReadManager(OrderCompositeQueryPort orderCompositeQueryPort) {
        this.orderCompositeQueryPort = orderCompositeQueryPort;
    }

    /**
     * 주문 ID 목록으로 주문 상세 Composite 조회.
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록 (도메인 VO)
     */
    @Transactional(readOnly = true)
    public List<OrderDetail> fetchOrderDetails(List<Long> orderIds) {
        return orderCompositeQueryPort.fetchOrderDetails(orderIds);
    }
}
