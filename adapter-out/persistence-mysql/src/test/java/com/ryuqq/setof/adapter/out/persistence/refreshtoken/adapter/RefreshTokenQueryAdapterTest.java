package com.ryuqq.setof.adapter.out.persistence.refreshtoken.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.mapper.RefreshTokenJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository.RefreshTokenQueryDslRepository;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * RefreshTokenQueryAdapter 통합 테스트
 *
 * <p>RefreshTokenQueryPort 구현체의 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefreshTokenQueryAdapter 통합 테스트")
@Import({
    RefreshTokenQueryAdapter.class,
    RefreshTokenJpaEntityMapper.class,
    RefreshTokenQueryDslRepository.class
})
class RefreshTokenQueryAdapterTest extends JpaSliceTestSupport {

    @Autowired private RefreshTokenQueryAdapter refreshTokenQueryAdapter;

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
    private static final String TOKEN_VALUE =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test-refresh-token";
    private static final long EXPIRES_IN_SECONDS = 604800L; // 7 days
    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    private RefreshTokenJpaEntity createTestTokenEntity(
            String memberId, String token, Instant createdAt) {
        return RefreshTokenJpaEntity.of(
                memberId, token, createdAt.plusSeconds(EXPIRES_IN_SECONDS), createdAt);
    }

    @Nested
    @DisplayName("findTokenByMemberId 메서드")
    class FindTokenByMemberId {

        @BeforeEach
        void setUp() {
            // 시간 순서대로 저장 (오래된 것부터)
            persistAndFlush(
                    createTestTokenEntity(MEMBER_ID, TOKEN_VALUE + "_old", NOW.minusSeconds(3600)));
            persistAndFlush(createTestTokenEntity(MEMBER_ID, TOKEN_VALUE + "_newest", NOW));
            persistAndFlush(
                    createTestTokenEntity(
                            MEMBER_ID, TOKEN_VALUE + "_middle", NOW.minusSeconds(1800)));
        }

        @Test
        @DisplayName("성공 - 회원 ID로 가장 최근 토큰을 조회한다")
        void findTokenByMemberId_returnsLatestToken() {
            // When
            Optional<String> result = refreshTokenQueryAdapter.findTokenByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TOKEN_VALUE + "_newest");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 조회 시 빈 Optional 반환")
        void findTokenByMemberId_nonExistingMemberId_returnsEmpty() {
            // When
            Optional<String> result =
                    refreshTokenQueryAdapter.findTokenByMemberId("non-existing-member-id");

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findMemberIdByToken 메서드")
    class FindMemberIdByToken {

        @BeforeEach
        void setUp() {
            persistAndFlush(createTestTokenEntity(MEMBER_ID, TOKEN_VALUE, NOW));
            persistAndFlush(createTestTokenEntity("other-member-id", TOKEN_VALUE + "_other", NOW));
        }

        @Test
        @DisplayName("성공 - 토큰 값으로 회원 ID를 조회한다")
        void findMemberIdByToken_existingToken_returnsMemberId() {
            // When
            Optional<String> result = refreshTokenQueryAdapter.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(MEMBER_ID);
        }

        @Test
        @DisplayName("성공 - 다른 토큰으로 해당 회원 ID를 조회한다")
        void findMemberIdByToken_otherToken_returnsOtherMemberId() {
            // When
            Optional<String> result =
                    refreshTokenQueryAdapter.findMemberIdByToken(TOKEN_VALUE + "_other");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo("other-member-id");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 토큰으로 조회 시 빈 Optional 반환")
        void findMemberIdByToken_nonExistingToken_returnsEmpty() {
            // When
            Optional<String> result =
                    refreshTokenQueryAdapter.findMemberIdByToken("non-existing-token");

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("토큰 조회 시나리오")
    class TokenQueryScenarios {

        @Test
        @DisplayName("성공 - 단일 토큰만 존재할 때 해당 토큰 반환")
        void findTokenByMemberId_singleToken_returnsThatToken() {
            // Given
            persistAndFlush(createTestTokenEntity(MEMBER_ID, TOKEN_VALUE, NOW));

            // When
            Optional<String> result = refreshTokenQueryAdapter.findTokenByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TOKEN_VALUE);
        }

        @Test
        @DisplayName("성공 - 토큰이 없는 회원 조회 시 빈 Optional 반환")
        void findTokenByMemberId_noTokensForMember_returnsEmpty() {
            // Given
            persistAndFlush(createTestTokenEntity("other-member-id", TOKEN_VALUE, NOW));

            // When
            Optional<String> result = refreshTokenQueryAdapter.findTokenByMemberId(MEMBER_ID);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 양방향 조회가 일관성 있게 동작한다")
        void bidirectionalQuery_consistentResults() {
            // Given
            persistAndFlush(createTestTokenEntity(MEMBER_ID, TOKEN_VALUE, NOW));

            // When
            Optional<String> tokenResult = refreshTokenQueryAdapter.findTokenByMemberId(MEMBER_ID);
            Optional<String> memberIdResult =
                    refreshTokenQueryAdapter.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertThat(tokenResult).isPresent();
            assertThat(memberIdResult).isPresent();
            assertThat(tokenResult.get()).isEqualTo(TOKEN_VALUE);
            assertThat(memberIdResult.get()).isEqualTo(MEMBER_ID);
        }
    }
}
