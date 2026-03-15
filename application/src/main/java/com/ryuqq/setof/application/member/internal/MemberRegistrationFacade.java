package com.ryuqq.setof.application.member.internal;

import com.ryuqq.setof.application.member.dto.command.MemberRegistrationBundle;
import com.ryuqq.setof.application.member.manager.MemberAuthCommandManager;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.manager.MemberConsentCommandManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberRegistrationFacade - 회원 등록 파사드.
 *
 * <p>도메인별 CommandManager를 오케스트레이션하며 트랜잭션 경계를 관리합니다. Member → Auth → Consent를 하나의 트랜잭션으로 묶습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberRegistrationFacade {

    private final MemberCommandManager memberCommandManager;
    private final MemberAuthCommandManager memberAuthCommandManager;
    private final MemberConsentCommandManager memberConsentCommandManager;

    public MemberRegistrationFacade(
            MemberCommandManager memberCommandManager,
            MemberAuthCommandManager memberAuthCommandManager,
            MemberConsentCommandManager memberConsentCommandManager) {
        this.memberCommandManager = memberCommandManager;
        this.memberAuthCommandManager = memberAuthCommandManager;
        this.memberConsentCommandManager = memberConsentCommandManager;
    }

    /**
     * 신규 회원 등록.
     *
     * <p>Member를 먼저 저장하여 auto-increment PK를 받은 뒤, Auth와 Consent에 memberId를 할당하고 저장합니다.
     *
     * @param bundle 회원 등록 번들 (Member + MemberAuth + MemberConsent)
     * @return 생성된 회원 PK
     */
    @Transactional
    public Long register(MemberRegistrationBundle bundle) {
        Long memberId = memberCommandManager.persist(bundle.member());
        bundle.assignMemberId(memberId);

        memberAuthCommandManager.persist(bundle.memberAuth());
        memberConsentCommandManager.persist(bundle.memberConsent());

        return memberId;
    }
}
