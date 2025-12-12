package com.ryuqq.setof.application.refundpolicy.port.in.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;

/**
 * 환불 정책 등록 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterRefundPolicyUseCase {

    /**
     * 환불 정책 등록
     *
     * @param command 등록 커맨드
     * @return 생성된 환불 정책 ID
     */
    Long execute(RegisterRefundPolicyCommand command);
}
