package com.ryuqq.setof.storage.legacy.user.adapter;

import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import com.ryuqq.setof.storage.legacy.user.mapper.LegacyMemberEntityMapper;
import com.ryuqq.setof.storage.legacy.user.repository.LegacyMemberJpaRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/**
 * LegacyMemberCommandAdapter - 레거시 회원 명령 Adapter.
 *
 * <p>Application Layer의 MemberCommandPort를 구현하는 Adapter입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class LegacyMemberCommandAdapter implements MemberCommandPort {

    private final LegacyMemberJpaRepository jpaRepository;
    private final LegacyMemberEntityMapper mapper;

    public LegacyMemberCommandAdapter(
            LegacyMemberJpaRepository jpaRepository, LegacyMemberEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(Member member, MemberRegistrationInfo registrationInfo) {
        LegacyUserEntity entity = mapper.toEntity(member, registrationInfo);
        LegacyUserEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    @Override
    public void persistWithdrawal(long userId, String withdrawalReason) {
        LegacyUserEntity entity = findEntityById(userId);
        entity.withdrawal(withdrawalReason);
    }

    @Override
    public void persistPasswordChange(long userId, String encodedPassword) {
        LegacyUserEntity entity = findEntityById(userId);
        entity.resetPassword(encodedPassword);
    }

    @Override
    public void persistSocialIntegration(
            long userId,
            String socialLoginType,
            String socialPkId,
            String gender,
            String dateOfBirth) {
        LegacyUserEntity entity = findEntityById(userId);
        entity.integrateSocial(
                parseSocialLoginType(socialLoginType),
                socialPkId,
                parseGender(gender),
                parseDateOfBirth(dateOfBirth));
    }

    private LegacyUserEntity findEntityById(long userId) {
        return jpaRepository
                .findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(userId)));
    }

    private LegacyUserEntity.SocialLoginType parseSocialLoginType(String socialLoginType) {
        if (socialLoginType == null || socialLoginType.isBlank()) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
        try {
            return LegacyUserEntity.SocialLoginType.valueOf(socialLoginType);
        } catch (IllegalArgumentException e) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
    }

    private LegacyUserEntity.Gender parseGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return LegacyUserEntity.Gender.N;
        }
        try {
            return LegacyUserEntity.Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            return LegacyUserEntity.Gender.N;
        }
    }

    private LocalDate parseDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }
}
