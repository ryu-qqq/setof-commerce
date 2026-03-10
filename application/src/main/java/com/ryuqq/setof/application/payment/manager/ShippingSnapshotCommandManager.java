package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.command.ShippingSnapshotCommandPort;
import com.ryuqq.setof.domain.order.aggregate.Order;
import org.springframework.stereotype.Component;

/**
 * ShippingSnapshotCommandManager - 배송지 스냅샷 저장 Manager.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingSnapshotCommandManager {

    private final ShippingSnapshotCommandPort shippingSnapshotCommandPort;

    public ShippingSnapshotCommandManager(ShippingSnapshotCommandPort shippingSnapshotCommandPort) {
        this.shippingSnapshotCommandPort = shippingSnapshotCommandPort;
    }

    public void persist(Order order) {
        shippingSnapshotCommandPort.persist(order);
    }
}
