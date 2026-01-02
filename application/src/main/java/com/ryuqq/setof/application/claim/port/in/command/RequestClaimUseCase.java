package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.RequestClaimCommand;

/**
 * RequestClaimUseCase - 클레임 요청 UseCase
 *
 * <p>고객이 취소/반품/교환/부분환불을 요청합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface RequestClaimUseCase {

    /**
     * 클레임 요청
     *
     * @param command 클레임 요청 Command
     * @return 생성된 클레임 ID
     */
    String request(RequestClaimCommand command);
}
