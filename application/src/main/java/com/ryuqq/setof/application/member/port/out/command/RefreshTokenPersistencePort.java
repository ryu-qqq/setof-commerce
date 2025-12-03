package com.ryuqq.setof.application.member.port.out.command;

/**
 * Refresh Token Command Port
 *
 * <p>Refresh Token 저장/삭제를 담당하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenPersistencePort {

    /**
     * Refresh Token 저장
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param refreshToken Refresh Token 값
     * @param expiresInSeconds 만료 시간 (초)
     */
    void save(String memberId, String refreshToken, long expiresInSeconds);

    /**
     * Refresh Token 삭제 (회원 ID 기준)
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    void deleteByMemberId(String memberId);

    /**
     * Refresh Token 삭제 (토큰 값 기준)
     *
     * @param refreshToken Refresh Token 값
     */
    void deleteByToken(String refreshToken);
}
