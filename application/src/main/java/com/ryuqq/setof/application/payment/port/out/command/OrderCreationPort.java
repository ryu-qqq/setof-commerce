package com.ryuqq.setof.application.payment.port.out.command;

import com.ryuqq.setof.domain.order.aggregate.Order;
import java.util.List;

/**
 * 주문 영속화 Port (결제 시 주문 생성 전용).
 *
 * <p>Order 도메인 객체를 영속화합니다. Order는 paymentId를 이미 보유합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface OrderCreationPort {

    /**
     * 주문을 영속화합니다.
     *
     * @param order 주문 도메인 객체 (paymentId 할당 완료 상태)
     * @return 생성된 주문 아이템 ID 목록
     */
    List<Long> persist(Order order);
}
