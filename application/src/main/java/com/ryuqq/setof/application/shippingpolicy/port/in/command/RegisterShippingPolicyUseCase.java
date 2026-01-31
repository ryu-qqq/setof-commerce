package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;

/** 배송정책 등록 UseCase. */
public interface RegisterShippingPolicyUseCase {

    Long execute(RegisterShippingPolicyCommand command);
}
