package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;

/**
 * 환불 계좌 삭제 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DeleteRefundAccountUseCase {

    void execute(DeleteRefundAccountCommand command);
}
