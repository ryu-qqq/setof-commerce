package com.ryuqq.setof.application.refundaccount.port.in.command;

import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;

/**
 * 환불 계좌 등록 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RegisterRefundAccountUseCase {

    Long execute(RegisterRefundAccountCommand command);
}
