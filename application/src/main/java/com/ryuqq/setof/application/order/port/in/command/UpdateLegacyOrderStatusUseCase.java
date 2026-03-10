package com.ryuqq.setof.application.order.port.in.command;

import com.ryuqq.setof.application.order.dto.command.UpdateLegacyOrderStatusCommand;
import com.ryuqq.setof.application.order.dto.response.UpdateOrderResult;

/**
 * Legacy 주문 상태 변경 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateLegacyOrderStatusUseCase {

    UpdateOrderResult execute(UpdateLegacyOrderStatusCommand command);
}
