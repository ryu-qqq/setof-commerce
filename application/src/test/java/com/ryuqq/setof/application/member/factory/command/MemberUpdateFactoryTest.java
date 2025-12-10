package com.ryuqq.setof.application.member.factory.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderPort;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.Email;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.Password;
import com.ryuqq.setof.domain.member.vo.SocialId;
import com.ryuqq.setof.domain.member.vo.WithdrawalReason;
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

@DisplayName("MemberUpdateFactory")
@ExtendWith(MockitoExtension.class)
class MemberUpdateFactoryTest {

    @Mock private PasswordEncoderPort passwordEncoderPort;

    @Mock private ClockHolder clockHolder;

    @Mock private Member mockMember;

    private Clock fixedClock;
    private MemberUpdateFactory memberUpdateFactory;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));
        when(clockHolder.getClock()).thenReturn(fixedClock);
        memberUpdateFactory = new MemberUpdateFactory(passwordEncoderPort, clockHolder);
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
            memberUpdateFactory.changePassword(mockMember, newRawPassword);

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
            memberUpdateFactory.changePassword(mockMember, rawPassword);

            // Then
            verify(passwordEncoderPort).encode(rawPassword);
            verify(mockMember)
                    .changePassword(
                            argThat(password -> password.value().equals(hashedPassword)),
                            eq(fixedClock));
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
            memberUpdateFactory.withdraw(mockMember, reason);

            // Then
            verify(mockMember).withdraw(WithdrawalReason.RARELY_USED, fixedClock);
        }

        @Test
        @DisplayName("회원 탈퇴 처리 성공 - OTHER")
        void shouldWithdrawWithOtherReason() {
            // Given
            String reason = "OTHER";

            // When
            memberUpdateFactory.withdraw(mockMember, reason);

            // Then
            verify(mockMember).withdraw(WithdrawalReason.OTHER, fixedClock);
        }

        @Test
        @DisplayName("회원 탈퇴 처리 성공 - SERVICE_DISSATISFIED")
        void shouldWithdrawWithServiceDissatisfiedReason() {
            // Given
            String reason = "SERVICE_DISSATISFIED";

            // When
            memberUpdateFactory.withdraw(mockMember, reason);

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
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(
                            "member-id",
                            "kakao_12345",
                            "kakao@example.com",
                            "카카오이름",
                            LocalDate.of(1990, 1, 1),
                            "M");

            // When
            memberUpdateFactory.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember)
                    .linkKakaoWithProfile(
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
            IntegrateKakaoCommand command =
                    IntegrateKakaoCommand.withoutProfile("member-id", "kakao_12345");

            // When
            memberUpdateFactory.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember)
                    .linkKakaoWithProfile(
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
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(
                            "member-id",
                            "kakao_12345",
                            "kakao@example.com",
                            "카카오이름",
                            LocalDate.of(1995, 5, 15),
                            "W");

            // When
            memberUpdateFactory.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember)
                    .linkKakaoWithProfile(
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
            IntegrateKakaoCommand command =
                    new IntegrateKakaoCommand(
                            "member-id",
                            "kakao_12345",
                            "test@example.com",
                            "테스트",
                            LocalDate.of(1990, 1, 1),
                            "m");

            // When
            memberUpdateFactory.linkKakaoWithProfile(mockMember, command);

            // Then
            verify(mockMember)
                    .linkKakaoWithProfile(
                            any(SocialId.class),
                            any(Email.class),
                            any(MemberName.class),
                            eq(LocalDate.of(1990, 1, 1)),
                            eq(Gender.M),
                            eq(fixedClock));
        }
    }
}
