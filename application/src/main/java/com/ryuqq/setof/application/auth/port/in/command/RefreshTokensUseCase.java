package com.ryuqq.setof.application.auth.port.in.command;

import com.ryuqq.setof.application.auth.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Refresh Tokens UseCase
 *
 * <p>토큰 갱신 UseCase (Port-In)
 *
 * <p><strong>처리 순서:</strong>
 *
 * <ol>
 *   <li>Refresh Token 유효성 검증
 *   <li>기존 Refresh Token 무효화
 *   <li>새 토큰 쌍 발급
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokensUseCase {

    /**
     * 토큰 갱신
     *
     * @param command Refresh Token 갱신 요청
     * @return 새로 발급된 토큰 쌍
     */
    TokenPairResponse execute(RefreshTokenCommand command);
}
