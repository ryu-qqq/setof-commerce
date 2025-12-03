package com.ryuqq.setof.application.member.component;

import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.port.out.PasswordEncoderPort;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import com.ryuqq.setof.domain.core.member.vo.WithdrawalReason;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberUpdater")
@ExtendWith(MockitoExtension.class)
class MemberUpdaterTest {

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private Member mockMember;

    private Clock fixedClock;
    private MemberUpdater memberUpdater;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));
        memberUpdater = new MemberUpdater(passwordEncoderPort, fixedClock);
    }

    @Nested
    @DisplayName("changePassword")
    class ChangePasswordTest {

        @Test
        @DisplayName("비밀번호 변경 성공")
        void shouldChangePassword() {
            // Given
            String newRawPassword = "newPassword123!";
            String hashedPassword = "$2a$10$newHashedPassword";

            when(passwordEncoderPort.encode(newRawPassword)).thenReturn(hashedPassword);

            // When
            memberUpdater.changePassword(mockMember, newRawPassword);

            // Then
            verify(passwordEncoderPort).encode(newRawPassword);
            verify(mockMember).changePassword(any(Password.class), eq(fixedClock));
        }

        @Test
        @DisplayName("비밀번호 해싱 후 Member에 전달")
        void shouldHashPasswordBeforeChanging() {
            // Given
            String rawPassword = "plainText!@#";
            String hashedPassword = "$2a$10$crypted";

            when(passwordEncoderPort.encode(rawPassword)).thenReturn(hashedPassword);

            // When
            memberUpdater.changePassword(mockMember, rawPassword);

            // Then
            verify(passwordEncoderPort).encode(rawPassword);
            verify(mockMember).changePassword(argThat(password ->
                    password.value().equals(hashedPassword)), eq(fixedClock));
        }
    }

    @Nested
    @DisplayName("withdraw")
    class WithdrawTest {

        @Test
        @DisplayName("회원 탈퇴 처리 성공 - RARELY_USED")
        void shouldWithdrawWithRarelyUsedReason() {
            // Given
            String reason = "RARELY_USED";

            // When
            memberUpdater.withdraw(mockMember, reason);

            // Then
            verify(mockMember).withdraw(WithdrawalReason.RARELY_USED, fixedClock);
        }

        @Test
        @DisplayName("회원 탈퇴 처리 성공 - OTHER")
        void shouldWithdrawWithOtherReason() {
            // Given
            String reason = "OTHER";

            // When
            memberUpdater.withdraw(mockMember, reason);

            // Then
            verify(mockMember).withdraw(WithdrawalReason.OTHER, fixedClock);
        }

        @Test
        @DisplayName("회원 탈퇴 처리 성공 - SERVICE_DISSATISFIED")
        void shouldWithdrawWithServiceDissatisfiedReason() {
            // Given
            String reason = "SERVICE_DISSATISFIED";

            // When
            memberUpdater.withdraw(mockMember, reason);

            // Then
            verify(mockMember).withdraw(WithdrawalReason.SERVICE_DISSATISFIED, fixedClock);
        }
    }

    @Nested
    @DisplayName("linkKakaoWithProfile")
    class LinkKakaoWithProfileTest {

        @Test
        @DisplayName("카카오 연동 및 프로필 업데이트 성공 - 전체 정보")
        void shouldLinkKakaoWithFullProfile() {
            // Given
            IntegrateKakaoCommand command = new IntegrateKakaoCommand(
                    "member-id",
                    "kakao_12345",
                    "kakao@example.com",
                    "카카오이름",
                    LocalDate.of(1990, 1, 1),
                    "M");

            // When
            memberUpdater.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember).linkKakaoWithProfile(
                    any(SocialId.class),
                    any(Email.class),
                    any(MemberName.class),
                    eq(LocalDate.of(1990, 1, 1)),
                    eq(Gender.M),
                    eq(fixedClock));
        }

        @Test
        @DisplayName("카카오 연동 - 프로필 없이")
        void shouldLinkKakaoWithoutProfile() {
            // Given
            IntegrateKakaoCommand command = IntegrateKakaoCommand.withoutProfile("member-id", "kakao_12345");

            // When
            memberUpdater.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember).linkKakaoWithProfile(
                    any(SocialId.class),
                    isNull(),
                    isNull(),
                    isNull(),
                    isNull(),
                    eq(fixedClock));
        }

        @Test
        @DisplayName("카카오 연동 - 여성 성별")
        void shouldLinkKakaoWithFemaleGender() {
            // Given
            IntegrateKakaoCommand command = new IntegrateKakaoCommand(
                    "member-id",
                    "kakao_12345",
                    "kakao@example.com",
                    "카카오이름",
                    LocalDate.of(1995, 5, 15),
                    "W");

            // When
            memberUpdater.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember).linkKakaoWithProfile(
                    any(SocialId.class),
                    any(Email.class),
                    any(MemberName.class),
                    eq(LocalDate.of(1995, 5, 15)),
                    eq(Gender.W),
                    eq(fixedClock));
        }

        @Test
        @DisplayName("카카오 연동 - 소문자 성별 변환")
        void shouldConvertLowercaseGender() {
            // Given
            IntegrateKakaoCommand command = new IntegrateKakaoCommand(
                    "member-id",
                    "kakao_12345",
                    "test@example.com",
                    "테스트",
                    LocalDate.of(1990, 1, 1),
                    "m");

            // When
            memberUpdater.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember).linkKakaoWithProfile(
                    any(SocialId.class),
                    any(Email.class),
                    any(MemberName.class),
                    eq(LocalDate.of(1990, 1, 1)),
                    eq(Gender.M),
                    eq(fixedClock));
        }
    }
}
