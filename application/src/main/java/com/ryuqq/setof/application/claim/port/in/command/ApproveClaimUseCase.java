package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.ApproveClaimCommand;

/**
 * ApproveClaimUseCase - 클레임 승인 UseCase
 *
 * <p>관리자/셀러가 클레임 요청을 승인합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ApproveClaimUseCase {

    /**
     * 클레임 승인
     *
     * @param command 클레임 승인 Command
     */
    void approve(ApproveClaimCommand command);
}
