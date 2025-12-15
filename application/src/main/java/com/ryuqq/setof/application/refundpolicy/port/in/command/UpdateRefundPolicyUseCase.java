package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;

/**
 * 환불 정책 수정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateRefundPolicyUseCase {

    /**
     * 환불 정책 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateRefundPolicyCommand command);
}
