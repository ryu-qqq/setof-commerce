package com.ryuqq.setof.application.auth.port.out.query;

import java.util.Optional;

/**
 * Refresh Token Query Port
 *
 * <p>Refresh Token RDB 조회를 담당하는 Port-Out 인터페이스
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>adapter-out-persistence: RefreshTokenQueryAdapter
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenQueryPort {

    /**
     * 회원 ID로 토큰 값 조회
     *
     * <p>가장 최근 토큰 반환 (1:N 관계에서 최신 토큰)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 값 (Optional)
     */
    Optional<String> findTokenByMemberId(String memberId);

    /**
     * 토큰 값으로 회원 ID 조회
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (Optional)
     */
    Optional<String> findMemberIdByToken(String tokenValue);
}
