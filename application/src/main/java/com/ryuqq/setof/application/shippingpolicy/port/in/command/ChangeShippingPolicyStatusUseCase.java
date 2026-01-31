package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.ChangeShippingPolicyStatusCommand;

/** 배송정책 상태 변경 UseCase. */
public interface ChangeShippingPolicyStatusUseCase {

    void execute(ChangeShippingPolicyStatusCommand command);
}
