package com.ryuqq.setof.domain.member.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MemberException 및 구체적 예외 클래스 테스트")
class MemberExceptionTest {

    @Nested
    @DisplayName("MemberException 기본 생성 테스트")
    class MemberExceptionCreationTest {

        @Test
        @DisplayName("ErrorCode로 MemberException을 생성한다")
        void createWithErrorCode() {
            // when
            MemberException exception = new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("회원을 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("MBR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 MemberException을 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            MemberException exception =
                    new MemberException(MemberErrorCode.MEMBER_NOT_FOUND, "ID abc인 회원 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID abc인 회원 없음");
            assertThat(exception.code()).isEqualTo("MBR-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 MemberException을 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            MemberException exception =
                    new MemberException(MemberErrorCode.MEMBER_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("MBR-001");
        }
    }

    @Nested
    @DisplayName("MemberNotFoundException 테스트")
    class MemberNotFoundExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            MemberNotFoundException exception = new MemberNotFoundException();

            assertThat(exception.code()).isEqualTo("MBR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("회원을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("memberId를 포함하여 생성한다")
        void createWithMemberId() {
            MemberNotFoundException exception = new MemberNotFoundException("abc-123");

            assertThat(exception.code()).isEqualTo("MBR-001");
            assertThat(exception.getMessage()).contains("abc-123");
        }

        @Test
        @DisplayName("withMessage()로 커스텀 메시지로 생성한다")
        void createWithMessage() {
            MemberNotFoundException exception = MemberNotFoundException.withMessage("커스텀 메시지");

            assertThat(exception.code()).isEqualTo("MBR-001");
            assertThat(exception.getMessage()).isEqualTo("커스텀 메시지");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            MemberNotFoundException exception = new MemberNotFoundException();

            assertThat(exception).isInstanceOf(MemberException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("MemberNotActiveException 테스트")
    class MemberNotActiveExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            MemberNotActiveException exception = new MemberNotActiveException();

            assertThat(exception.code()).isEqualTo("MBR-002");
            assertThat(exception.httpStatus()).isEqualTo(403);
            assertThat(exception.getMessage()).isEqualTo("로그인할 수 없는 회원 상태입니다");
        }

        @Test
        @DisplayName("memberId를 포함하여 생성한다")
        void createWithMemberId() {
            MemberNotActiveException exception = new MemberNotActiveException("abc-123");

            assertThat(exception.code()).isEqualTo("MBR-002");
            assertThat(exception.getMessage()).contains("abc-123");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            MemberNotActiveException exception = new MemberNotActiveException();

            assertThat(exception).isInstanceOf(MemberException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("MemberAlreadyRegisteredException 테스트")
    class MemberAlreadyRegisteredExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            MemberAlreadyRegisteredException exception = new MemberAlreadyRegisteredException();

            assertThat(exception.code()).isEqualTo("MBR-003");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).isEqualTo("이미 가입된 회원입니다");
        }

        @Test
        @DisplayName("인증 제공자를 포함하여 생성한다")
        void createWithAuthProvider() {
            MemberAlreadyRegisteredException exception =
                    new MemberAlreadyRegisteredException("전화번호");

            assertThat(exception.code()).isEqualTo("MBR-003");
            assertThat(exception.getMessage()).contains("전화번호");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            assertThat(new MemberAlreadyRegisteredException())
                    .isInstanceOf(MemberException.class)
                    .isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("MemberAuthNotFoundException 테스트")
    class MemberAuthNotFoundExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            MemberAuthNotFoundException exception = new MemberAuthNotFoundException();

            assertThat(exception.code()).isEqualTo("MBR-050");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("인증 정보를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("상세 정보를 포함하여 생성한다")
        void createWithDetail() {
            MemberAuthNotFoundException exception = new MemberAuthNotFoundException("PHONE 인증");

            assertThat(exception.code()).isEqualTo("MBR-050");
            assertThat(exception.getMessage()).contains("PHONE 인증");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            assertThat(new MemberAuthNotFoundException())
                    .isInstanceOf(MemberException.class)
                    .isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("InvalidCredentialsException 테스트")
    class InvalidCredentialsExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            InvalidCredentialsException exception = new InvalidCredentialsException();

            assertThat(exception.code()).isEqualTo("MBR-051");
            assertThat(exception.httpStatus()).isEqualTo(401);
            assertThat(exception.getMessage()).isEqualTo("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        @Test
        @DisplayName("providerUserId를 포함하여 생성한다")
        void createWithProviderUserId() {
            InvalidCredentialsException exception =
                    new InvalidCredentialsException("010-1234-5678");

            assertThat(exception.code()).isEqualTo("MBR-051");
            assertThat(exception.getMessage()).contains("010-1234-5678");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            assertThat(new InvalidCredentialsException())
                    .isInstanceOf(MemberException.class)
                    .isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("UnsupportedAuthProviderException 테스트")
    class UnsupportedAuthProviderExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            UnsupportedAuthProviderException exception = new UnsupportedAuthProviderException();

            assertThat(exception.code()).isEqualTo("MBR-052");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("지원하지 않는 인증 제공자입니다");
        }

        @Test
        @DisplayName("인증 제공자명을 포함하여 생성한다")
        void createWithAuthProvider() {
            UnsupportedAuthProviderException exception =
                    new UnsupportedAuthProviderException("NAVER");

            assertThat(exception.code()).isEqualTo("MBR-052");
            assertThat(exception.getMessage()).contains("NAVER");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            assertThat(new UnsupportedAuthProviderException())
                    .isInstanceOf(MemberException.class)
                    .isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("SocialLoginAlreadyExistsException 테스트")
    class SocialLoginAlreadyExistsExceptionTest {

        @Test
        @DisplayName("기본 생성자로 생성한다")
        void createDefault() {
            SocialLoginAlreadyExistsException exception = new SocialLoginAlreadyExistsException();

            assertThat(exception.code()).isEqualTo("MBR-053");
            assertThat(exception.httpStatus()).isEqualTo(409);
            assertThat(exception.getMessage()).isEqualTo("소셜 로그인으로 이미 가입되어 있습니다");
        }

        @Test
        @DisplayName("인증 제공자를 포함하여 생성한다")
        void createWithAuthProvider() {
            SocialLoginAlreadyExistsException exception =
                    new SocialLoginAlreadyExistsException("카카오");

            assertThat(exception.code()).isEqualTo("MBR-053");
            assertThat(exception.getMessage()).contains("카카오");
        }

        @Test
        @DisplayName("MemberException을 상속한다")
        void extendsMemberException() {
            assertThat(new SocialLoginAlreadyExistsException())
                    .isInstanceOf(MemberException.class)
                    .isInstanceOf(DomainException.class);
        }
    }
}
