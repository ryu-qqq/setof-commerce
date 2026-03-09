package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.dto.command.SocialIntegrationContext;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberCommandManager - 회원 명령 Manager.
 *
 * <p>MemberCommandPort를 래핑하며 트랜잭션 경계를 설정합니다. Application Context를 Port 호출로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MemberCommandManager {

    private final MemberCommandPort memberCommandPort;

    public MemberCommandManager(MemberCommandPort memberCommandPort) {
        this.memberCommandPort = memberCommandPort;
    }

    /**
     * 신규 회원 저장.
     *
     * @param member 회원 도메인 객체
     * @param registrationInfo 가입 부가 정보
     * @return 생성된 회원의 레거시 user_id
     */
    @Transactional
    public Long persist(Member member, MemberRegistrationInfo registrationInfo) {
        return memberCommandPort.persist(member, registrationInfo);
    }

    /**
     * 회원 상태 변경 - 탈퇴 (더티 체킹).
     *
     * @param context 상태 변경 Context
     */
    @Transactional
    public void persist(StatusChangeContext context) {
        memberCommandPort.persistWithdrawal(context.userId(), context.withdrawalReason());
    }

    /**
     * 비밀번호 변경 (더티 체킹).
     *
     * @param context 비밀번호 변경 Context
     */
    @Transactional
    public void persist(UpdatePasswordContext context) {
        memberCommandPort.persistPasswordChange(context.userId(), context.encodedPassword());
    }

    /**
     * 소셜 로그인 통합 (JDBC UPDATE).
     *
     * @param context 소셜 통합 Context
     */
    @Transactional
    public void persist(SocialIntegrationContext context) {
        memberCommandPort.persistSocialIntegration(
                context.userId(),
                context.socialLoginType(),
                context.socialPkId(),
                context.gender(),
                context.dateOfBirth());
    }
}
