package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.member.dto.bundle.KakaoOAuthResult;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.facade.command.KakaoOAuthFacade;
import com.ryuqq.setof.application.member.port.in.command.KakaoOAuthLoginUseCase;
import org.springframework.stereotype.Service;

/**
 * 카카오 OAuth 로그인 서비스
 *
 * <p>역할: UseCase 구현, 토큰 발급, 응답 조립
 *
 * <p>시나리오별 처리는 KakaoOAuthFacade에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class KakaoOAuthLoginService implements KakaoOAuthLoginUseCase {

    private final KakaoOAuthFacade kakaoOAuthFacade;
    private final IssueTokensUseCase issueTokensUseCase;

    public KakaoOAuthLoginService(
            KakaoOAuthFacade kakaoOAuthFacade, IssueTokensUseCase issueTokensUseCase) {
        this.kakaoOAuthFacade = kakaoOAuthFacade;
        this.issueTokensUseCase = issueTokensUseCase;
    }

    @Override
    public KakaoOAuthResponse execute(KakaoOAuthCommand command) {
        // 1. Facade가 회원 처리 (조회/연동/생성)
        KakaoOAuthResult result = kakaoOAuthFacade.processKakaoLogin(command);

        // 2. 토큰 발급
        TokenPairResponse tokens = issueTokensUseCase.execute(result.memberId());

        // 3. 응답 조립
        return toResponse(result, tokens);
    }

    private KakaoOAuthResponse toResponse(KakaoOAuthResult result, TokenPairResponse tokens) {
        return switch (result.resultType()) {
            case NEW_MEMBER -> KakaoOAuthResponse.newMember(result.memberId(), tokens);
            case EXISTING_KAKAO, EXISTING_MEMBER ->
                    KakaoOAuthResponse.existingKakaoMember(result.memberId(), tokens);
            case INTEGRATED ->
                    KakaoOAuthResponse.integrated(result.memberId(), tokens, result.memberName());
        };
    }
}
