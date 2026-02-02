package com.ryuqq.setof.adapter.in.rest.admin.v2.auth.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.adapter.in.rest.admin.auth.AuthApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.MyInfoApiResponse;
import com.ryuqq.setof.application.auth.dto.response.MyInfoResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * AuthQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("AuthQueryApiMapper 단위 테스트")
class AuthQueryApiMapperTest {

    private AuthQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthQueryApiMapper();
    }

    @Nested
    @DisplayName("extractToken(String)")
    class ExtractTokenTest {

        @Test
        @DisplayName("Bearer 토큰에서 액세스 토큰을 추출한다")
        void extractToken_ValidBearer_Success() {
            // given
            String authorization = AuthApiFixtures.bearerToken();

            // when
            String token = mapper.extractToken(authorization);

            // then
            assertThat(token).isEqualTo(AuthApiFixtures.DEFAULT_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("잘못된 형식의 헤더는 예외를 발생시킨다")
        void extractToken_InvalidHeader_ThrowsException() {
            // given
            String authorization = AuthApiFixtures.invalidAuthorizationHeader();

            // when & then
            assertThatThrownBy(() -> mapper.extractToken(authorization))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid Authorization header");
        }

        @Test
        @DisplayName("null 헤더는 예외를 발생시킨다")
        void extractToken_NullHeader_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> mapper.extractToken(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid Authorization header");
        }
    }

    @Nested
    @DisplayName("toResponse(MyInfoResult)")
    class ToResponseTest {

        @Test
        @DisplayName("MyInfoResult를 응답으로 변환한다")
        void toResponse_Success() {
            // given
            MyInfoResult result = AuthApiFixtures.myInfoResult();

            // when
            MyInfoApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.userId()).isEqualTo(result.userId());
            assertThat(response.email()).isEqualTo(result.email());
            assertThat(response.name()).isEqualTo(result.name());
            assertThat(response.tenantId()).isEqualTo(result.tenantId());
            assertThat(response.tenantName()).isEqualTo(result.tenantName());
            assertThat(response.organizationId()).isEqualTo(result.organizationId());
            assertThat(response.organizationName()).isEqualTo(result.organizationName());
            assertThat(response.roles()).hasSize(result.roles().size());
            assertThat(response.permissions()).isEqualTo(result.permissions());
        }

        @Test
        @DisplayName("역할이 null인 경우 빈 목록으로 변환한다")
        void toResponse_NullRoles_ReturnsEmptyList() {
            // given
            MyInfoResult result = AuthApiFixtures.myInfoResultWithoutRoles();

            // when
            MyInfoApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.roles()).isEmpty();
        }
    }
}
