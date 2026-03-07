package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.internal.ShippingAddressUpdateCoordinator;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import org.springframework.stereotype.Service;

/**
 * UpdateShippingAddressService - 배송지 수정 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateShippingAddressService implements UpdateShippingAddressUseCase {

    private final ShippingAddressUpdateCoordinator coordinator;

    public UpdateShippingAddressService(ShippingAddressUpdateCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateShippingAddressCommand command) {
        coordinator.update(command);
    }
}
