package com.ryuqq.setof.adapter.out.persistence.memberauth.adapter;

import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.MemberAuthJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.memberauth.mapper.MemberAuthJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.memberauth.repository.MemberAuthQueryDslRepository;
import com.ryuqq.setof.application.member.port.out.query.MemberAuthQueryPort;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MemberAuthQueryAdapter - 인증 수단 Query 어댑터.
 *
 * <p>member_auths 테이블 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MemberAuthQueryAdapter implements MemberAuthQueryPort {

    private final MemberAuthQueryDslRepository memberAuthQueryDslRepository;
    private final MemberAuthJpaEntityMapper memberAuthJpaEntityMapper;

    public MemberAuthQueryAdapter(
            MemberAuthQueryDslRepository memberAuthQueryDslRepository,
            MemberAuthJpaEntityMapper memberAuthJpaEntityMapper) {
        this.memberAuthQueryDslRepository = memberAuthQueryDslRepository;
        this.memberAuthJpaEntityMapper = memberAuthJpaEntityMapper;
    }

    @Override
    public MemberAuth findPhoneAuthByMemberId(Long memberId) {
        MemberAuthJpaEntity entity =
                memberAuthQueryDslRepository
                        .findByMemberIdAndProvider(memberId, "PHONE")
                        .orElseThrow(
                                () ->
                                        new MemberNotFoundException(
                                                "Phone auth not found for memberId: " + memberId));
        return memberAuthJpaEntityMapper.toDomain(entity);
    }
}
