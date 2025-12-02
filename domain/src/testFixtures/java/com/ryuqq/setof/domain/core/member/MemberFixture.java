package com.ryuqq.setof.domain.core.member;

import com.ryuqq.setof.domain.core.member.type.AuthProvider;
import com.ryuqq.setof.domain.core.member.type.Gender;
import com.ryuqq.setof.domain.core.member.type.MemberStatus;
import com.ryuqq.setof.domain.core.member.type.WithdrawalReason;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalInfo;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Member TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Member 인스턴스 생성을 위한 팩토리 클래스</p>
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * Member localMember = MemberFixture.createLocalMember();
 * Member kakaoMember = MemberFixture.createKakaoMember();
 * Member withdrawnMember = MemberFixture.createWithdrawnMember();
 * }</pre>
 */
public final class MemberFixture {

    public static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2025-01-01T00:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

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
                FIXED_CLOCK
        );
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
                FIXED_CLOCK
        );
    }

    /**
     * ID가 포함된 LOCAL 회원 생성 (of)
     *
     * @param id 회원 ID
     * @return Member 인스턴스
     */
    public static Member createLocalMemberWithId(Long id) {
        return Member.of(
                MemberId.of(id),
                PhoneNumber.of("01012345678"),
                Email.of("test@example.com"),
                Password.of("$2a$10$hashedPassword"),
                MemberName.of("홍길동"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                FIXED_CLOCK
        );
    }

    /**
     * ID가 포함된 KAKAO 회원 생성 (of)
     *
     * @param id 회원 ID
     * @return Member 인스턴스
     */
    public static Member createKakaoMemberWithId(Long id) {
        return Member.of(
                MemberId.of(id),
                PhoneNumber.of("01087654321"),
                null,
                null,
                MemberName.of("카카오"),
                LocalDate.of(1995, 5, 15),
                Gender.W,
                AuthProvider.KAKAO,
                SocialId.of("kakao_12345678"),
                Consent.of(true, true, true),
                FIXED_CLOCK
        );
    }

    /**
     * 탈퇴한 회원 생성 (reconstitute)
     *
     * @return Member 인스턴스
     */
    public static Member createWithdrawnMember() {
        return Member.reconstitute(
                MemberId.of(999L),
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
                FIXED_TIME
        );
    }

    /**
     * Persistence에서 복원된 회원 생성 (reconstitute)
     *
     * @param id 회원 ID
     * @return Member 인스턴스
     */
    public static Member createReconstitutedMember(Long id) {
        return Member.reconstitute(
                MemberId.of(id),
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
                FIXED_TIME
        );
    }

    /**
     * 카카오 연동된 회원 생성 (reconstitute)
     * LOCAL에서 KAKAO로 연동된 상태
     *
     * @param id 회원 ID
     * @return Member 인스턴스
     */
    public static Member createKakaoLinkedMember(Long id) {
        return Member.reconstitute(
                MemberId.of(id),
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
                FIXED_TIME
        );
    }

    /**
     * 정지된 회원 생성 (reconstitute)
     *
     * @param id 회원 ID
     * @return Member 인스턴스
     */
    public static Member createSuspendedMember(Long id) {
        return Member.reconstitute(
                MemberId.of(id),
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
                FIXED_TIME
        );
    }

    /**
     * 순차적 회원 생성 (테스트용)
     *
     * @param sequence 시퀀스 번호
     * @return Member 인스턴스
     */
    public static Member createWithSequence(int sequence) {
        return Member.of(
                MemberId.of((long) sequence),
                PhoneNumber.of(String.format("010%08d", sequence)),
                Email.of(String.format("user%d@example.com", sequence)),
                Password.of("$2a$10$hash" + sequence),
                MemberName.of("회원" + sequence),
                LocalDate.of(1990, 1, 1),
                Gender.N,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                FIXED_CLOCK
        );
    }
}
