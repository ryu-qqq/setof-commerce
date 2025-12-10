package com.ryuqq.setof.application.auth.port.out.command;

import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;

/**
 * Refresh Token Persistence Port
 *
 * <p>Refresh Token RDB 저장/삭제를 담당하는 Port-Out 인터페이스
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>adapter-out-persistence: RefreshTokenPersistenceAdapter
 * </ul>
 *
 * <p><strong>트랜잭션:</strong>
 *
 * <ul>
 *   <li>@Transactional은 Facade/Manager 책임
 *   <li>Adapter는 트랜잭션 어노테이션 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenPersistencePort {

    /**
     * Refresh Token 저장
     *
     * @param refreshToken RefreshToken Aggregate
     */
    void persist(RefreshToken refreshToken);

    /**
     * 회원 ID 기준 모든 Refresh Token 삭제
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    void deleteByMemberId(String memberId);

    /**
     * 토큰 값 기준 삭제
     *
     * @param tokenValue 토큰 값
     */
    void deleteByToken(String tokenValue);
}
