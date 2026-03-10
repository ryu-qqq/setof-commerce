package com.ryuqq.setof.application.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.auth.AuthResponseFixtures;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import java.util.Optional;
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
@DisplayName("TokenCommandFacade 단위 테스트")
class TokenCommandFacadeTest {

    @InjectMocks private TokenCommandFacade sut;

    @Mock private TokenProviderManager tokenProviderManager;
    @Mock private RefreshTokenCacheCommandManager refreshTokenCacheCommandManager;
    @Mock private RefreshTokenCacheReadManager refreshTokenCacheReadManager;

    @Nested
    @DisplayName("issueTokenPair() - 토큰 쌍 발급 및 캐시 저장")
    class IssueTokenPairTest {

        @Test
        @DisplayName("회원 ID로 토큰 쌍을 생성하고 Refresh Token을 캐시에 저장한다")
        void issueTokenPair_ValidMemberId_ReturnsTokenPairAndPersistsCache() {
            // given
            String memberId = "1";
            TokenPairResponse expectedTokenPair = AuthResponseFixtures.tokenPairResponse();

            given(tokenProviderManager.generateTokenPair(memberId)).willReturn(expectedTokenPair);
            willDoNothing()
                    .given(refreshTokenCacheCommandManager)
                    .persist(
                            expectedTokenPair.refreshToken(),
                            memberId,
                            expectedTokenPair.refreshTokenExpiresIn());

            // when
            TokenPairResponse result = sut.issueTokenPair(memberId);

            // then
            assertThat(result).isEqualTo(expectedTokenPair);
            then(tokenProviderManager).should().generateTokenPair(memberId);
            then(refreshTokenCacheCommandManager)
                    .should()
                    .persist(
                            expectedTokenPair.refreshToken(),
                            memberId,
                            expectedTokenPair.refreshTokenExpiresIn());
        }
    }

    @Nested
    @DisplayName("issueLoginResult() - 로그인 결과 발급")
    class IssueLoginResultTest {

        @Test
        @DisplayName("레거시 userId로 LoginResult를 생성하여 반환한다")
        void issueLoginResult_ValidUserId_ReturnsLoginResult() {
            // given
            long userId = 1L;
            String memberId = "1";
            TokenPairResponse tokenPair = AuthResponseFixtures.tokenPairResponse();

            given(tokenProviderManager.generateTokenPair(memberId)).willReturn(tokenPair);
            willDoNothing()
                    .given(refreshTokenCacheCommandManager)
                    .persist(tokenPair.refreshToken(), memberId, tokenPair.refreshTokenExpiresIn());

            // when
            LoginResult result = sut.issueLoginResult(userId);

            // then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.userId()).isEqualTo(memberId);
            assertThat(result.accessToken()).isEqualTo(tokenPair.accessToken());
            assertThat(result.refreshToken()).isEqualTo(tokenPair.refreshToken());
            assertThat(result.expiresIn()).isEqualTo(tokenPair.accessTokenExpiresIn());
            assertThat(result.tokenType()).isEqualTo("Bearer");
            then(tokenProviderManager).should().generateTokenPair(memberId);
        }

        @Test
        @DisplayName("다른 userId로 LoginResult를 생성할 때 memberId 변환이 올바르게 된다")
        void issueLoginResult_DifferentUserId_ConvertsToStringMemberId() {
            // given
            long userId = 42L;
            String memberId = "42";
            TokenPairResponse tokenPair =
                    AuthResponseFixtures.tokenPairResponse("access.42", "refresh.42");

            given(tokenProviderManager.generateTokenPair(memberId)).willReturn(tokenPair);
            willDoNothing()
                    .given(refreshTokenCacheCommandManager)
                    .persist(tokenPair.refreshToken(), memberId, tokenPair.refreshTokenExpiresIn());

            // when
            LoginResult result = sut.issueLoginResult(userId);

            // then
            assertThat(result.userId()).isEqualTo("42");
            assertThat(result.isSuccess()).isTrue();
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

            given(tokenProviderManager.extractMemberId(accessToken)).willReturn(expectedMemberId);

            // when
            String result = sut.extractMemberId(accessToken);

            // then
            assertThat(result).isEqualTo(expectedMemberId);
            then(tokenProviderManager).should().extractMemberId(accessToken);
        }
    }

    @Nested
    @DisplayName("findMemberIdByRefreshToken() - Refresh Token으로 회원 ID 조회")
    class FindMemberIdByRefreshTokenTest {

        @Test
        @DisplayName("캐시에 존재하는 Refresh Token으로 회원 ID를 조회한다")
        void findMemberIdByRefreshToken_ExistingToken_ReturnsMemberId() {
            // given
            String refreshToken = AuthResponseFixtures.DEFAULT_REFRESH_TOKEN;
            String expectedMemberId = "1";

            given(refreshTokenCacheReadManager.findMemberIdByRefreshToken(refreshToken))
                    .willReturn(Optional.of(expectedMemberId));

            // when
            Optional<String> result = sut.findMemberIdByRefreshToken(refreshToken);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedMemberId);
            then(refreshTokenCacheReadManager).should().findMemberIdByRefreshToken(refreshToken);
        }

        @Test
        @DisplayName("캐시에 존재하지 않는 Refresh Token은 Optional.empty()를 반환한다")
        void findMemberIdByRefreshToken_NonExistingToken_ReturnsEmpty() {
            // given
            String refreshToken = "expired.refresh.token";

            given(refreshTokenCacheReadManager.findMemberIdByRefreshToken(refreshToken))
                    .willReturn(Optional.empty());

            // when
            Optional<String> result = sut.findMemberIdByRefreshToken(refreshToken);

            // then
            assertThat(result).isEmpty();
            then(refreshTokenCacheReadManager).should().findMemberIdByRefreshToken(refreshToken);
        }
    }

    @Nested
    @DisplayName("revokeRefreshToken() - Refresh Token 캐시 삭제")
    class RevokeRefreshTokenTest {

        @Test
        @DisplayName("Refresh Token 캐시를 삭제한다")
        void revokeRefreshToken_ValidToken_DeletesCache() {
            // given
            String refreshToken = AuthResponseFixtures.DEFAULT_REFRESH_TOKEN;
            willDoNothing().given(refreshTokenCacheCommandManager).delete(refreshToken);

            // when
            sut.revokeRefreshToken(refreshToken);

            // then
            then(refreshTokenCacheCommandManager).should().delete(refreshToken);
        }

        @Test
        @DisplayName("다른 Refresh Token 캐시를 삭제할 때 refreshTokenCacheCommandManager.delete를 호출한다")
        void revokeRefreshToken_AnotherToken_CallsDelete() {
            // given
            String refreshToken = "other.refresh.token";
            willDoNothing().given(refreshTokenCacheCommandManager).delete(refreshToken);

            // when
            sut.revokeRefreshToken(refreshToken);

            // then
            then(refreshTokenCacheCommandManager).should().delete(refreshToken);
        }
    }
}
