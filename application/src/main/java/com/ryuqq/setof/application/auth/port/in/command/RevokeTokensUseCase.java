package com.ryuqq.setof.application.auth.port.in.command;

/**
 * Revoke Tokens UseCase
 *
 * <p>토큰 무효화 UseCase (Port-In)
 *
 * <p><strong>사용 시점:</strong>
 *
 * <ul>
 *   <li>로그아웃 시 회원의 모든 토큰 무효화
 *   <li>회원 탈퇴 시 모든 토큰 무효화
 *   <li>특정 토큰만 무효화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RevokeTokensUseCase {

    /**
     * 회원의 모든 Refresh Token 무효화
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    void revokeByMemberId(String memberId);

    /**
     * 특정 Refresh Token 무효화
     *
     * @param refreshToken Refresh Token 값
     */
    void revokeByToken(String refreshToken);
}
