package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.component.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberCreator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.facade.RegisterMemberFacade;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.application.member.port.in.command.KakaoOAuthLoginUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 카카오 OAuth 로그인 서비스
 *
 * <p>처리 시나리오:
 *
 * <ol>
 *   <li>카카오 ID로 기존 회원 조회 → 있으면 토큰 발급
 *   <li>핸드폰 번호로 LOCAL 회원 조회 → 있으면 통합 유도 플래그 반환
 *   <li>둘 다 없으면 신규 카카오 회원 자동 가입 → 토큰 발급
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class KakaoOAuthLoginService implements KakaoOAuthLoginUseCase {

    private final MemberReader memberReader;
    private final MemberCreator memberCreator;
    private final MemberAssembler memberAssembler;
    private final KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;
    private final RegisterMemberFacade registerMemberFacade;
    private final TokenManager tokenManager;

    public KakaoOAuthLoginService(
            MemberReader memberReader,
            MemberCreator memberCreator,
            MemberAssembler memberAssembler,
            KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator,
            RegisterMemberFacade registerMemberFacade,
            TokenManager tokenManager) {
        this.memberReader = memberReader;
        this.memberCreator = memberCreator;
        this.memberAssembler = memberAssembler;
        this.kakaoOAuthPolicyValidator = kakaoOAuthPolicyValidator;
        this.registerMemberFacade = registerMemberFacade;
        this.tokenManager = tokenManager;
    }

    @Override
    public KakaoOAuthResponse execute(KakaoOAuthCommand command) {
        Optional<Member> kakaoMemberOpt = memberReader.findBySocialId(command.kakaoId());
        if (kakaoMemberOpt.isPresent()) {
            return handleExistingKakaoMember(kakaoMemberOpt.get());
        }

        return handleNewKakaoMember(command);
    }

    private KakaoOAuthResponse handleExistingKakaoMember(Member member) {
        kakaoOAuthPolicyValidator.validateCanKakaoLogin(member);
        TokenPairResponse tokens = tokenManager.issueTokens(member.getIdValue());
        return memberAssembler.toExistingKakaoMemberResponse(member.getIdValue(), tokens);
    }

    private KakaoOAuthResponse handleNewKakaoMember(KakaoOAuthCommand command) {
        Member newMember = memberCreator.createKakaoMember(command);
        RegisterMemberResponse response = registerMemberFacade.register(newMember);
        return memberAssembler.toNewKakaoMemberResponse(response.memberId(), response.tokens());
    }
}
