package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;

/**
 * 배송지 삭제 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DeleteShippingAddressUseCase {

    void execute(DeleteShippingAddressCommand command);
}
