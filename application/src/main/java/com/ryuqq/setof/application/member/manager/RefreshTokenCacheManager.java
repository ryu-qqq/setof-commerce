package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.command.RefreshTokenCachePort;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Cache Manager
 *
 * <p>Refresh Token 캐시(Redis) 저장/조회/삭제를 담당하는 Manager
 *
 * <p>Redis 작업은 트랜잭션 없이 수행 (외부 시스템)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenCacheManager {

    private final RefreshTokenCachePort refreshTokenCachePort;

    public RefreshTokenCacheManager(RefreshTokenCachePort refreshTokenCachePort) {
        this.refreshTokenCachePort = refreshTokenCachePort;
    }

    /**
     * Refresh Token 캐시 저장
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param refreshToken Refresh Token 값
     * @param expiresInSeconds 만료 시간 (초)
     */
    public void save(String memberId, String refreshToken, long expiresInSeconds) {
        refreshTokenCachePort.save(memberId, refreshToken, expiresInSeconds);
    }

    /**
     * 회원 ID로 Refresh Token 조회
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Refresh Token (Optional)
     */
    public Optional<String> findByMemberId(String memberId) {
        return refreshTokenCachePort.findByMemberId(memberId);
    }

    /**
     * Refresh Token으로 회원 ID 조회
     *
     * @param refreshToken Refresh Token 값
     * @return 회원 ID (UUID 문자열, Optional)
     */
    public Optional<String> findMemberIdByToken(String refreshToken) {
        return refreshTokenCachePort.findMemberIdByToken(refreshToken);
    }

    /**
     * Refresh Token 삭제 (회원 ID 기준)
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    public void deleteByMemberId(String memberId) {
        refreshTokenCachePort.deleteByMemberId(memberId);
    }

    /**
     * Refresh Token 삭제 (토큰 값 기준)
     *
     * @param refreshToken Refresh Token 값
     */
    public void deleteByToken(String refreshToken) {
        refreshTokenCachePort.deleteByToken(refreshToken);
    }
}
