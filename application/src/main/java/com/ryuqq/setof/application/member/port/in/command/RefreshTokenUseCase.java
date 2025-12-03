package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.member.dto.response.RefreshTokenResponse;

/**
 * Refresh Token UseCase (Command)
 *
 * <p>토큰 갱신을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>Refresh Token 검증 (Redis)
 *   <li>새 Access/Refresh Token 발급
 *   <li>기존 Refresh Token 무효화
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenUseCase {

    /**
     * 토큰 갱신 실행
     *
     * @param command 토큰 갱신 명령
     * @return 토큰 갱신 결과 (tokens)
     */
    RefreshTokenResponse execute(RefreshTokenCommand command);
}
