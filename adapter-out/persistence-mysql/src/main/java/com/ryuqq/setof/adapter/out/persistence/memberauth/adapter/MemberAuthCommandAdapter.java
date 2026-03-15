package com.ryuqq.setof.adapter.out.persistence.memberauth.adapter;

import com.ryuqq.setof.adapter.out.persistence.memberauth.mapper.MemberAuthJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.memberauth.repository.MemberAuthJpaRepository;
import com.ryuqq.setof.application.member.port.out.command.MemberAuthCommandPort;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MemberAuthCommandAdapter - 인증 수단 Command 어댑터.
 *
 * <p>member_auths 테이블만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MemberAuthCommandAdapter implements MemberAuthCommandPort {

    private final MemberAuthJpaRepository memberAuthJpaRepository;
    private final MemberAuthJpaEntityMapper memberAuthJpaEntityMapper;

    public MemberAuthCommandAdapter(
            MemberAuthJpaRepository memberAuthJpaRepository,
            MemberAuthJpaEntityMapper memberAuthJpaEntityMapper) {
        this.memberAuthJpaRepository = memberAuthJpaRepository;
        this.memberAuthJpaEntityMapper = memberAuthJpaEntityMapper;
    }

    @Override
    public void persist(MemberAuth memberAuth) {
        memberAuthJpaRepository.save(memberAuthJpaEntityMapper.toEntity(memberAuth));
    }
}
