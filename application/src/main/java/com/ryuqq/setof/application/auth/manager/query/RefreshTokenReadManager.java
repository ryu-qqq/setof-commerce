package com.ryuqq.setof.application.auth.manager.query;

import com.ryuqq.setof.application.auth.port.out.query.RefreshTokenQueryPort;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Refresh Token Read Manager
 *
 * <p>RDB에서 Refresh Token 조회를 관리하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefreshTokenReadManager {

    private final RefreshTokenQueryPort refreshTokenQueryPort;

    public RefreshTokenReadManager(RefreshTokenQueryPort refreshTokenQueryPort) {
        this.refreshTokenQueryPort = refreshTokenQueryPort;
    }

    /**
     * 회원 ID로 토큰 값 조회
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 값 (Optional)
     */
    public Optional<String> findTokenByMemberId(String memberId) {
        return refreshTokenQueryPort.findTokenByMemberId(memberId);
    }

    /**
     * 토큰 값으로 회원 ID 조회
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (Optional)
     */
    public Optional<String> findMemberIdByToken(String tokenValue) {
        return refreshTokenQueryPort.findMemberIdByToken(tokenValue);
    }
}
