package com.ryuqq.setof.application.member.port.out.command;

import java.util.Optional;

/**
 * Refresh Token Cache Port
 *
 * <p>Refresh Token 캐시 저장/삭제를 담당하는 Port (Redis 등)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenCachePort {

    /**
     * Refresh Token 캐시 저장
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param refreshToken Refresh Token 값
     * @param expiresInSeconds 만료 시간 (초)
     */
    void save(String memberId, String refreshToken, long expiresInSeconds);

    /**
     * 회원 ID로 Refresh Token 조회
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Refresh Token (Optional)
     */
    Optional<String> findByMemberId(String memberId);

    /**
     * Refresh Token으로 회원 ID 조회
     *
     * @param refreshToken Refresh Token 값
     * @return 회원 ID (UUID 문자열, Optional)
     */
    Optional<String> findMemberIdByToken(String refreshToken);

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
