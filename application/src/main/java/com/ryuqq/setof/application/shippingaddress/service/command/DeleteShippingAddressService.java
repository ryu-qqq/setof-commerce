package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.internal.ShippingAddressUpdateCoordinator;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import org.springframework.stereotype.Service;

/**
 * DeleteShippingAddressService - 배송지 삭제 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class DeleteShippingAddressService implements DeleteShippingAddressUseCase {

    private final ShippingAddressUpdateCoordinator coordinator;

    public DeleteShippingAddressService(ShippingAddressUpdateCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void execute(DeleteShippingAddressCommand command) {
        coordinator.delete(command);
    }
}
