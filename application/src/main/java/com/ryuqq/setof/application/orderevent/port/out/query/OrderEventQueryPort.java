package com.ryuqq.setof.application.orderevent.port.out.query;

import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import java.util.List;

/**
 * OrderEventQueryPort - OrderEvent Query Port
 *
 * <p>OrderEvent 조회를 위한 Outbound Port입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface OrderEventQueryPort {

    /**
     * 주문 ID로 이벤트 목록 조회 (시간순 오름차순)
     *
     * @param orderId 주문 ID (Value Object)
     * @return 이벤트 목록
     */
    List<OrderEvent> findByOrderId(OrderId orderId);

    /**
     * 주문 ID로 이벤트 목록 조회 (시간순 내림차순 - 최신순)
     *
     * @param orderId 주문 ID (Value Object)
     * @return 이벤트 목록
     */
    List<OrderEvent> findByOrderIdDesc(OrderId orderId);

    /**
     * 출처 ID로 이벤트 목록 조회
     *
     * @param eventSource 이벤트 출처 (CLAIM 등)
     * @param sourceId 출처 ID
     * @return 이벤트 목록
     */
    List<OrderEvent> findBySourceId(String eventSource, String sourceId);
}
