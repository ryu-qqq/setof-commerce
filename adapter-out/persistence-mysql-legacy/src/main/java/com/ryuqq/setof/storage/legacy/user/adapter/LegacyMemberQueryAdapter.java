package com.ryuqq.setof.storage.legacy.user.adapter;

import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.port.out.query.MemberAuthQueryPort;
import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import com.ryuqq.setof.domain.member.id.MemberAuthId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import com.ryuqq.setof.storage.legacy.user.dto.LegacyMemberProfileQueryDto;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import com.ryuqq.setof.storage.legacy.user.mapper.LegacyMemberEntityMapper;
import com.ryuqq.setof.storage.legacy.user.repository.LegacyMemberQueryDslRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "persistence.member.enabled", havingValue = "true")
public class LegacyMemberQueryAdapter implements MemberQueryPort, MemberAuthQueryPort {

    private final LegacyMemberQueryDslRepository repository;
    private final LegacyMemberEntityMapper mapper;

    public LegacyMemberQueryAdapter(
            LegacyMemberQueryDslRepository repository, LegacyMemberEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return repository.findByUserId(memberId).map(mapper::toDomain);
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
    public Optional<MemberLoginInfo> findLoginInfoById(Long memberId) {
        return repository.findProfileByUserId(memberId).map(this::toMemberLoginInfo);
    }

    private MemberWithCredentials toMemberWithCredentials(LegacyUserEntity entity) {
        Member member = mapper.toDomain(entity);
        return new MemberWithCredentials(
                member,
                entity.getPasswordHash(),
                entity.getSocialLoginType() != null ? entity.getSocialLoginType().name() : null,
                entity.getSocialPkId());
    }

    private MemberLoginInfo toMemberLoginInfo(LegacyMemberProfileQueryDto dto) {
        Member member = mapper.toDomainFromProfile(dto);
        return new MemberLoginInfo(
                member,
                dto.getSocialLoginType() != null ? dto.getSocialLoginType().name() : null,
                dto.getSocialPkId());
    }

    @Override
    public MemberAuth findPhoneAuthByMemberId(Long memberId) {
        LegacyUserEntity entity =
                repository
                        .findByUserId(memberId)
                        .orElseThrow(
                                () ->
                                        new MemberNotFoundException(
                                                "Phone auth not found for memberId: " + memberId));
        return toMemberAuth(entity);
    }

    private MemberAuth toMemberAuth(LegacyUserEntity entity) {
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());
        PasswordHash passwordHash =
                entity.getPasswordHash() != null ? PasswordHash.of(entity.getPasswordHash()) : null;
        return MemberAuth.reconstitute(
                MemberAuthId.of(entity.getId()),
                MemberId.of(entity.getId()),
                AuthProvider.PHONE,
                ProviderUserId.of(entity.getPhoneNumber()),
                passwordHash,
                DeletionStatus.active(),
                createdAt,
                updatedAt);
    }

    private static final ZoneId LEGACY_DB_ZONE = ZoneId.of("Asia/Seoul");

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(LEGACY_DB_ZONE).toInstant();
    }
}
