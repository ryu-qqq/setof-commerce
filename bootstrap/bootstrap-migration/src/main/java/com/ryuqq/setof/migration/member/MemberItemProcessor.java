package com.ryuqq.setof.migration.member;

import com.github.f4b6a3.uuid.UuidCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Member 데이터 변환 Processor
 *
 * <p>레거시 USERS 데이터를 신규 members 데이터로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberItemProcessor implements ItemProcessor<LegacyUserDto, MemberMigrationData> {

    private static final Logger log = LoggerFactory.getLogger(MemberItemProcessor.class);

    @Override
    public MemberMigrationData process(LegacyUserDto legacyUser) {
        // UUID v7 생성 (시간 순서 보장)
        var newMemberId = UuidCreator.getTimeOrderedEpoch();

        log.debug("Processing user: legacyId={}, newId={}", legacyUser.userId(), newMemberId);

        return new MemberMigrationData(
                newMemberId,
                legacyUser.userId(),
                legacyUser.socialPkId(),
                normalizePhoneNumber(legacyUser.phoneNumber()),
                normalizeEmail(legacyUser.email()),
                legacyUser.passwordHash(),
                legacyUser.name(),
                legacyUser.dateOfBirth(),
                normalizeGender(legacyUser.gender()),
                normalizeProvider(legacyUser.provider()),
                normalizeStatus(legacyUser.status()),
                legacyUser.privacyConsent(),
                legacyUser.serviceTermsConsent(),
                legacyUser.adConsent(),
                legacyUser.withdrawalReason(),
                legacyUser.withdrawnAt(),
                legacyUser.createdAt(),
                legacyUser.updatedAt(),
                legacyUser.deletedAt());
    }

    /** 휴대폰 번호 정규화 */
    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        // 숫자만 추출
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    /** 이메일 정규화 */
    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    /** 성별 정규화 */
    private String normalizeGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return "OTHER";
        }
        return switch (gender.toUpperCase()) {
            case "M", "MALE" -> "MALE";
            case "F", "FEMALE" -> "FEMALE";
            default -> "OTHER";
        };
    }

    /** 인증 제공자 정규화 */
    private String normalizeProvider(String provider) {
        if (provider == null || provider.isBlank()) {
            return "LOCAL";
        }
        return provider.toUpperCase();
    }

    /** 회원 상태 정규화 */
    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "ACTIVE";
        }
        return status.toUpperCase();
    }
}
