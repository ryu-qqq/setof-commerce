package com.ryuqq.setof.application.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
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
@DisplayName("RefreshTokenCacheReadManager 단위 테스트")
class RefreshTokenCacheReadManagerTest {

    @InjectMocks private RefreshTokenCacheReadManager sut;

    @Mock private RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;

    @Nested
    @DisplayName("findMemberIdByRefreshToken() - Refresh Token으로 회원 ID 조회")
    class FindMemberIdByRefreshTokenTest {

        @Test
        @DisplayName("캐시에 존재하는 Refresh Token으로 회원 ID를 조회한다")
        void findMemberIdByRefreshToken_ExistingToken_ReturnsMemberId() {
            // given
            String refreshToken = "test.refresh.token";
            String expectedMemberId = "1";

            given(refreshTokenCacheQueryPort.findMemberIdByToken(any()))
                    .willReturn(Optional.of(expectedMemberId));

            // when
            Optional<String> result = sut.findMemberIdByRefreshToken(refreshToken);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedMemberId);
            then(refreshTokenCacheQueryPort).should().findMemberIdByToken(any());
        }

        @Test
        @DisplayName("캐시에 존재하지 않는 Refresh Token은 Optional.empty()를 반환한다")
        void findMemberIdByRefreshToken_NonExistingToken_ReturnsEmpty() {
            // given
            String refreshToken = "expired.refresh.token";

            given(refreshTokenCacheQueryPort.findMemberIdByToken(any()))
                    .willReturn(Optional.empty());

            // when
            Optional<String> result = sut.findMemberIdByRefreshToken(refreshToken);

            // then
            assertThat(result).isEmpty();
            then(refreshTokenCacheQueryPort).should().findMemberIdByToken(any());
        }

        @Test
        @DisplayName("다른 회원의 Refresh Token으로 해당 회원 ID를 반환한다")
        void findMemberIdByRefreshToken_AnotherMembersToken_ReturnsDifferentMemberId() {
            // given
            String refreshToken = "other.member.refresh.token";
            String expectedMemberId = "999";

            given(refreshTokenCacheQueryPort.findMemberIdByToken(any()))
                    .willReturn(Optional.of(expectedMemberId));

            // when
            Optional<String> result = sut.findMemberIdByRefreshToken(refreshToken);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedMemberId);
        }
    }
}
