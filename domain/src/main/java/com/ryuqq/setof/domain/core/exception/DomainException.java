package com.ryuqq.setof.domain.core.exception;

/**
 * Domain Layer 기본 예외
 *
 * <p>모든 도메인 예외는 이 클래스를 상속해야 합니다.
 *
 * <p>Domain Layer 예외 계층:
 *
 * <pre>
 * DomainException (Base)
 * ├─ InvalidMemberIdException
 * ├─ InvalidPhoneNumberException
 * ├─ InvalidEmailException
 * ├─ InvalidPasswordException
 * ├─ PasswordPolicyViolationException
 * ├─ InvalidMemberNameException
 * ├─ InvalidSocialIdException
 * ├─ RequiredConsentMissingException
 * ├─ InvalidWithdrawalInfoException
 * ├─ AlreadyWithdrawnMemberException
 * ├─ AlreadyKakaoMemberException
 * ├─ KakaoMemberCannotChangePasswordException
 * ├─ MemberNotFoundException
 * ├─ InactiveMemberException
 * └─ DuplicatePhoneNumberException
 * </pre>
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
