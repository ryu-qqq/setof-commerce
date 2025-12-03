package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.domain.core.member.aggregate.Member;

/**
 * Member Persistence Port (Command)
 *
 * <p>Member Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface MemberPersistencePort {

    /**
     * Member 저장 (신규 생성 또는 수정)
     *
     * <p>Member는 UUID v7 ID를 이미 갖고 있으므로 ID 반환 불필요
     *
     * @param member 저장할 Member (Domain Aggregate, UUID v7 ID 포함)
     */
    void persist(Member member);
}
