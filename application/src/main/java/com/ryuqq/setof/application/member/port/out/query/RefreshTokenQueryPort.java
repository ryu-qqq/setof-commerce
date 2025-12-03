package com.ryuqq.setof.application.member.port.out.query;

import java.util.Optional;

/**
 * Refresh Token Query Port
 *
 * <p>Refresh Token 조회를 담당하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefreshTokenQueryPort {

    /**
     * 회원 ID로 Refresh Token 조회
     *
     * @param memberId 회원 ID
     * @return Refresh Token 값 (Optional)
     */
    Optional<String> findByMemberId(Long memberId);

    /**
     * Refresh Token 값으로 회원 ID 조회
     *
     * @param refreshToken Refresh Token 값
     * @return 회원 ID (Optional)
     */
    Optional<Long> findMemberIdByToken(String refreshToken);

    /**
     * Refresh Token 존재 여부 확인
     *
     * @param refreshToken Refresh Token 값
     * @return 존재 여부
     */
    boolean existsByToken(String refreshToken);
}
