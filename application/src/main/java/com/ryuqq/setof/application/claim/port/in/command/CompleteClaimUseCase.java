package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.CompleteClaimCommand;

/**
 * CompleteClaimUseCase - 클레임 완료 처리 UseCase
 *
 * <p>클레임 처리가 완료되면 호출합니다. (환불 완료, 교환품 발송 완료 등)
 *
 * @author development-team
 * @since 2.0.0
 */
public interface CompleteClaimUseCase {

    /**
     * 클레임 완료 처리
     *
     * @param command 클레임 완료 Command
     */
    void complete(CompleteClaimCommand command);
}
