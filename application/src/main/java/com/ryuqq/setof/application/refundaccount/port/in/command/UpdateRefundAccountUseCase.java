package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;

/**
 * 환불 계좌 수정 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateRefundAccountUseCase {

    void execute(UpdateRefundAccountCommand command);
}
