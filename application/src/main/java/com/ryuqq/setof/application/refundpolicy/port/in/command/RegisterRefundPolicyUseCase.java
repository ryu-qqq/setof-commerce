package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;

/** 환불정책 등록 UseCase. */
public interface RegisterRefundPolicyUseCase {

    Long execute(RegisterRefundPolicyCommand command);
}
