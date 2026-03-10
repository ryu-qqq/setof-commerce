package com.ryuqq.setof.application.member.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.InvalidCredentialsException;
import com.ryuqq.setof.domain.member.exception.MemberNotActiveException;
import com.ryuqq.setof.domain.member.exception.SocialLoginAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginValidator 단위 테스트")
class LoginValidatorTest {

    @InjectMocks private LoginValidator sut;

    @Mock private MemberReadManager memberReadManager;
    @Mock private PasswordManager passwordManager;

    @Nested
    @DisplayName("validate() - 로그인 검증")
    class ValidateTest {

        @Test
        @DisplayName("유효한 인증 정보면 회원 도메인 객체를 반환한다")
        void validate_ValidCredentials_ReturnsMember() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            MemberWithCredentials credentials = MemberQueryFixtures.memberWithCredentials();

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentials);
            given(passwordManager.matches(rawPassword, credentials.passwordHash()))
                    .willReturn(true);

            // when
            Member result = sut.validate(phoneNumber, rawPassword);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(credentials.member());
            then(memberReadManager).should().getWithCredentialsByPhoneNumber(phoneNumber);
            then(passwordManager).should().matches(rawPassword, credentials.passwordHash());
        }

        @Test
        @DisplayName("탈퇴 회원이면 MemberNotActiveException이 발생한다")
        void validate_WithdrawnMember_ThrowsMemberNotActiveException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            Member withdrawnMember = MemberFixtures.withdrawnMember();
            MemberWithCredentials credentials =
                    MemberQueryFixtures.memberWithCredentials(withdrawnMember, "EMAIL");

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentials);

            // when & then
            assertThatThrownBy(() -> sut.validate(phoneNumber, rawPassword))
                    .isInstanceOf(MemberNotActiveException.class);

            then(passwordManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("정지 회원이면 MemberNotActiveException이 발생한다")
        void validate_SuspendedMember_ThrowsMemberNotActiveException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            Member suspendedMember = MemberFixtures.suspendedMember();
            MemberWithCredentials credentials =
                    MemberQueryFixtures.memberWithCredentials(suspendedMember, "EMAIL");

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentials);

            // when & then
            assertThatThrownBy(() -> sut.validate(phoneNumber, rawPassword))
                    .isInstanceOf(MemberNotActiveException.class);
        }

        @Test
        @DisplayName("소셜 로그인으로 가입한 회원이면 SocialLoginAlreadyExistsException이 발생한다")
        void validate_SocialLoginMember_ThrowsSocialLoginAlreadyExistsException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            MemberWithCredentials kakaoCredentials =
                    MemberQueryFixtures.memberWithCredentials(
                            MemberFixtures.activeMigratedMember(), "KAKAO");

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(kakaoCredentials);

            // when & then
            assertThatThrownBy(() -> sut.validate(phoneNumber, rawPassword))
                    .isInstanceOf(SocialLoginAlreadyExistsException.class);

            then(passwordManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 InvalidCredentialsException이 발생한다")
        void validate_WrongPassword_ThrowsInvalidCredentialsException() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String wrongPassword = "wrongPassword!";
            MemberWithCredentials credentials = MemberQueryFixtures.memberWithCredentials();

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentials);
            given(passwordManager.matches(wrongPassword, credentials.passwordHash()))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> sut.validate(phoneNumber, wrongPassword))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("socialLoginType이 null이면 이메일 회원으로 비밀번호 검증을 진행한다")
        void validate_NullSocialLoginType_ProceedsWithPasswordValidation() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            MemberWithCredentials credentialsWithNullSocial =
                    MemberQueryFixtures.memberWithCredentials(
                            MemberFixtures.activeMigratedMember(), null);

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentialsWithNullSocial);
            given(passwordManager.matches(rawPassword, credentialsWithNullSocial.passwordHash()))
                    .willReturn(true);

            // when
            Member result = sut.validate(phoneNumber, rawPassword);

            // then
            assertThat(result).isEqualTo(credentialsWithNullSocial.member());
            then(passwordManager)
                    .should()
                    .matches(rawPassword, credentialsWithNullSocial.passwordHash());
        }

        @Test
        @DisplayName("socialLoginType이 EMAIL이면 이메일 회원으로 비밀번호 검증을 진행한다")
        void validate_EmailSocialLoginType_ProceedsWithPasswordValidation() {
            // given
            String phoneNumber = MemberCommandFixtures.DEFAULT_PHONE_NUMBER;
            String rawPassword = MemberCommandFixtures.DEFAULT_PASSWORD;
            MemberWithCredentials credentials = MemberQueryFixtures.memberWithCredentials();

            given(memberReadManager.getWithCredentialsByPhoneNumber(phoneNumber))
                    .willReturn(credentials);
            given(passwordManager.matches(rawPassword, credentials.passwordHash()))
                    .willReturn(true);

            // when
            Member result = sut.validate(phoneNumber, rawPassword);

            // then
            assertThat(result).isNotNull();
        }
    }
}
