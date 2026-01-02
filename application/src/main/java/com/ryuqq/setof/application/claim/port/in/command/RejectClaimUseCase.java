package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.RejectClaimCommand;

/**
 * RejectClaimUseCase - 클레임 반려 UseCase
 *
 * <p>관리자/셀러가 클레임 요청을 반려합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface RejectClaimUseCase {

    /**
     * 클레임 반려
     *
     * @param command 클레임 반려 Command
     */
    void reject(RejectClaimCommand command);
}
