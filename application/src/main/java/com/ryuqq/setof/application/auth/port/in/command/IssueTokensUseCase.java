package com.ryuqq.setof.application.auth.port.in.command;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Issue Tokens UseCase
 *
 * <p>토큰 발급 UseCase (Port-In)
 *
 * <p><strong>사용 시점:</strong>
 *
 * <ul>
 *   <li>로그인 성공 후 토큰 발급
 *   <li>OAuth 로그인 후 토큰 발급
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface IssueTokensUseCase {

    /**
     * 토큰 쌍 발급
     *
     * <p>Access Token + Refresh Token을 생성하고 Refresh Token은 저장합니다.
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 발급된 토큰 쌍
     */
    TokenPairResponse execute(String memberId);
}
