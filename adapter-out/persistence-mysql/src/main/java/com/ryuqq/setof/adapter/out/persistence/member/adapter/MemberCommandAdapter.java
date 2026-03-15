package com.ryuqq.setof.adapter.out.persistence.member.adapter;

import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.member.mapper.MemberJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.member.repository.MemberJpaRepository;
import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MemberCommandAdapter - 회원 엔티티 Command 어댑터.
 *
 * <p>members 테이블만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MemberCommandAdapter implements MemberCommandPort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberJpaEntityMapper mapper;

    public MemberCommandAdapter(
            MemberJpaRepository memberJpaRepository, MemberJpaEntityMapper mapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(Member member) {
        MemberJpaEntity entity = mapper.toEntity(member);
        MemberJpaEntity saved = memberJpaRepository.save(entity);
        return saved.getId();
    }
}
