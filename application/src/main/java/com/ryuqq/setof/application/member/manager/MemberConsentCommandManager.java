package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.command.MemberConsentCommandPort;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import org.springframework.stereotype.Component;

/**
 * MemberConsentCommandManager - 동의 정보 Command Manager.
 *
 * <p>MemberConsentCommandPort를 래핑합니다. 트랜잭션 경계는 Facade 또는 Service에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberConsentCommandManager {

    private final MemberConsentCommandPort memberConsentCommandPort;

    public MemberConsentCommandManager(MemberConsentCommandPort memberConsentCommandPort) {
        this.memberConsentCommandPort = memberConsentCommandPort;
    }

    public void persist(MemberConsent memberConsent) {
        memberConsentCommandPort.persist(memberConsent);
    }
}
