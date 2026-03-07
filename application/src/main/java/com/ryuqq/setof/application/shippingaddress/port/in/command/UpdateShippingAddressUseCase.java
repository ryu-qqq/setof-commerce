package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;

/**
 * 배송지 수정 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateShippingAddressUseCase {

    void execute(UpdateShippingAddressCommand command);
}
