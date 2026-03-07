package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.internal.ShippingAddressRegistrationCoordinator;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterShippingAddressService - 배송지 등록 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterShippingAddressService implements RegisterShippingAddressUseCase {

    private final ShippingAddressRegistrationCoordinator coordinator;

    public RegisterShippingAddressService(ShippingAddressRegistrationCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public Long execute(RegisterShippingAddressCommand command) {
        return coordinator.register(command);
    }
}
