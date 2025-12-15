package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;

/**
 * 배송 정책 등록 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterShippingPolicyUseCase {

    /**
     * 배송 정책 등록
     *
     * @param command 등록 커맨드
     * @return 생성된 배송 정책 ID
     */
    Long execute(RegisterShippingPolicyCommand command);
}
