package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.command.MemberAuthCommandPort;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import org.springframework.stereotype.Component;

/**
 * MemberAuthCommandManager - 인증 수단 Command Manager.
 *
 * <p>MemberAuthCommandPort를 래핑합니다. 트랜잭션 경계는 Facade 또는 Service에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberAuthCommandManager {

    private final MemberAuthCommandPort memberAuthCommandPort;

    public MemberAuthCommandManager(MemberAuthCommandPort memberAuthCommandPort) {
        this.memberAuthCommandPort = memberAuthCommandPort;
    }

    public void persist(MemberAuth memberAuth) {
        memberAuthCommandPort.persist(memberAuth);
    }
}
