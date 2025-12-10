package com.ryuqq.setof.application.auth.manager.command;

import com.ryuqq.setof.application.auth.port.out.command.RefreshTokenPersistencePort;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Refresh Token Persistence Manager
 *
 * <p>RDB에 Refresh Token 저장/삭제를 관리하는 Manager
 *
 * <p><strong>트랜잭션 책임:</strong>
 *
 * <ul>
 *   <li>@Transactional은 Manager에서 관리
 *   <li>Adapter는 트랜잭션 어노테이션 금지
 * </ul>
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
     * Refresh Token 저장
     *
     * @param refreshToken RefreshToken Aggregate
     */
    @Transactional
    public void persist(RefreshToken refreshToken) {
        refreshTokenPersistencePort.persist(refreshToken);
    }

    /**
     * 회원 ID 기준 모든 Refresh Token 삭제
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    @Transactional
    public void deleteByMemberId(String memberId) {
        refreshTokenPersistencePort.deleteByMemberId(memberId);
    }

    /**
     * 토큰 값 기준 삭제
     *
     * @param tokenValue 토큰 값
     */
    @Transactional
    public void deleteByToken(String tokenValue) {
        refreshTokenPersistencePort.deleteByToken(tokenValue);
    }
}
