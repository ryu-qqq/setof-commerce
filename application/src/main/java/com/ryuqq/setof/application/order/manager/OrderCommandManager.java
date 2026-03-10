package com.ryuqq.setof.application.order.manager;

import com.ryuqq.setof.application.order.port.out.command.LegacyOrderCommandPort;
import com.ryuqq.setof.application.order.port.out.query.LegacyOrderQueryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderCommandManager - 주문 명령 Manager.
 *
 * <p>Legacy orders 테이블 상태 변경을 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderCommandManager {

    private final LegacyOrderCommandPort commandPort;
    private final LegacyOrderQueryPort queryPort;

    public OrderCommandManager(LegacyOrderCommandPort commandPort, LegacyOrderQueryPort queryPort) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
    }

    public String fetchCurrentStatus(long orderId) {
        return queryPort.fetchOrderStatus(orderId);
    }

    public long fetchOrderUserId(long orderId) {
        return queryPort.fetchOrderUserId(orderId);
    }

    @Transactional
    public String updateOrderStatus(long orderId, String targetOrderStatus) {
        return commandPort.updateOrderStatus(orderId, targetOrderStatus);
    }
}
