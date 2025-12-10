package com.ryuqq.setof.application.auth.service.command;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import com.ryuqq.setof.application.auth.factory.RefreshTokenFactory;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import org.springframework.stereotype.Service;

/**
 * Issue Tokens Service
 *
 * <p>토큰 발급 UseCase 구현체
 *
 * <p><strong>처리 순서:</strong>
 *
 * <ol>
 *   <li>JWT Access Token + Refresh Token 생성
 *   <li>Refresh Token → RDB 저장 (트랜잭션)
 *   <li>Refresh Token → Cache(Redis) 저장
 * </ol>
 *
 * <p><strong>주의:</strong>
 *
 * <ul>
 *   <li>이 서비스는 트랜잭션 외부에서 호출해야 함
 *   <li>JWT 발급/Redis 저장은 외부 시스템 호출
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class IssueTokensService implements IssueTokensUseCase {

    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenFacade refreshTokenFacade;

    public IssueTokensService(
            TokenProviderPort tokenProviderPort,
            RefreshTokenFactory refreshTokenFactory,
            RefreshTokenFacade refreshTokenFacade) {
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenFacade = refreshTokenFacade;
    }

    @Override
    public TokenPairResponse execute(String memberId) {
        TokenPairResponse tokenPair = tokenProviderPort.generateTokenPair(memberId);
        RefreshToken refreshToken =
                refreshTokenFactory.create(
                        memberId, tokenPair.refreshToken(), tokenPair.refreshTokenExpiresIn());

        refreshTokenFacade.persist(refreshToken);
        return tokenPair;
    }
}
