package com.ryuqq.setof.domain.exception;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.member.exception.InvalidEmailException;
import com.ryuqq.setof.domain.member.exception.InvalidMemberIdException;
import com.ryuqq.setof.domain.member.exception.InvalidMemberNameException;
import com.ryuqq.setof.domain.member.exception.InvalidPasswordException;
import com.ryuqq.setof.domain.member.exception.InvalidPhoneNumberException;
import com.ryuqq.setof.domain.member.exception.InvalidSocialIdException;
import com.ryuqq.setof.domain.member.exception.InvalidWithdrawalInfoException;
import com.ryuqq.setof.domain.member.exception.KakaoMemberCannotChangePasswordException;
import com.ryuqq.setof.domain.member.exception.PasswordPolicyViolationException;
import com.ryuqq.setof.domain.member.exception.RequiredConsentMissingException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DomainException 계층 구조 테스트")
class DomainExceptionTest {

    private static final String TEST_VALUE = "test-value";
    private static final UUID TEST_MEMBER_ID = UUID.randomUUID();

    @Nested
    @DisplayName("모든 도메인 예외는 DomainException을 상속해야 함")
    class InheritanceTest {

        @Test
        @DisplayName("InvalidMemberIdException은 DomainException의 하위 클래스")
        void invalidMemberIdExceptionExtendsDomainException() {
            InvalidMemberIdException exception = new InvalidMemberIdException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidPhoneNumberException은 DomainException의 하위 클래스")
        void invalidPhoneNumberExceptionExtendsDomainException() {
            InvalidPhoneNumberException exception = new InvalidPhoneNumberException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidEmailException은 DomainException의 하위 클래스")
        void invalidEmailExceptionExtendsDomainException() {
            InvalidEmailException exception = new InvalidEmailException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidPasswordException은 DomainException의 하위 클래스")
        void invalidPasswordExceptionExtendsDomainException() {
            InvalidPasswordException exception = new InvalidPasswordException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("PasswordPolicyViolationException은 DomainException의 하위 클래스")
        void passwordPolicyViolationExceptionExtendsDomainException() {
            PasswordPolicyViolationException exception =
                    new PasswordPolicyViolationException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidMemberNameException은 DomainException의 하위 클래스")
        void invalidMemberNameExceptionExtendsDomainException() {
            InvalidMemberNameException exception = new InvalidMemberNameException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidSocialIdException은 DomainException의 하위 클래스")
        void invalidSocialIdExceptionExtendsDomainException() {
            InvalidSocialIdException exception = new InvalidSocialIdException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("RequiredConsentMissingException은 DomainException의 하위 클래스")
        void requiredConsentMissingExceptionExtendsDomainException() {
            RequiredConsentMissingException exception =
                    new RequiredConsentMissingException(TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidWithdrawalInfoException은 DomainException의 하위 클래스")
        void invalidWithdrawalInfoExceptionExtendsDomainException() {
            InvalidWithdrawalInfoException exception =
                    new InvalidWithdrawalInfoException(TEST_VALUE, TEST_VALUE);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("AlreadyWithdrawnMemberException은 DomainException의 하위 클래스")
        void alreadyWithdrawnMemberExceptionExtendsDomainException() {
            AlreadyWithdrawnMemberException exception =
                    new AlreadyWithdrawnMemberException(TEST_MEMBER_ID);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("AlreadyKakaoMemberException은 DomainException의 하위 클래스")
        void alreadyKakaoMemberExceptionExtendsDomainException() {
            AlreadyKakaoMemberException exception = new AlreadyKakaoMemberException(TEST_MEMBER_ID);
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("KakaoMemberCannotChangePasswordException은 DomainException의 하위 클래스")
        void kakaoMemberCannotChangePasswordExceptionExtendsDomainException() {
            KakaoMemberCannotChangePasswordException exception =
                    new KakaoMemberCannotChangePasswordException(TEST_MEMBER_ID);
            assertInstanceOf(DomainException.class, exception);
        }
    }

    @Nested
    @DisplayName("모든 도메인 예외는 RuntimeException의 하위 클래스")
    class RuntimeExceptionTest {

        @Test
        @DisplayName("DomainException은 RuntimeException의 하위 클래스")
        void domainExceptionIsRuntimeException() {
            DomainException exception = new InvalidMemberIdException(TEST_VALUE);
            assertInstanceOf(RuntimeException.class, exception);
        }
    }
}
