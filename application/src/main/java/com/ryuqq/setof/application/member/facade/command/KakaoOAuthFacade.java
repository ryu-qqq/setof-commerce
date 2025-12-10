package com.ryuqq.setof.application.member.facade.command;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.bundle.KakaoOAuthResult;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.factory.command.MemberCommandFactory;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 카카오 OAuth Facade
 *
 * <p>카카오 로그인 시나리오별 회원 처리를 조율합니다.
 *
 * <p>시나리오:
 *
 * <ol>
 *   <li>카카오 ID로 기존 회원 조회 → 있으면 기존 회원 반환
 *   <li>핸드폰 번호로 회원 조회 → 있으면:
 *       <ul>
 *         <li>integration=true: 카카오 연동 후 반환
 *         <li>integration=false: 기존 회원으로 반환
 *       </ul>
 *   <li>둘 다 없으면 신규 카카오 회원 가입 → 영속화 후 반환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class KakaoOAuthFacade {

    private final MemberReadManager memberReadManager;
    private final KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;
    private final MemberCommandFactory memberCommandFactory;
    private final MemberUpdateFactory memberUpdateFactory;
    private final MemberPersistenceManager memberPersistenceManager;
    private final MemberEventDispatcher memberEventDispatcher;

    public KakaoOAuthFacade(
            MemberReadManager memberReadManager,
            KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator,
            MemberCommandFactory memberCommandFactory,
            MemberUpdateFactory memberUpdateFactory,
            MemberPersistenceManager memberPersistenceManager,
            MemberEventDispatcher memberEventDispatcher) {
        this.memberReadManager = memberReadManager;
        this.kakaoOAuthPolicyValidator = kakaoOAuthPolicyValidator;
        this.memberCommandFactory = memberCommandFactory;
        this.memberUpdateFactory = memberUpdateFactory;
        this.memberPersistenceManager = memberPersistenceManager;
        this.memberEventDispatcher = memberEventDispatcher;
    }

    /**
     * 카카오 로그인 처리
     *
     * <p>회원 조회 → 케이스 판단 → 영속화까지 완료 후 결과 반환
     *
     * @param command 카카오 로그인 커맨드
     * @return 처리 결과 (memberId, resultType)
     */
    public KakaoOAuthResult processKakaoLogin(KakaoOAuthCommand command) {
        // 1. 카카오 ID로 기존 회원 조회
        Optional<Member> kakaoMemberOpt = memberReadManager.findBySocialId(command.kakaoId());
        if (kakaoMemberOpt.isPresent()) {
            return handleExistingKakaoMember(kakaoMemberOpt.get());
        }

        // 2. 핸드폰 번호로 기존 회원 조회
        Optional<Member> existingMemberOpt =
                memberReadManager.findByPhoneNumberOptional(command.phoneNumber());
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();
            kakaoOAuthPolicyValidator.validateCanKakaoLogin(existingMember);

            if (command.integration()) {
                return handleIntegrateMember(existingMember, command);
            } else {
                return handleExistingMemberLogin(existingMember);
            }
        }

        // 3. 신규 카카오 회원 가입
        return handleNewKakaoMember(command);
    }

    private KakaoOAuthResult handleExistingKakaoMember(Member member) {
        kakaoOAuthPolicyValidator.validateCanKakaoLogin(member);
        return KakaoOAuthResult.existingKakao(member.getIdValue());
    }

    private KakaoOAuthResult handleExistingMemberLogin(Member member) {
        return KakaoOAuthResult.existingMember(member.getIdValue());
    }

    private KakaoOAuthResult handleIntegrateMember(Member member, KakaoOAuthCommand command) {
        IntegrateKakaoCommand integrateCommand =
                new IntegrateKakaoCommand(
                        member.getIdValue(),
                        command.kakaoId(),
                        command.email(),
                        command.name(),
                        command.dateOfBirth(),
                        command.gender());

        memberUpdateFactory.linkKakaoWithProfile(member, integrateCommand);
        memberPersistenceManager.persist(member);

        return KakaoOAuthResult.integrated(member.getIdValue(), member.getNameValue());
    }

    private KakaoOAuthResult handleNewKakaoMember(KakaoOAuthCommand command) {
        Member newMember = memberCommandFactory.create(command);
        memberPersistenceManager.persist(newMember);
        memberEventDispatcher.publish(newMember);

        return KakaoOAuthResult.newMember(newMember.getIdValue());
    }
}
