package com.ryuqq.setof.domain.core.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.AlreadyWithdrawnMemberException;
import com.ryuqq.setof.domain.core.member.exception.InvalidEmailException;
import com.ryuqq.setof.domain.core.member.exception.InvalidMemberIdException;
import com.ryuqq.setof.domain.core.member.exception.InvalidMemberNameException;
import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;
import com.ryuqq.setof.domain.core.member.exception.InvalidPhoneNumberException;
import com.ryuqq.setof.domain.core.member.exception.InvalidSocialIdException;
import com.ryuqq.setof.domain.core.member.exception.InvalidWithdrawalInfoException;
import com.ryuqq.setof.domain.core.member.exception.KakaoMemberCannotChangePasswordException;
import com.ryuqq.setof.domain.core.member.exception.PasswordPolicyViolationException;
import com.ryuqq.setof.domain.core.member.exception.RequiredConsentMissingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DomainException 계층 구조 테스트")
class DomainExceptionTest {

    @Nested
    @DisplayName("모든 도메인 예외는 DomainException을 상속해야 함")
    class InheritanceTest {

        @Test
        @DisplayName("InvalidMemberIdException은 DomainException의 하위 클래스")
        void invalidMemberIdExceptionExtendsDomainException() {
            InvalidMemberIdException exception = new InvalidMemberIdException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidPhoneNumberException은 DomainException의 하위 클래스")
        void invalidPhoneNumberExceptionExtendsDomainException() {
            InvalidPhoneNumberException exception = new InvalidPhoneNumberException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidEmailException은 DomainException의 하위 클래스")
        void invalidEmailExceptionExtendsDomainException() {
            InvalidEmailException exception = new InvalidEmailException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidPasswordException은 DomainException의 하위 클래스")
        void invalidPasswordExceptionExtendsDomainException() {
            InvalidPasswordException exception = new InvalidPasswordException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("PasswordPolicyViolationException은 DomainException의 하위 클래스")
        void passwordPolicyViolationExceptionExtendsDomainException() {
            PasswordPolicyViolationException exception = new PasswordPolicyViolationException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidMemberNameException은 DomainException의 하위 클래스")
        void invalidMemberNameExceptionExtendsDomainException() {
            InvalidMemberNameException exception = new InvalidMemberNameException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidSocialIdException은 DomainException의 하위 클래스")
        void invalidSocialIdExceptionExtendsDomainException() {
            InvalidSocialIdException exception = new InvalidSocialIdException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("RequiredConsentMissingException은 DomainException의 하위 클래스")
        void requiredConsentMissingExceptionExtendsDomainException() {
            RequiredConsentMissingException exception = new RequiredConsentMissingException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("InvalidWithdrawalInfoException은 DomainException의 하위 클래스")
        void invalidWithdrawalInfoExceptionExtendsDomainException() {
            InvalidWithdrawalInfoException exception = new InvalidWithdrawalInfoException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("AlreadyWithdrawnMemberException은 DomainException의 하위 클래스")
        void alreadyWithdrawnMemberExceptionExtendsDomainException() {
            AlreadyWithdrawnMemberException exception = new AlreadyWithdrawnMemberException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("AlreadyKakaoMemberException은 DomainException의 하위 클래스")
        void alreadyKakaoMemberExceptionExtendsDomainException() {
            AlreadyKakaoMemberException exception = new AlreadyKakaoMemberException();
            assertInstanceOf(DomainException.class, exception);
        }

        @Test
        @DisplayName("KakaoMemberCannotChangePasswordException은 DomainException의 하위 클래스")
        void kakaoMemberCannotChangePasswordExceptionExtendsDomainException() {
            KakaoMemberCannotChangePasswordException exception =
                    new KakaoMemberCannotChangePasswordException();
            assertInstanceOf(DomainException.class, exception);
        }
    }

    @Nested
    @DisplayName("모든 도메인 예외는 RuntimeException의 하위 클래스")
    class RuntimeExceptionTest {

        @Test
        @DisplayName("DomainException은 RuntimeException의 하위 클래스")
        void domainExceptionIsRuntimeException() {
            DomainException exception = new InvalidMemberIdException();
            assertInstanceOf(RuntimeException.class, exception);
        }
    }
}
