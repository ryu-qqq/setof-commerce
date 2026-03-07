package com.ryuqq.setof.application.shippingaddress.manager;

import com.ryuqq.setof.application.shippingaddress.port.out.command.ShippingAddressCommandPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressCommandManager - 배송지 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressCommandManager {

    private final ShippingAddressCommandPort commandPort;

    public ShippingAddressCommandManager(ShippingAddressCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(ShippingAddress shippingAddress) {
        return commandPort.persist(shippingAddress);
    }

    public void persistAll(List<ShippingAddress> shippingAddresses) {
        for (ShippingAddress address : shippingAddresses) {
            commandPort.persist(address);
        }
    }
}
