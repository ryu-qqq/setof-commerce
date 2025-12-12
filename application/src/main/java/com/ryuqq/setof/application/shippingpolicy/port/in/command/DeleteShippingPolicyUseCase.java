package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;

/**
 * 배송 정책 삭제 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteShippingPolicyUseCase {

    /**
     * 배송 정책 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteShippingPolicyCommand command);
}
