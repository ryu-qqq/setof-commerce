package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Component;

/**
 * MemberCommandManager - 회원 엔티티 Command Manager.
 *
 * <p>MemberCommandPort를 래핑합니다. 트랜잭션 경계는 Facade 또는 Service에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberCommandManager {

    private final MemberCommandPort memberCommandPort;

    public MemberCommandManager(MemberCommandPort memberCommandPort) {
        this.memberCommandPort = memberCommandPort;
    }

    public Long persist(Member member) {
        return memberCommandPort.persist(member);
    }
}
