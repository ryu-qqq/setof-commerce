package com.ryuqq.setof.application.member.manager.command;

import com.ryuqq.setof.application.member.port.out.command.MemberPersistencePort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Member Persistence Manager
 *
 * <p>Member 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberPersistenceManager {

    private final MemberPersistencePort memberPersistencePort;

    public MemberPersistenceManager(MemberPersistencePort memberPersistencePort) {
        this.memberPersistencePort = memberPersistencePort;
    }

    /**
     * Member 저장
     *
     * <p>JPA dirty checking으로 수정 시에도 persist 호출로 처리
     *
     * @param member 저장할 Member (UUID v7 ID 포함)
     * @return 저장된 Member의 ID
     */
    @Transactional
    public MemberId persist(Member member) {
        return memberPersistencePort.persist(member);
    }
}
