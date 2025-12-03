package com.ryuqq.setof.domain.core.member;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.core.member.exception.KakaoMemberCannotChangePasswordException;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Member Aggregate")
class MemberTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    @Nested
    @DisplayName("forNew() - 신규 회원 생성")
    class ForNew {

        @Test
        @DisplayName("LOCAL 회원을 생성할 수 있다 - UUID v7 ID가 자동 생성됨")
        void shouldCreateLocalMemberWithForNew() {
            // given
            PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
            Email email = Email.of("test@example.com");
            Password password = Password.of("$2a$10$hashedPassword");
            MemberName name = MemberName.of("홍길동");
            LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
            Gender gender = Gender.M;
            AuthProvider provider = AuthProvider.LOCAL;
            Consent consent = Consent.of(true, true, false);

            // when
            Member member =
                    Member.forNew(
                            phoneNumber,
                            email,
                            password,
                            name,
                            dateOfBirth,
                            gender,
                            provider,
                            null,
                            consent,
                            FIXED_CLOCK);

            // then
            assertNotNull(member.getId()); // UUID v7이 자동 생성됨
            assertNotNull(member.getIdValue());
            assertEquals("01012345678", member.getPhoneNumberValue());
            assertEquals("test@example.com", member.getEmailValue());
            assertEquals("$2a$10$hashedPassword", member.getPasswordValue());
            assertEquals("홍길동", member.getNameValue());
            assertEquals(dateOfBirth, member.getDateOfBirth());
            assertEquals(Gender.M, member.getGender());
            assertEquals(AuthProvider.LOCAL, member.getProvider());
            assertNull(member.getSocialId());
            assertEquals(MemberStatus.ACTIVE, member.getStatus());
            assertNotNull(member.getConsent());
            assertNull(member.getWithdrawalInfo());
            assertNotNull(member.getCreatedAt());
            assertNotNull(member.getUpdatedAt());
        }

        @Test
        @DisplayName("KAKAO 회원을 생성할 수 있다")
        void shouldCreateKakaoMemberWithForNew() {
            // given
            PhoneNumber phoneNumber = PhoneNumber.of("01087654321");
            MemberName name = MemberName.of("카카오");
            LocalDate dateOfBirth = LocalDate.of(1995, 5, 15);
            Gender gender = Gender.W;
            AuthProvider provider = AuthProvider.KAKAO;
            SocialId socialId = SocialId.of("kakao_12345678");
            Consent consent = Consent.of(true, true, true);

            // when
            Member member =
                    Member.forNew(
                            phoneNumber,
                            null,
                            null,
                            name,
                            dateOfBirth,
                            gender,
                            provider,
                            socialId,
                            consent,
                            FIXED_CLOCK);

            // then
            assertNotNull(member.getId()); // UUID v7이 자동 생성됨
            assertEquals("01087654321", member.getPhoneNumberValue());
            assertNull(member.getEmailValue());
            assertNull(member.getPassword());
            assertEquals("카카오", member.getNameValue());
            assertEquals(AuthProvider.KAKAO, member.getProvider());
            assertEquals("kakao_12345678", member.getSocialIdValue());
            assertEquals(MemberStatus.ACTIVE, member.getStatus());
        }
    }

    @Nested
    @DisplayName("of() - ID 포함 회원 생성")
    class Of {

        @Test
        @DisplayName("ID가 포함된 회원을 생성할 수 있다")
        void shouldCreateMemberWithOf() {
            // given
            String uuidString = "01234567-89ab-7cde-8000-000000000001";
            MemberId id = MemberId.of(uuidString);
            PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
            Email email = Email.of("test@example.com");
            Password password = Password.of("$2a$10$hashedPassword");
            MemberName name = MemberName.of("홍길동");
            LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
            Gender gender = Gender.M;
            AuthProvider provider = AuthProvider.LOCAL;
            Consent consent = Consent.of(true, true, false);

            // when
            Member member =
                    Member.of(
                            id,
                            phoneNumber,
                            email,
                            password,
                            name,
                            dateOfBirth,
                            gender,
                            provider,
                            null,
                            consent,
                            FIXED_CLOCK);

            // then
            assertNotNull(member.getId());
            assertEquals(uuidString, member.getIdValue());
            assertEquals("01012345678", member.getPhoneNumberValue());
            assertEquals(MemberStatus.ACTIVE, member.getStatus());
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteMemberFromPersistence() {
            // given
            String uuidString = "01234567-89ab-7cde-8000-000000000100";
            MemberId id = MemberId.of(uuidString);
            PhoneNumber phoneNumber = PhoneNumber.of("01099998888");
            Email email = Email.of("restored@example.com");
            Password password = Password.of("$2a$10$restoredHash");
            MemberName name = MemberName.of("복원됨");
            LocalDate dateOfBirth = LocalDate.of(1985, 3, 20);
            Gender gender = Gender.N;
            AuthProvider provider = AuthProvider.KAKAO;
            SocialId socialId = SocialId.of("kakao_99999");
            MemberStatus status = MemberStatus.SUSPENDED;
            Consent consent = Consent.of(true, true, true);
            WithdrawalInfo withdrawalInfo = null;
            LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
            LocalDateTime updatedAt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);

            // when
            Member member =
                    Member.reconstitute(
                            id,
                            phoneNumber,
                            email,
                            password,
                            name,
                            dateOfBirth,
                            gender,
                            provider,
                            socialId,
                            status,
                            consent,
                            withdrawalInfo,
                            createdAt,
                            updatedAt);

            // then
            assertEquals(uuidString, member.getIdValue());
            assertEquals("01099998888", member.getPhoneNumberValue());
            assertEquals("restored@example.com", member.getEmailValue());
            assertEquals("복원됨", member.getNameValue());
            assertEquals(MemberStatus.SUSPENDED, member.getStatus());
            assertEquals(AuthProvider.KAKAO, member.getProvider());
            assertEquals("kakao_99999", member.getSocialIdValue());
            assertEquals(createdAt, member.getCreatedAt());
            assertEquals(updatedAt, member.getUpdatedAt());
        }

        @Test
        @DisplayName("탈퇴 회원을 복원할 수 있다")
        void shouldReconstituteWithdrawnMember() {
            // given
            String uuidString = "01234567-89ab-7cde-8000-000000000200";
            MemberId id = MemberId.of(uuidString);
            PhoneNumber phoneNumber = PhoneNumber.of("01011112222");
            MemberName name = MemberName.of("탈퇴함");
            LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
            Consent consent = Consent.of(true, true, false);
            WithdrawalInfo withdrawalInfo =
                    WithdrawalInfo.of(
                            WithdrawalReason.RARELY_USED, LocalDateTime.of(2024, 12, 1, 12, 0, 0));
            LocalDateTime createdAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
            LocalDateTime updatedAt = LocalDateTime.of(2024, 12, 1, 12, 0, 0);

            // when
            Member member =
                    Member.reconstitute(
                            id,
                            phoneNumber,
                            null,
                            null,
                            name,
                            dateOfBirth,
                            Gender.M,
                            AuthProvider.LOCAL,
                            null,
                            MemberStatus.WITHDRAWN,
                            consent,
                            withdrawalInfo,
                            createdAt,
                            updatedAt);

            // then
            assertEquals(MemberStatus.WITHDRAWN, member.getStatus());
            assertNotNull(member.getWithdrawalInfo());
            assertTrue(member.isWithdrawn());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 직접 반환한다")
        void shouldReturnIdValueDirectly() {
            // given
            Member member = createLocalMember();

            // then
            assertNotNull(member.getIdValue()); // forNew()로 생성 시 UUID v7 자동 생성
        }

        @Test
        @DisplayName("getPhoneNumberValue()는 휴대폰 번호를 직접 반환한다")
        void shouldReturnPhoneNumberValueDirectly() {
            // given
            Member member = createLocalMember();

            // then
            assertEquals("01012345678", member.getPhoneNumberValue());
        }

        @Test
        @DisplayName("getSocialIdValue()는 소셜 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenSocialIdIsNull() {
            // given
            Member member = createLocalMember();

            // then
            assertNull(member.getSocialIdValue());
        }

        @Test
        @DisplayName("getSocialIdValue()는 소셜 ID 값을 직접 반환한다")
        void shouldReturnSocialIdValueDirectly() {
            // given
            Member member = createKakaoMember();

            // then
            assertEquals("kakao_12345678", member.getSocialIdValue());
        }
    }

    @Nested
    @DisplayName("withdraw() - 회원 탈퇴")
    class Withdraw {

        @Test
        @DisplayName("활성 회원을 탈퇴 처리할 수 있다")
        void shouldWithdrawMember() {
            // given
            Member member = createLocalMember();
            WithdrawalReason reason = WithdrawalReason.RARELY_USED;

            // when
            member.withdraw(reason, FIXED_CLOCK);

            // then
            assertEquals(MemberStatus.WITHDRAWN, member.getStatus());
            assertNotNull(member.getWithdrawalInfo());
            assertTrue(member.isWithdrawn());
            assertFalse(member.isActive());
        }

        @Test
        @DisplayName("이미 탈퇴한 회원은 다시 탈퇴할 수 없다")
        void shouldThrowExceptionWhenAlreadyWithdrawn() {
            // given
            Member member = createLocalMember();
            member.withdraw(WithdrawalReason.OTHER, FIXED_CLOCK);

            // when & then
            assertThrows(
                    AlreadyWithdrawnMemberException.class,
                    () -> member.withdraw(WithdrawalReason.PRIVACY_CONCERN, FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("linkKakao() - 카카오 연동")
    class LinkKakao {

        @Test
        @DisplayName("LOCAL 회원을 카카오로 연동할 수 있다")
        void shouldLinkKakaoToLocalMember() {
            // given
            Member member = createLocalMember();
            SocialId kakaoSocialId = SocialId.of("kakao_new_12345");

            // when
            member.linkKakao(kakaoSocialId, FIXED_CLOCK);

            // then
            assertEquals(AuthProvider.KAKAO, member.getProvider());
            assertEquals("kakao_new_12345", member.getSocialIdValue());
            assertTrue(member.isKakaoMember());
            assertFalse(member.isLocalMember());
        }

        @Test
        @DisplayName("이미 카카오 회원은 다시 연동할 수 없다")
        void shouldThrowExceptionWhenAlreadyKakaoMember() {
            // given
            Member member = createKakaoMember();
            SocialId newSocialId = SocialId.of("kakao_another");

            // when & then
            assertThrows(
                    AlreadyKakaoMemberException.class,
                    () -> member.linkKakao(newSocialId, FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("changePassword() - 비밀번호 변경")
    class ChangePassword {

        @Test
        @DisplayName("LOCAL 회원은 비밀번호를 변경할 수 있다")
        void shouldChangePasswordForLocalMember() {
            // given
            Member member = createLocalMember();
            Password newPassword = Password.of("$2a$10$newHashedPassword");

            // when
            member.changePassword(newPassword, FIXED_CLOCK);

            // then
            assertEquals("$2a$10$newHashedPassword", member.getPasswordValue());
        }

        @Test
        @DisplayName("KAKAO 회원은 비밀번호를 변경할 수 없다")
        void shouldThrowExceptionWhenKakaoMemberChangesPassword() {
            // given
            Member member = createKakaoMember();
            Password newPassword = Password.of("$2a$10$newHash");

            // when & then
            assertThrows(
                    KakaoMemberCannotChangePasswordException.class,
                    () -> member.changePassword(newPassword, FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isLocalMember()는 LOCAL 회원이면 true를 반환한다")
        void shouldReturnTrueForLocalMember() {
            // given
            Member member = createLocalMember();

            // then
            assertTrue(member.isLocalMember());
            assertFalse(member.isKakaoMember());
        }

        @Test
        @DisplayName("isKakaoMember()는 KAKAO 회원이면 true를 반환한다")
        void shouldReturnTrueForKakaoMember() {
            // given
            Member member = createKakaoMember();

            // then
            assertTrue(member.isKakaoMember());
            assertFalse(member.isLocalMember());
        }

        @Test
        @DisplayName("isActive()는 활성 회원이면 true를 반환한다")
        void shouldReturnTrueForActiveMember() {
            // given
            Member member = createLocalMember();

            // then
            assertTrue(member.isActive());
            assertFalse(member.isWithdrawn());
        }

        @Test
        @DisplayName("isWithdrawn()은 탈퇴 회원이면 true를 반환한다")
        void shouldReturnTrueForWithdrawnMember() {
            // given
            Member member = createLocalMember();
            member.withdraw(WithdrawalReason.OTHER, FIXED_CLOCK);

            // then
            assertTrue(member.isWithdrawn());
            assertFalse(member.isActive());
        }
    }

    // ========== Helper Methods ==========

    private Member createLocalMember() {
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

    private Member createKakaoMember() {
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
}
