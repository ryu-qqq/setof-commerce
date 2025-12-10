package com.ryuqq.setof.application.member.port.out.command;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;

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
     * @param member 저장할 Member (Domain Aggregate, UUID v7 ID 포함)
     * @return 저장된 Member의 ID
     */
    MemberId persist(Member member);
}
