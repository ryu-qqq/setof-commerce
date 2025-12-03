package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.component.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.port.in.command.IntegrateKakaoUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 카카오 계정 통합 서비스
 *
 * <p>기존 LOCAL 회원에 카카오 계정을 연동하고 프로필을 업데이트합니다.
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReader로 회원 조회
 *   <li>KakaoOAuthPolicyValidator로 통합 가능 여부 검증
 *   <li>MemberUpdater로 카카오 연동 및 프로필 업데이트
 *   <li>MemberPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class IntegrateKakaoService implements IntegrateKakaoUseCase {

    private final MemberReader memberReader;
    private final KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;
    private final MemberUpdater memberUpdater;
    private final MemberPersistenceManager memberPersistenceManager;

    public IntegrateKakaoService(
            MemberReader memberReader,
            KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator,
            MemberUpdater memberUpdater,
            MemberPersistenceManager memberPersistenceManager) {
        this.memberReader = memberReader;
        this.kakaoOAuthPolicyValidator = kakaoOAuthPolicyValidator;
        this.memberUpdater = memberUpdater;
        this.memberPersistenceManager = memberPersistenceManager;
    }

    @Override
    public void execute(IntegrateKakaoCommand command) {
        Member member = memberReader.getById(command.memberId());

        kakaoOAuthPolicyValidator.validateCanIntegrateKakao(member);

        memberUpdater.linkKakaoWithProfile(member, command);

        memberPersistenceManager.persist(member);
    }
}
