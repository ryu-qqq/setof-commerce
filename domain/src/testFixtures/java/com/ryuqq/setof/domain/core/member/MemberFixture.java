package com.ryuqq.setof.domain.core.member;

import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import com.ryuqq.setof.domain.core.member.vo.MemberIdFixture;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.MemberStatus;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalInfo;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Member TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Member 인스턴스 생성을 위한 팩토리 클래스
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * Member localMember = MemberFixture.createLocalMember();
 * Member kakaoMember = MemberFixture.createKakaoMember();
 * Member withdrawnMember = MemberFixture.createWithdrawnMember();
 * }</pre>
 */
public final class MemberFixture {

    public static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2025, 1, 1, 9, 0, 0);

    private MemberFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 LOCAL 회원 생성 (forNew)
     *
     * @return Member 인스턴스
     */
    public static Member createLocalMember() {
        return Member.forNew(
                PhoneNumber.of("01012345678"),
                Email.of("test@example.com"),
                Password.of("$2a$10$hashedPassword"),
                MemberName.of("홍길동"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                FIXED_CLOCK);
    }

    /**
     * 기본 KAKAO 회원 생성 (forNew)
     *
     * @return Member 인스턴스
     */
    public static Member createKakaoMember() {
        return Member.forNew(
                PhoneNumber.of("01087654321"),
                null,
                null,
                MemberName.of("카카오"),
                LocalDate.of(1995, 5, 15),
                Gender.W,
                AuthProvider.KAKAO,
                SocialId.of("kakao_12345678"),
                Consent.of(true, true, true),
                FIXED_CLOCK);
    }

    /**
     * ID가 포함된 LOCAL 회원 생성 (of)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createLocalMemberWithId(String memberId) {
        return Member.of(
                MemberId.of(memberId),
                PhoneNumber.of("01012345678"),
                Email.of("test@example.com"),
                Password.of("$2a$10$hashedPassword"),
                MemberName.of("홍길동"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                FIXED_CLOCK);
    }

    /**
     * ID가 포함된 KAKAO 회원 생성 (of)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createKakaoMemberWithId(String memberId) {
        return Member.of(
                MemberId.of(memberId),
                PhoneNumber.of("01087654321"),
                null,
                null,
                MemberName.of("카카오"),
                LocalDate.of(1995, 5, 15),
                Gender.W,
                AuthProvider.KAKAO,
                SocialId.of("kakao_12345678"),
                Consent.of(true, true, true),
                FIXED_CLOCK);
    }

    /**
     * 탈퇴한 회원 생성 (reconstitute)
     *
     * @return Member 인스턴스
     */
    public static Member createWithdrawnMember() {
        return Member.reconstitute(
                MemberIdFixture.createWithSequence(999),
                PhoneNumber.of("01099998888"),
                Email.of("withdrawn@example.com"),
                Password.of("$2a$10$withdrawnHash"),
                MemberName.of("탈퇴함"),
                LocalDate.of(1985, 12, 25),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                MemberStatus.WITHDRAWN,
                Consent.of(true, true, false),
                WithdrawalInfo.of(WithdrawalReason.RARELY_USED, FIXED_TIME),
                FIXED_TIME.minusYears(1),
                FIXED_TIME);
    }

    /**
     * Persistence에서 복원된 회원 생성 (reconstitute)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createReconstitutedMember(String memberId) {
        return Member.reconstitute(
                MemberId.of(memberId),
                PhoneNumber.of("01012345678"),
                Email.of("test@example.com"),
                Password.of("$2a$10$hashedPassword"),
                MemberName.of("홍길동"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                MemberStatus.ACTIVE,
                Consent.of(true, true, false),
                null,
                FIXED_TIME.minusYears(1),
                FIXED_TIME);
    }

    /**
     * 카카오 연동된 회원 생성 (reconstitute) LOCAL에서 KAKAO로 연동된 상태
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createKakaoLinkedMember(String memberId) {
        return Member.reconstitute(
                MemberId.of(memberId),
                PhoneNumber.of("01012345678"),
                Email.of("linked@example.com"),
                Password.of("$2a$10$originalHash"),
                MemberName.of("연동됨"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.KAKAO,
                SocialId.of("kakao_linked_12345"),
                MemberStatus.ACTIVE,
                Consent.of(true, true, true),
                null,
                FIXED_TIME.minusYears(2),
                FIXED_TIME);
    }

    /**
     * 정지된 회원 생성 (reconstitute)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createSuspendedMember(String memberId) {
        return Member.reconstitute(
                MemberId.of(memberId),
                PhoneNumber.of("01055556666"),
                Email.of("suspended@example.com"),
                Password.of("$2a$10$suspendedHash"),
                MemberName.of("정지됨"),
                LocalDate.of(1988, 3, 15),
                Gender.W,
                AuthProvider.LOCAL,
                null,
                MemberStatus.SUSPENDED,
                Consent.of(true, true, false),
                null,
                FIXED_TIME.minusYears(3),
                FIXED_TIME);
    }

    /**
     * 휴면(비활성) 회원 생성 (reconstitute)
     *
     * @return Member 인스턴스
     */
    public static Member createInactiveMember() {
        return createInactiveMember("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");
    }

    /**
     * 휴면(비활성) 회원 생성 (reconstitute)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Member 인스턴스
     */
    public static Member createInactiveMember(String memberId) {
        return Member.reconstitute(
                MemberId.of(memberId),
                PhoneNumber.of("01044445555"),
                Email.of("inactive@example.com"),
                Password.of("$2a$10$inactiveHash"),
                MemberName.of("휴면회원"),
                LocalDate.of(1992, 6, 20),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                MemberStatus.INACTIVE,
                Consent.of(true, true, false),
                null,
                FIXED_TIME.minusYears(2),
                FIXED_TIME);
    }

    /**
     * 카카오 ID와 함께 KAKAO 회원 생성 (forNew)
     *
     * @param kakaoId 카카오 소셜 ID
     * @return Member 인스턴스
     */
    public static Member createKakaoMemberWithSocialId(String kakaoId) {
        return Member.forNew(
                PhoneNumber.of("01087654321"),
                Email.of("kakao@example.com"),
                null,
                MemberName.of("카카오"),
                LocalDate.of(1995, 5, 15),
                Gender.W,
                AuthProvider.KAKAO,
                SocialId.of(kakaoId),
                Consent.of(true, true, true),
                FIXED_CLOCK);
    }

    /**
     * 순차적 회원 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호
     * @return Member 인스턴스
     */
    public static Member createWithSequence(int sequence) {
        return Member.of(
                MemberIdFixture.createWithSequence(sequence),
                PhoneNumber.of(String.format("010%08d", sequence)),
                Email.of(String.format("user%d@example.com", sequence)),
                Password.of("$2a$10$hash" + sequence),
                MemberName.of("회원" + sequence),
                LocalDate.of(1990, 1, 1),
                Gender.N,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                FIXED_CLOCK);
    }
}
