package com.ryuqq.setof.application.auth.facade.command;

import com.ryuqq.setof.application.auth.manager.command.RefreshTokenCacheManager;
import com.ryuqq.setof.application.auth.manager.command.RefreshTokenPersistenceManager;
import com.ryuqq.setof.application.auth.manager.query.RefreshTokenCacheReadManager;
import com.ryuqq.setof.application.auth.manager.query.RefreshTokenReadManager;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Facade
 *
 * <p>RDB와 Cache(Redis) 양쪽의 Refresh Token 저장/조회/삭제를 통합 관리하는 Facade
 *
 * <p><strong>저장 순서:</strong>
 *
 * <ol>
 *   <li>Factory로 RefreshToken Aggregate 생성
 *   <li>RDB 저장 (트랜잭션 O)
 *   <li>Cache 저장 (트랜잭션 X)
 * </ol>
 *
 * <p><strong>삭제 순서:</strong>
 *
 * <ol>
 *   <li>Cache 삭제 (빠른 무효화)
 *   <li>RDB 삭제 (트랜잭션 O)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenFacade {

    private final RefreshTokenPersistenceManager refreshTokenPersistenceManager;
    private final RefreshTokenCacheManager refreshTokenCacheManager;
    private final RefreshTokenReadManager refreshTokenReadManager;
    private final RefreshTokenCacheReadManager refreshTokenCacheReadManager;

    public RefreshTokenFacade(
            RefreshTokenPersistenceManager refreshTokenPersistenceManager,
            RefreshTokenCacheManager refreshTokenCacheManager,
            RefreshTokenReadManager refreshTokenReadManager,
            RefreshTokenCacheReadManager refreshTokenCacheReadManager) {
        this.refreshTokenPersistenceManager = refreshTokenPersistenceManager;
        this.refreshTokenCacheManager = refreshTokenCacheManager;
        this.refreshTokenReadManager = refreshTokenReadManager;
        this.refreshTokenCacheReadManager = refreshTokenCacheReadManager;
    }

    /**
     * Refresh Token 저장 (RDB + Cache)
     *
     * <p>순서: Factory 생성 → RDB 저장 → Cache 저장
     *
     * @param refreshToken 리프레시 토큰
     */
    public void persist(RefreshToken refreshToken) {

        refreshTokenPersistenceManager.persist(refreshToken);
        refreshTokenCacheManager.persist(
                refreshToken.getTokenValue(),
                refreshToken.getMemberId(),
                refreshToken.getExpiresInSeconds());
    }

    /**
     * Refresh Token 삭제 (회원 ID 기준, RDB + Cache)
     *
     * <p>순서: DB에서 토큰 조회 → Cache 삭제 → RDB 삭제
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    public void deleteByMemberId(String memberId) {
        refreshTokenReadManager
                .findTokenByMemberId(memberId)
                .ifPresent(refreshTokenCacheManager::delete);
        refreshTokenPersistenceManager.deleteByMemberId(memberId);
    }

    /**
     * Refresh Token 삭제 (토큰 기준, RDB + Cache)
     *
     * <p>순서: Cache 삭제 → RDB 삭제
     *
     * @param tokenValue 토큰 값
     */
    public void deleteByToken(String tokenValue) {
        refreshTokenCacheManager.delete(tokenValue);
        refreshTokenPersistenceManager.deleteByToken(tokenValue);
    }

    /**
     * Refresh Token으로 회원 ID 조회 (Cache 우선)
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (UUID 문자열, Optional)
     */
    public Optional<String> findMemberIdByToken(String tokenValue) {
        return refreshTokenCacheReadManager.findMemberIdByToken(tokenValue);
    }

    /**
     * 회원 ID로 Refresh Token 조회 (RDB)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 값 (Optional)
     */
    public Optional<String> findTokenByMemberId(String memberId) {
        return refreshTokenReadManager.findTokenByMemberId(memberId);
    }
}
