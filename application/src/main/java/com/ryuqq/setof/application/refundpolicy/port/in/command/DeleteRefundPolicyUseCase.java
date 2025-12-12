package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;

/**
 * 환불 정책 삭제 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteRefundPolicyUseCase {

    /**
     * 환불 정책 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteRefundPolicyCommand command);
}
