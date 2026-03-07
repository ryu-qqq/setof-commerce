package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;

/**
 * 배송지 등록 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterShippingAddressUseCase {

    Long execute(RegisterShippingAddressCommand command);
}
