package com.ryuqq.setof.domain.member;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Email;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.id.MemberAuthId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.DateOfBirth;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import java.time.LocalDate;

/**
 * Member 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Member 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class MemberFixtures {

    private MemberFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_MEMBER_ID = "01900000-0000-7000-8000-000000000099";
    public static final long DEFAULT_LEGACY_MEMBER_ID = 1001L;
    public static final Long DEFAULT_MEMBER_AUTH_ID = 10L;

    // ===== MemberId Fixtures =====
    public static MemberId defaultMemberId() {
        return MemberId.of(DEFAULT_MEMBER_ID);
    }

    public static MemberId memberId(String value) {
        return MemberId.of(value);
    }

    // ===== MemberAuthId Fixtures =====
    public static MemberAuthId defaultMemberAuthId() {
        return MemberAuthId.of(DEFAULT_MEMBER_AUTH_ID);
    }

    public static MemberAuthId memberAuthId(Long value) {
        return MemberAuthId.of(value);
    }

    public static MemberAuthId newMemberAuthId() {
        return MemberAuthId.forNew();
    }

    // ===== MemberName Fixtures =====
    public static MemberName defaultMemberName() {
        return MemberName.of("홍길동");
    }

    public static MemberName memberName(String value) {
        return MemberName.of(value);
    }

    // ===== Email Fixtures =====
    public static Email defaultEmail() {
        return Email.of("test@example.com");
    }

    public static Email email(String value) {
        return Email.of(value);
    }

    // ===== DateOfBirth Fixtures =====
    public static DateOfBirth defaultDateOfBirth() {
        return DateOfBirth.of(LocalDate.of(1990, 1, 1));
    }

    public static DateOfBirth dateOfBirth(int year, int month, int day) {
        return DateOfBirth.of(LocalDate.of(year, month, day));
    }

    // ===== LegacyMemberId Fixtures =====
    public static LegacyMemberId defaultLegacyMemberId() {
        return LegacyMemberId.of(DEFAULT_LEGACY_MEMBER_ID);
    }

    public static LegacyMemberId legacyMemberId(long value) {
        return LegacyMemberId.of(value);
    }

    // ===== PasswordHash Fixtures =====
    public static PasswordHash defaultPasswordHash() {
        return PasswordHash.of("$2a$10$hashedPasswordValue");
    }

    public static PasswordHash passwordHash(String value) {
        return PasswordHash.of(value);
    }

    // ===== ProviderUserId Fixtures =====
    public static ProviderUserId defaultPhoneProviderUserId() {
        return ProviderUserId.of("010-1234-5678");
    }

    public static ProviderUserId defaultKakaoProviderUserId() {
        return ProviderUserId.of("kakao_123456789");
    }

    public static ProviderUserId providerUserId(String value) {
        return ProviderUserId.of(value);
    }

    // ===== Member Aggregate Fixtures =====

    /** 신규 회원 (forNew, ACTIVE 상태) */
    public static Member newMember() {
        return Member.forNew(
                defaultMemberId(),
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                CommonVoFixtures.now());
    }

    /** 신규 회원 (지정 MemberId) */
    public static Member newMember(MemberId memberId) {
        return Member.forNew(
                memberId,
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                CommonVoFixtures.now());
    }

    /** 레거시 마이그레이션 회원 */
    public static Member migratedMember() {
        return Member.forMigration(
                defaultMemberId(),
                defaultLegacyMemberId(),
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                CommonVoFixtures.now());
    }

    /** 활성 상태 회원 (영속성 복원) */
    public static Member activeMember() {
        return Member.reconstitute(
                defaultMemberId(),
                null,
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                MemberStatus.ACTIVE,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 정지 상태 회원 */
    public static Member suspendedMember() {
        return Member.reconstitute(
                MemberId.of("01900000-0000-7000-8000-000000000002"),
                null,
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.FEMALE,
                MemberStatus.SUSPENDED,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 탈퇴 상태 회원 */
    public static Member withdrawnMember() {
        return Member.reconstitute(
                MemberId.of("01900000-0000-7000-8000-000000000003"),
                null,
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.OTHER,
                MemberStatus.WITHDRAWN,
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 소프트 삭제된 회원 */
    public static Member deletedMember() {
        return Member.reconstitute(
                MemberId.of("01900000-0000-7000-8000-000000000004"),
                null,
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                MemberStatus.ACTIVE,
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 레거시 마이그레이션된 활성 회원 (영속성 복원) */
    public static Member activeMigratedMember() {
        return Member.reconstitute(
                defaultMemberId(),
                defaultLegacyMemberId(),
                defaultMemberName(),
                defaultEmail(),
                CommonVoFixtures.defaultPhoneNumber(),
                defaultDateOfBirth(),
                Gender.MALE,
                MemberStatus.ACTIVE,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== MemberAuth Aggregate Fixtures =====

    /** 전화번호 인증 MemberAuth (신규) */
    public static MemberAuth newPhoneMemberAuth() {
        return MemberAuth.forPhoneAuth(
                defaultMemberId(),
                defaultPhoneProviderUserId(),
                defaultPasswordHash(),
                CommonVoFixtures.now());
    }

    /** 카카오 소셜 인증 MemberAuth (신규) */
    public static MemberAuth newKakaoMemberAuth() {
        return MemberAuth.forSocialAuth(
                defaultMemberId(),
                AuthProvider.KAKAO,
                defaultKakaoProviderUserId(),
                CommonVoFixtures.now());
    }

    /** 활성 전화번호 인증 MemberAuth (영속성 복원) */
    public static MemberAuth activePhoneMemberAuth() {
        return MemberAuth.reconstitute(
                defaultMemberAuthId(),
                defaultMemberId(),
                AuthProvider.PHONE,
                defaultPhoneProviderUserId(),
                defaultPasswordHash(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 활성 카카오 소셜 인증 MemberAuth (영속성 복원) */
    public static MemberAuth activeKakaoMemberAuth() {
        return MemberAuth.reconstitute(
                MemberAuthId.of(20L),
                defaultMemberId(),
                AuthProvider.KAKAO,
                defaultKakaoProviderUserId(),
                null,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 삭제된 MemberAuth (Soft Delete 상태) */
    public static MemberAuth deletedMemberAuth() {
        return MemberAuth.reconstitute(
                MemberAuthId.of(30L),
                defaultMemberId(),
                AuthProvider.PHONE,
                defaultPhoneProviderUserId(),
                defaultPasswordHash(),
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }
}
