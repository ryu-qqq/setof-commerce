package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.adapter.in.rest.admin.auth.AuthApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.LoginApiResponse;
import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * AuthCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("AuthCommandApiMapper 단위 테스트")
class AuthCommandApiMapperTest {

    private AuthCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(LoginApiRequest)")
    class ToLoginCommandTest {

        @Test
        @DisplayName("로그인 요청을 Command로 변환한다")
        void toCommand_Login_Success() {
            // given
            LoginApiRequest request = AuthApiFixtures.loginRequest();

            // when
            LoginCommand command = mapper.toCommand(request);

            // then
            assertThat(command.identifier()).isEqualTo(request.identifier());
            assertThat(command.password()).isEqualTo(request.password());
        }
    }

    @Nested
    @DisplayName("toCommand(String userId)")
    class ToLogoutCommandTest {

        @Test
        @DisplayName("userId를 LogoutCommand로 변환한다")
        void toCommand_Logout_Success() {
            // given
            String userId = "user-123";

            // when
            LogoutCommand command = mapper.toCommand(userId);

            // then
            assertThat(command.userId()).isEqualTo(userId);
        }
    }

    @Nested
    @DisplayName("toResponse(LoginResult)")
    class ToResponseTest {

        @Test
        @DisplayName("성공 결과를 응답으로 변환한다")
        void toResponse_Success() {
            // given
            LoginResult result = AuthApiFixtures.successLoginResult();

            // when
            LoginApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.accessToken()).isEqualTo(result.accessToken());
            assertThat(response.refreshToken()).isEqualTo(result.refreshToken());
            assertThat(response.tokenType()).isEqualTo(result.tokenType());
            assertThat(response.expiresIn()).isEqualTo(result.expiresIn());
        }

        @Test
        @DisplayName("실패 결과는 예외를 발생시킨다")
        void toResponse_Failure_ThrowsException() {
            // given
            LoginResult result = AuthApiFixtures.failureLoginResult();

            // when & then
            assertThatThrownBy(() -> mapper.toResponse(result))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(result.errorMessage());
        }
    }
}
