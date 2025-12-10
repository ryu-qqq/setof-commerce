package com.ryuqq.setof.application.auth.factory;

import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Factory
 *
 * <p>RefreshToken Aggregate 생성을 담당하는 Factory
 *
 * <p><strong>Factory 책임:</strong>
 *
 * <ul>
 *   <li>도메인 객체 생성 로직 캡슐화
 *   <li>ClockHolder를 통한 시간 주입 (테스트 용이성)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenFactory {

    private final ClockHolder clockHolder;

    public RefreshTokenFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * RefreshToken Aggregate 생성
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @param tokenValue 토큰 값
     * @param expiresInSeconds 만료까지 남은 초
     * @return RefreshToken Aggregate
     */
    public RefreshToken create(String memberId, String tokenValue, long expiresInSeconds) {
        Instant now = Instant.now(clockHolder.getClock());
        return RefreshToken.create(memberId, tokenValue, expiresInSeconds, now);
    }
}
