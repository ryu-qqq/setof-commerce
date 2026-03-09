package com.ryuqq.setof.storage.legacy.user.adapter;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.storage.legacy.user.dto.LegacyMemberProfileQueryDto;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import com.ryuqq.setof.storage.legacy.user.mapper.LegacyMemberEntityMapper;
import com.ryuqq.setof.storage.legacy.user.repository.LegacyMemberQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyMemberQueryAdapter - 레거시 회원 조회 Adapter.
 *
 * <p>Application Layer의 MemberQueryPort를 구현하는 Adapter입니다. Domain 객체만 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class LegacyMemberQueryAdapter implements MemberQueryPort {

    private final LegacyMemberQueryDslRepository repository;
    private final LegacyMemberEntityMapper mapper;

    public LegacyMemberQueryAdapter(
            LegacyMemberQueryDslRepository repository, LegacyMemberEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Member> findByLegacyId(Long userId) {
        return repository.findByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber).map(mapper::toDomain);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber).map(this::toMemberWithCredentials);
    }

    @Override
    public Optional<MemberProfile> findProfileByLegacyId(Long userId) {
        return repository.findProfileByUserId(userId).map(this::toMemberProfile);
    }

    private MemberWithCredentials toMemberWithCredentials(LegacyUserEntity entity) {
        Member member = mapper.toDomain(entity);
        return new MemberWithCredentials(
                member,
                entity.getPasswordHash(),
                entity.getSocialLoginType() != null ? entity.getSocialLoginType().name() : null,
                entity.getSocialPkId());
    }

    private MemberProfile toMemberProfile(LegacyMemberProfileQueryDto dto) {
        Member member = mapper.toDomainFromProfile(dto);
        return new MemberProfile(
                member,
                dto.getGradeName(),
                dto.getCurrentMileage(),
                dto.getSocialLoginType() != null ? dto.getSocialLoginType().name() : null,
                dto.getSocialPkId());
    }
}
