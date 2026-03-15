package com.ryuqq.setof.application.member.internal;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.factory.KakaoMemberFactory;
import com.ryuqq.setof.application.member.manager.MemberAuthCommandManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * KakaoLoginCoordinator - 카카오 로그인 분기 코디네이터.
 *
 * <p>신규 회원 가입, 기존 회원 로그인, 통합회원 처리의 분기 로직을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class KakaoLoginCoordinator {

    private final KakaoMemberFactory kakaoMemberFactory;
    private final MemberRegistrationFacade memberRegistrationFacade;
    private final MemberAuthCommandManager memberAuthCommandManager;
    private final TokenCommandFacade tokenCommandFacade;

    public KakaoLoginCoordinator(
            KakaoMemberFactory kakaoMemberFactory,
            MemberRegistrationFacade memberRegistrationFacade,
            MemberAuthCommandManager memberAuthCommandManager,
            TokenCommandFacade tokenCommandFacade) {
        this.kakaoMemberFactory = kakaoMemberFactory;
        this.memberRegistrationFacade = memberRegistrationFacade;
        this.memberAuthCommandManager = memberAuthCommandManager;
        this.tokenCommandFacade = tokenCommandFacade;
    }

    /**
     * 기존 회원 유무에 따라 적절한 로그인 처리를 수행합니다.
     *
     * @param existingOpt 기존 회원 인증 정보 (Optional)
     * @param command 카카오 로그인 Command
     * @return 카카오 로그인 결과
     */
    @Transactional
    public KakaoLoginResult coordinate(
            Optional<MemberWithCredentials> existingOpt, KakaoLoginCommand command) {
        if (existingOpt.isPresent()) {
            return handleExistingMember(existingOpt.get(), command);
        }
        return handleNewMember(command);
    }

    private KakaoLoginResult handleExistingMember(
            MemberWithCredentials credentials, KakaoLoginCommand command) {
        Member member = credentials.member();
        Long userId = member.idValue();

        if (command.integration()) {
            MemberAuth socialAuth = kakaoMemberFactory.createSocialAuth(userId, command);
            memberAuthCommandManager.persist(socialAuth);
            return KakaoLoginResult.ofIntegrated(tokenCommandFacade.issueLoginResult(userId));
        }

        return KakaoLoginResult.ofExistingMember(tokenCommandFacade.issueLoginResult(userId));
    }

    private KakaoLoginResult handleNewMember(KakaoLoginCommand command) {
        MemberRegistrationBundle bundle = kakaoMemberFactory.createRegistrationBundle(command);
        Long userId = memberRegistrationFacade.register(bundle);
        return KakaoLoginResult.ofNewMember(tokenCommandFacade.issueLoginResult(userId));
    }
}
