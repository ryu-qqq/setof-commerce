package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.application.member.port.in.command.LogoutMemberUseCase;
import org.springframework.stereotype.Service;

/**
 * 로그아웃 서비스
 *
 * <p>회원의 Refresh Token을 무효화하여 로그아웃 처리
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>TokenManager로 Refresh Token 무효화 (Cache + RDB)
 * </ol>
 *
 * <p>주의: Redis 삭제는 트랜잭션 외부에서 수행됨
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class LogoutMemberService implements LogoutMemberUseCase {

    private final TokenManager tokenManager;

    public LogoutMemberService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void execute(LogoutMemberCommand command) {
        tokenManager.revokeTokensByMemberId(command.memberId());
    }
}
