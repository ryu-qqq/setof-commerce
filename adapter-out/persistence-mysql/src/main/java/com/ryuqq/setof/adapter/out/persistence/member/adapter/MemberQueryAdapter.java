package com.ryuqq.setof.adapter.out.persistence.member.adapter;

import com.querydsl.core.Tuple;
import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.member.entity.QMemberJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.member.mapper.MemberJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.member.repository.MemberQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.MemberAuthJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.QMemberAuthJpaEntity;
import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "persistence.member.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class MemberQueryAdapter implements MemberQueryPort {

    private final MemberQueryDslRepository queryDslRepository;
    private final MemberJpaEntityMapper mapper;

    public MemberQueryAdapter(
            MemberQueryDslRepository queryDslRepository, MemberJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return queryDslRepository.findByMemberId(memberId).map(mapper::toDomain);
    }

    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return queryDslRepository.findByPhoneNumber(phoneNumber).map(mapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return queryDslRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber) {
        List<Tuple> tuples = queryDslRepository.findMemberWithAuthsByPhoneNumber(phoneNumber);

        if (tuples.isEmpty()) {
            return Optional.empty();
        }

        MemberJpaEntity memberEntity = tuples.get(0).get(QMemberJpaEntity.memberJpaEntity);

        if (memberEntity == null) {
            return Optional.empty();
        }

        Member member = mapper.toDomain(memberEntity);

        String passwordHash = null;
        String socialLoginType = null;
        String socialPkId = null;

        for (Tuple tuple : tuples) {
            MemberAuthJpaEntity authEntity = tuple.get(QMemberAuthJpaEntity.memberAuthJpaEntity);
            if (authEntity == null) {
                continue;
            }
            String provider = authEntity.getAuthProvider();
            if ("PHONE".equals(provider)) {
                passwordHash = authEntity.getPasswordHash();
            } else {
                socialLoginType = provider;
                socialPkId = authEntity.getProviderUserId();
            }
        }

        return Optional.of(
                new MemberWithCredentials(member, passwordHash, socialLoginType, socialPkId));
    }

    @Override
    public Optional<MemberLoginInfo> findLoginInfoById(Long memberId) {
        Optional<MemberJpaEntity> memberOpt = queryDslRepository.findByMemberId(memberId);

        if (memberOpt.isEmpty()) {
            return Optional.empty();
        }

        Member member = mapper.toDomain(memberOpt.get());

        List<MemberAuthJpaEntity> auths = queryDslRepository.findAuthsByMemberId(memberId);

        String socialLoginType = null;
        String socialPkId = null;

        for (MemberAuthJpaEntity auth : auths) {
            if (!"PHONE".equals(auth.getAuthProvider())) {
                socialLoginType = auth.getAuthProvider();
                socialPkId = auth.getProviderUserId();
                break;
            }
        }

        return Optional.of(new MemberLoginInfo(member, socialLoginType, socialPkId));
    }
}
