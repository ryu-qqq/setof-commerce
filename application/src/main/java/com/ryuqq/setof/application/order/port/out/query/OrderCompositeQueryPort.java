package com.ryuqq.setof.application.order.port.out.query;

import com.ryuqq.setof.domain.order.vo.OrderDetail;
import java.util.List;

/**
 * OrderCompositeQueryPort - 주문 Composite 조회 Port-Out.
 *
 * <p>다중 테이블 JOIN이 필요한 주문 상세 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface OrderCompositeQueryPort {

    /**
     * 주문 ID 목록으로 주문 상세 조회 (Composite JOIN).
     *
     * @param orderIds 주문 ID 목록
     * @return 주문 상세 목록 (도메인 VO)
     */
    List<OrderDetail> fetchOrderDetails(List<Long> orderIds);
}
