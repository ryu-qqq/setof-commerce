package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.command.RefreshTokenPersistencePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Refresh Token Persistence Manager
 *
 * <p>Refresh Token RDB 저장/삭제를 담당하는 Manager
 *
 * <p>이 클래스만 트랜잭션을 관리 (MySQL 저장은 트랜잭션 필요)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenPersistenceManager {

    private final RefreshTokenPersistencePort refreshTokenPersistencePort;

    public RefreshTokenPersistenceManager(RefreshTokenPersistencePort refreshTokenPersistencePort) {
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
    }

    /**
     * Refresh Token RDB 저장
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param refreshToken Refresh Token 값
     * @param expiresInSeconds 만료 시간 (초)
     */
    @Transactional
    public void save(String memberId, String refreshToken, long expiresInSeconds) {
        refreshTokenPersistencePort.save(memberId, refreshToken, expiresInSeconds);
    }

    /**
     * Refresh Token 삭제 (회원 ID 기준)
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    @Transactional
    public void deleteByMemberId(String memberId) {
        refreshTokenPersistencePort.deleteByMemberId(memberId);
    }

    /**
     * Refresh Token 삭제 (토큰 값 기준)
     *
     * @param refreshToken Refresh Token 값
     */
    @Transactional
    public void deleteByToken(String refreshToken) {
        refreshTokenPersistencePort.deleteByToken(refreshToken);
    }
}
