package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;

/**
 * 배송 정책 수정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateShippingPolicyUseCase {

    /**
     * 배송 정책 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateShippingPolicyCommand command);
}
