package com.ryuqq.setof.domain.member.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MemberErrorCode 테스트")
class MemberErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(MemberErrorCode.MEMBER_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("회원 관련 에러 코드 테스트")
    class MemberErrorCodesTest {

        @Test
        @DisplayName("MEMBER_NOT_FOUND 에러 코드를 검증한다")
        void memberNotFound() {
            assertThat(MemberErrorCode.MEMBER_NOT_FOUND.getCode()).isEqualTo("MBR-001");
            assertThat(MemberErrorCode.MEMBER_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()).isEqualTo("회원을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("MEMBER_NOT_ACTIVE 에러 코드를 검증한다")
        void memberNotActive() {
            assertThat(MemberErrorCode.MEMBER_NOT_ACTIVE.getCode()).isEqualTo("MBR-002");
            assertThat(MemberErrorCode.MEMBER_NOT_ACTIVE.getHttpStatus()).isEqualTo(403);
            assertThat(MemberErrorCode.MEMBER_NOT_ACTIVE.getMessage())
                    .isEqualTo("로그인할 수 없는 회원 상태입니다");
        }

        @Test
        @DisplayName("MEMBER_ALREADY_REGISTERED 에러 코드를 검증한다")
        void memberAlreadyRegistered() {
            assertThat(MemberErrorCode.MEMBER_ALREADY_REGISTERED.getCode()).isEqualTo("MBR-003");
            assertThat(MemberErrorCode.MEMBER_ALREADY_REGISTERED.getHttpStatus()).isEqualTo(409);
            assertThat(MemberErrorCode.MEMBER_ALREADY_REGISTERED.getMessage())
                    .isEqualTo("이미 가입된 회원입니다");
        }
    }

    @Nested
    @DisplayName("인증 관련 에러 코드 테스트")
    class AuthErrorCodesTest {

        @Test
        @DisplayName("MEMBER_AUTH_NOT_FOUND 에러 코드를 검증한다")
        void memberAuthNotFound() {
            assertThat(MemberErrorCode.MEMBER_AUTH_NOT_FOUND.getCode()).isEqualTo("MBR-050");
            assertThat(MemberErrorCode.MEMBER_AUTH_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(MemberErrorCode.MEMBER_AUTH_NOT_FOUND.getMessage())
                    .isEqualTo("인증 정보를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("INVALID_CREDENTIALS 에러 코드를 검증한다")
        void invalidCredentials() {
            assertThat(MemberErrorCode.INVALID_CREDENTIALS.getCode()).isEqualTo("MBR-051");
            assertThat(MemberErrorCode.INVALID_CREDENTIALS.getHttpStatus()).isEqualTo(401);
            assertThat(MemberErrorCode.INVALID_CREDENTIALS.getMessage())
                    .isEqualTo("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        @Test
        @DisplayName("UNSUPPORTED_AUTH_PROVIDER 에러 코드를 검증한다")
        void unsupportedAuthProvider() {
            assertThat(MemberErrorCode.UNSUPPORTED_AUTH_PROVIDER.getCode()).isEqualTo("MBR-052");
            assertThat(MemberErrorCode.UNSUPPORTED_AUTH_PROVIDER.getHttpStatus()).isEqualTo(400);
            assertThat(MemberErrorCode.UNSUPPORTED_AUTH_PROVIDER.getMessage())
                    .isEqualTo("지원하지 않는 인증 제공자입니다");
        }

        @Test
        @DisplayName("SOCIAL_LOGIN_ALREADY_EXISTS 에러 코드를 검증한다")
        void socialLoginAlreadyExists() {
            assertThat(MemberErrorCode.SOCIAL_LOGIN_ALREADY_EXISTS.getCode()).isEqualTo("MBR-053");
            assertThat(MemberErrorCode.SOCIAL_LOGIN_ALREADY_EXISTS.getHttpStatus()).isEqualTo(409);
            assertThat(MemberErrorCode.SOCIAL_LOGIN_ALREADY_EXISTS.getMessage())
                    .isEqualTo("소셜 로그인으로 이미 가입되어 있습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(MemberErrorCode.values())
                    .containsExactly(
                            MemberErrorCode.MEMBER_NOT_FOUND,
                            MemberErrorCode.MEMBER_NOT_ACTIVE,
                            MemberErrorCode.MEMBER_ALREADY_REGISTERED,
                            MemberErrorCode.MEMBER_AUTH_NOT_FOUND,
                            MemberErrorCode.INVALID_CREDENTIALS,
                            MemberErrorCode.UNSUPPORTED_AUTH_PROVIDER,
                            MemberErrorCode.SOCIAL_LOGIN_ALREADY_EXISTS);
        }
    }
}
