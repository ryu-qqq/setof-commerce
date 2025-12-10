package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.factory.command.MemberUpdateFactory;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.command.IntegrateKakaoUseCase;
import com.ryuqq.setof.application.member.validator.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 카카오 계정 통합 서비스
 *
 * <p>기존 LOCAL 회원에 카카오 계정을 연동하고 프로필을 업데이트합니다.
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReadManager로 회원 조회
 *   <li>KakaoOAuthPolicyValidator로 통합 가능 여부 검증
 *   <li>MemberUpdateFactory로 카카오 연동 및 프로필 업데이트
 *   <li>MemberPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class IntegrateKakaoService implements IntegrateKakaoUseCase {

    private final MemberReadManager memberReadManager;
    private final KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;
    private final MemberUpdateFactory memberUpdateFactory;
    private final MemberPersistenceManager memberPersistenceManager;

    public IntegrateKakaoService(
            MemberReadManager memberReadManager,
            KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator,
            MemberUpdateFactory memberUpdateFactory,
            MemberPersistenceManager memberPersistenceManager) {
        this.memberReadManager = memberReadManager;
        this.kakaoOAuthPolicyValidator = kakaoOAuthPolicyValidator;
        this.memberUpdateFactory = memberUpdateFactory;
        this.memberPersistenceManager = memberPersistenceManager;
    }

    @Override
    public void execute(IntegrateKakaoCommand command) {
        Member member = memberReadManager.findById(command.memberId());

        kakaoOAuthPolicyValidator.validateCanIntegrateKakao(member);

        memberUpdateFactory.linkKakaoWithProfile(member, command);

        memberPersistenceManager.persist(member);
    }
}
