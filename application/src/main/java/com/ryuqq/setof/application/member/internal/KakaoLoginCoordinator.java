package com.ryuqq.setof.application.member.internal;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.factory.KakaoMemberFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;
import org.springframework.stereotype.Component;

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
    private final MemberCommandManager memberCommandManager;
    private final TokenCommandFacade tokenCommandFacade;

    public KakaoLoginCoordinator(
            KakaoMemberFactory kakaoMemberFactory,
            MemberCommandManager memberCommandManager,
            TokenCommandFacade tokenCommandFacade) {
        this.kakaoMemberFactory = kakaoMemberFactory;
        this.memberCommandManager = memberCommandManager;
        this.tokenCommandFacade = tokenCommandFacade;
    }

    /**
     * 기존 회원 유무에 따라 적절한 로그인 처리를 수행합니다.
     *
     * @param existingOpt 기존 회원 인증 정보 (Optional)
     * @param command 카카오 로그인 Command
     * @return 카카오 로그인 결과
     */
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
        long userId = member.legacyMemberIdValue();

        if (command.integration()) {
            SocialIntegrationContext context =
                    kakaoMemberFactory.createIntegrationContext(userId, command);
            memberCommandManager.persist(context);
            return KakaoLoginResult.ofIntegrated(tokenCommandFacade.issueLoginResult(userId));
        }

        return KakaoLoginResult.ofExistingMember(tokenCommandFacade.issueLoginResult(userId));
    }

    private KakaoLoginResult handleNewMember(KakaoLoginCommand command) {
        Member member = kakaoMemberFactory.createMember(command);
        MemberRegistrationInfo registrationInfo =
                kakaoMemberFactory.createRegistrationInfo(command);

        Long userId = memberCommandManager.persist(member, registrationInfo);
        return KakaoLoginResult.ofNewMember(tokenCommandFacade.issueLoginResult(userId));
    }
}
