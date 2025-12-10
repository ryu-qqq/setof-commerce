package com.ryuqq.setof.application.member.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.port.out.client.PasswordEncoderPort;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.member.exception.DuplicatePhoneNumberException;
import com.ryuqq.setof.domain.member.exception.InactiveMemberException;
import com.ryuqq.setof.domain.member.exception.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberPolicyValidator")
@ExtendWith(MockitoExtension.class)
class MemberPolicyValidatorTest {

    @Mock private PasswordEncoderPort passwordEncoderPort;

    private MemberPolicyValidator memberPolicyValidator;

    @BeforeEach
    void setUp() {
        memberPolicyValidator = new MemberPolicyValidator(passwordEncoderPort);
    }

    @Nested
    @DisplayName("validateCanLogin")
    class ValidateCanLoginTest {

        @Test
        @DisplayName("로컬 회원 로그인 검증 성공")
        void shouldPassForValidLocalMember() {
            // Given
            Member member = MemberFixture.createLocalMember();
            String rawPassword = "password123";
            when(passwordEncoderPort.matches(rawPassword, member.getPasswordValue()))
                    .thenReturn(true);

            // When & Then
            assertDoesNotThrow(() -> memberPolicyValidator.validateCanLogin(member, rawPassword));
            verify(passwordEncoderPort).matches(rawPassword, member.getPasswordValue());
        }

        @Test
        @DisplayName("카카오 회원 로그인 시도 시 예외 발생")
        void shouldThrowExceptionForKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();
            String rawPassword = "password123";

            // When & Then
            assertThrows(
                    AlreadyKakaoMemberException.class,
                    () -> memberPolicyValidator.validateCanLogin(kakaoMember, rawPassword));
            verify(passwordEncoderPort, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("비밀번호 불일치 시 예외 발생")
        void shouldThrowExceptionForInvalidPassword() {
            // Given
            Member member = MemberFixture.createLocalMember();
            String wrongPassword = "wrongPassword";
            when(passwordEncoderPort.matches(wrongPassword, member.getPasswordValue()))
                    .thenReturn(false);

            // When & Then
            assertThrows(
                    InvalidPasswordException.class,
                    () -> memberPolicyValidator.validateCanLogin(member, wrongPassword));
        }

        @Test
        @DisplayName("탈퇴한 회원 로그인 시도 시 예외 발생")
        void shouldThrowExceptionForWithdrawnMember() {
            // Given
            Member member = MemberFixture.createWithdrawnMember();
            String rawPassword = "password123";
            when(passwordEncoderPort.matches(rawPassword, member.getPasswordValue()))
                    .thenReturn(true);

            // When & Then
            assertThrows(
                    AlreadyWithdrawnMemberException.class,
                    () -> memberPolicyValidator.validateCanLogin(member, rawPassword));
        }

        @Test
        @DisplayName("휴면 회원 로그인 시도 시 예외 발생")
        void shouldThrowExceptionForInactiveMember() {
            // Given
            Member member = MemberFixture.createInactiveMember();
            String rawPassword = "password123";
            when(passwordEncoderPort.matches(rawPassword, member.getPasswordValue()))
                    .thenReturn(true);

            // When & Then
            assertThrows(
                    InactiveMemberException.class,
                    () -> memberPolicyValidator.validateCanLogin(member, rawPassword));
        }
    }

    @Nested
    @DisplayName("validateCanResetPassword")
    class ValidateCanResetPasswordTest {

        @Test
        @DisplayName("로컬 회원 비밀번호 재설정 검증 성공")
        void shouldPassForLocalMember() {
            // Given
            Member member = MemberFixture.createLocalMember();

            // When & Then
            assertDoesNotThrow(() -> memberPolicyValidator.validateCanResetPassword(member));
        }

        @Test
        @DisplayName("카카오 회원 비밀번호 재설정 시도 시 예외 발생")
        void shouldThrowExceptionForKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When & Then
            assertThrows(
                    AlreadyKakaoMemberException.class,
                    () -> memberPolicyValidator.validateCanResetPassword(kakaoMember));
        }
    }

    @Nested
    @DisplayName("validateCanWithdraw")
    class ValidateCanWithdrawTest {

        @Test
        @DisplayName("활성 회원 탈퇴 검증 성공")
        void shouldPassForActiveMember() {
            // Given
            Member member = MemberFixture.createLocalMember();

            // When & Then
            assertDoesNotThrow(() -> memberPolicyValidator.validateCanWithdraw(member));
        }

        @Test
        @DisplayName("카카오 회원 탈퇴 검증 성공")
        void shouldPassForKakaoMember() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When & Then
            assertDoesNotThrow(() -> memberPolicyValidator.validateCanWithdraw(kakaoMember));
        }

        @Test
        @DisplayName("이미 탈퇴한 회원 탈퇴 시도 시 예외 발생")
        void shouldThrowExceptionForWithdrawnMember() {
            // Given
            Member member = MemberFixture.createWithdrawnMember();

            // When & Then
            assertThrows(
                    AlreadyWithdrawnMemberException.class,
                    () -> memberPolicyValidator.validateCanWithdraw(member));
        }
    }

    @Nested
    @DisplayName("validatePhoneNumberNotDuplicate")
    class ValidatePhoneNumberNotDuplicateTest {

        private static final String TEST_PHONE_NUMBER = "01012345678";

        @Test
        @DisplayName("핸드폰 번호 미존재 시 검증 성공")
        void shouldPassWhenNotExists() {
            // When & Then
            assertDoesNotThrow(
                    () ->
                            memberPolicyValidator.validatePhoneNumberNotDuplicate(
                                    TEST_PHONE_NUMBER, false));
        }

        @Test
        @DisplayName("핸드폰 번호 존재 시 예외 발생")
        void shouldThrowExceptionWhenExists() {
            // When & Then
            assertThrows(
                    DuplicatePhoneNumberException.class,
                    () ->
                            memberPolicyValidator.validatePhoneNumberNotDuplicate(
                                    TEST_PHONE_NUMBER, true));
        }
    }
}
