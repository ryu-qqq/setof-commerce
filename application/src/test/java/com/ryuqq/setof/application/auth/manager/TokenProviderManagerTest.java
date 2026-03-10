package com.ryuqq.setof.application.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.auth.AuthResponseFixtures;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
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
@DisplayName("TokenProviderManager 단위 테스트")
class TokenProviderManagerTest {

    @InjectMocks private TokenProviderManager sut;

    @Mock private TokenProviderPort tokenProviderPort;

    @Nested
    @DisplayName("generateTokenPair() - 토큰 쌍 생성")
    class GenerateTokenPairTest {

        @Test
        @DisplayName("회원 ID로 토큰 쌍을 생성하여 반환한다")
        void generateTokenPair_ValidMemberId_ReturnsTokenPair() {
            // given
            String memberId = "1";
            TokenPairResponse expected = AuthResponseFixtures.tokenPairResponse();

            given(tokenProviderPort.generateTokenPair(memberId)).willReturn(expected);

            // when
            TokenPairResponse result = sut.generateTokenPair(memberId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.accessToken()).isEqualTo(AuthResponseFixtures.DEFAULT_ACCESS_TOKEN);
            assertThat(result.refreshToken()).isEqualTo(AuthResponseFixtures.DEFAULT_REFRESH_TOKEN);
            then(tokenProviderPort).should().generateTokenPair(memberId);
        }
    }

    @Nested
    @DisplayName("extractMemberId() - Access Token에서 회원 ID 추출")
    class ExtractMemberIdTest {

        @Test
        @DisplayName("유효한 Access Token에서 회원 ID를 추출한다")
        void extractMemberId_ValidToken_ReturnsMemberId() {
            // given
            String accessToken = AuthResponseFixtures.DEFAULT_ACCESS_TOKEN;
            String expectedMemberId = "1";

            given(tokenProviderPort.extractMemberId(accessToken)).willReturn(expectedMemberId);

            // when
            String result = sut.extractMemberId(accessToken);

            // then
            assertThat(result).isEqualTo(expectedMemberId);
            then(tokenProviderPort).should().extractMemberId(accessToken);
        }
    }

    @Nested
    @DisplayName("validateAccessToken() - Access Token 유효성 검증")
    class ValidateAccessTokenTest {

        @Test
        @DisplayName("유효한 Access Token은 true를 반환한다")
        void validateAccessToken_ValidToken_ReturnsTrue() {
            // given
            String accessToken = AuthResponseFixtures.DEFAULT_ACCESS_TOKEN;

            given(tokenProviderPort.validateAccessToken(accessToken)).willReturn(true);

            // when
            boolean result = sut.validateAccessToken(accessToken);

            // then
            assertThat(result).isTrue();
            then(tokenProviderPort).should().validateAccessToken(accessToken);
        }

        @Test
        @DisplayName("유효하지 않은 Access Token은 false를 반환한다")
        void validateAccessToken_InvalidToken_ReturnsFalse() {
            // given
            String invalidToken = "invalid.token";

            given(tokenProviderPort.validateAccessToken(invalidToken)).willReturn(false);

            // when
            boolean result = sut.validateAccessToken(invalidToken);

            // then
            assertThat(result).isFalse();
            then(tokenProviderPort).should().validateAccessToken(invalidToken);
        }
    }

    @Nested
    @DisplayName("validateRefreshToken() - Refresh Token 유효성 검증")
    class ValidateRefreshTokenTest {

        @Test
        @DisplayName("유효한 Refresh Token은 true를 반환한다")
        void validateRefreshToken_ValidToken_ReturnsTrue() {
            // given
            String refreshToken = AuthResponseFixtures.DEFAULT_REFRESH_TOKEN;

            given(tokenProviderPort.validateRefreshToken(refreshToken)).willReturn(true);

            // when
            boolean result = sut.validateRefreshToken(refreshToken);

            // then
            assertThat(result).isTrue();
            then(tokenProviderPort).should().validateRefreshToken(refreshToken);
        }

        @Test
        @DisplayName("만료된 Refresh Token은 false를 반환한다")
        void validateRefreshToken_ExpiredToken_ReturnsFalse() {
            // given
            String expiredToken = "expired.refresh.token";

            given(tokenProviderPort.validateRefreshToken(expiredToken)).willReturn(false);

            // when
            boolean result = sut.validateRefreshToken(expiredToken);

            // then
            assertThat(result).isFalse();
            then(tokenProviderPort).should().validateRefreshToken(expiredToken);
        }
    }

    @Nested
    @DisplayName("extractMemberIdFromRefreshToken() - Refresh Token에서 회원 ID 추출")
    class ExtractMemberIdFromRefreshTokenTest {

        @Test
        @DisplayName("유효한 Refresh Token에서 회원 ID를 추출한다")
        void extractMemberIdFromRefreshToken_ValidToken_ReturnsMemberId() {
            // given
            String refreshToken = AuthResponseFixtures.DEFAULT_REFRESH_TOKEN;
            String expectedMemberId = "1";

            given(tokenProviderPort.extractMemberIdFromRefreshToken(refreshToken))
                    .willReturn(expectedMemberId);

            // when
            String result = sut.extractMemberIdFromRefreshToken(refreshToken);

            // then
            assertThat(result).isEqualTo(expectedMemberId);
            then(tokenProviderPort).should().extractMemberIdFromRefreshToken(refreshToken);
        }
    }

    @Nested
    @DisplayName("isAccessTokenExpired() - Access Token 만료 여부 확인")
    class IsAccessTokenExpiredTest {

        @Test
        @DisplayName("만료된 Access Token은 true를 반환한다")
        void isAccessTokenExpired_ExpiredToken_ReturnsTrue() {
            // given
            String expiredToken = "expired.access.token";

            given(tokenProviderPort.isAccessTokenExpired(expiredToken)).willReturn(true);

            // when
            boolean result = sut.isAccessTokenExpired(expiredToken);

            // then
            assertThat(result).isTrue();
            then(tokenProviderPort).should().isAccessTokenExpired(expiredToken);
        }

        @Test
        @DisplayName("유효한 Access Token은 false를 반환한다")
        void isAccessTokenExpired_ValidToken_ReturnsFalse() {
            // given
            String validToken = AuthResponseFixtures.DEFAULT_ACCESS_TOKEN;

            given(tokenProviderPort.isAccessTokenExpired(validToken)).willReturn(false);

            // when
            boolean result = sut.isAccessTokenExpired(validToken);

            // then
            assertThat(result).isFalse();
            then(tokenProviderPort).should().isAccessTokenExpired(validToken);
        }
    }
}
