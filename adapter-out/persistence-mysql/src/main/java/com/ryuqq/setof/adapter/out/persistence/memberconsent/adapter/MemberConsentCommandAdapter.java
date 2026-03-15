package com.ryuqq.setof.adapter.out.persistence.memberconsent.adapter;

import com.ryuqq.setof.adapter.out.persistence.memberconsent.mapper.MemberConsentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.memberconsent.repository.MemberConsentJpaRepository;
import com.ryuqq.setof.application.member.port.out.command.MemberConsentCommandPort;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MemberConsentCommandAdapter - 동의 정보 Command 어댑터.
 *
 * <p>member_consents 테이블만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MemberConsentCommandAdapter implements MemberConsentCommandPort {

    private final MemberConsentJpaRepository memberConsentJpaRepository;
    private final MemberConsentJpaEntityMapper memberConsentJpaEntityMapper;

    public MemberConsentCommandAdapter(
            MemberConsentJpaRepository memberConsentJpaRepository,
            MemberConsentJpaEntityMapper memberConsentJpaEntityMapper) {
        this.memberConsentJpaRepository = memberConsentJpaRepository;
        this.memberConsentJpaEntityMapper = memberConsentJpaEntityMapper;
    }

    @Override
    public void persist(MemberConsent memberConsent) {
        memberConsentJpaRepository.save(memberConsentJpaEntityMapper.toEntity(memberConsent));
    }
}
