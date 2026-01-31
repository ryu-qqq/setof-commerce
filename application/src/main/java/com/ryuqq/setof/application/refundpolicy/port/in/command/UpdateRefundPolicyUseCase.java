package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;

/** 환불정책 수정 UseCase. */
public interface UpdateRefundPolicyUseCase {

    void execute(UpdateRefundPolicyCommand command);
}
