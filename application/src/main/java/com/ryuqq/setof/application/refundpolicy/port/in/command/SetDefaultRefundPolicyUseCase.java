package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;

/**
 * 기본 환불 정책 설정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SetDefaultRefundPolicyUseCase {

    /**
     * 기본 환불 정책 설정
     *
     * @param command 기본 정책 설정 커맨드
     */
    void execute(SetDefaultRefundPolicyCommand command);
}
