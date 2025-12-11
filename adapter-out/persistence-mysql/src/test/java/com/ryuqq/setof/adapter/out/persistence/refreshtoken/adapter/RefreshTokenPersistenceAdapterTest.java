package com.ryuqq.setof.adapter.out.persistence.refreshtoken.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.mapper.RefreshTokenJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository.RefreshTokenQueryDslRepository;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * RefreshTokenPersistenceAdapter 통합 테스트
 *
 * <p>RefreshTokenPersistencePort 구현체의 저장/삭제 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("RefreshTokenPersistenceAdapter 통합 테스트")
@Import({
    RefreshTokenPersistenceAdapter.class,
    RefreshTokenJpaEntityMapper.class,
    RefreshTokenQueryDslRepository.class
})
class RefreshTokenPersistenceAdapterTest extends JpaSliceTestSupport {

    @Autowired private RefreshTokenPersistenceAdapter refreshTokenPersistenceAdapter;

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
    private static final String TOKEN_VALUE =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test-refresh-token";
    private static final long EXPIRES_IN_SECONDS = 604800L; // 7 days
    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    private RefreshTokenJpaEntity createTestTokenEntity(
            String memberId, String token, Instant expiresAt, Instant createdAt) {
        return RefreshTokenJpaEntity.of(memberId, token, expiresAt, createdAt);
    }

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - RefreshToken을 저장한다")
        void persist_refreshToken_savesSuccessfully() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.forNew(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, NOW);

            // When
            refreshTokenPersistenceAdapter.persist(refreshToken);
            flushAndClear();

            // Then
            List<RefreshTokenJpaEntity> savedEntities =
                    query(
                            "SELECT r FROM RefreshTokenJpaEntity r WHERE r.memberId = '"
                                    + MEMBER_ID
                                    + "'",
                            RefreshTokenJpaEntity.class);

            assertThat(savedEntities).isNotEmpty();
            RefreshTokenJpaEntity savedEntity = savedEntities.get(0);
            assertThat(savedEntity.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(savedEntity.getToken()).isEqualTo(TOKEN_VALUE);
            assertThat(savedEntity.getExpiresAt()).isNotNull();
            assertThat(savedEntity.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 동일 회원의 여러 토큰을 저장할 수 있다")
        void persist_multipleTokensForSameMember_savesSuccessfully() {
            // Given
            RefreshToken token1 =
                    RefreshToken.forNew(MEMBER_ID, TOKEN_VALUE + "_1", EXPIRES_IN_SECONDS, NOW);
            RefreshToken token2 =
                    RefreshToken.forNew(MEMBER_ID, TOKEN_VALUE + "_2", EXPIRES_IN_SECONDS, NOW);

            // When
            refreshTokenPersistenceAdapter.persist(token1);
            refreshTokenPersistenceAdapter.persist(token2);
            flushAndClear();

            // Then
            List<RefreshTokenJpaEntity> savedEntities =
                    query(
                            "SELECT r FROM RefreshTokenJpaEntity r WHERE r.memberId = '"
                                    + MEMBER_ID
                                    + "'",
                            RefreshTokenJpaEntity.class);

            assertThat(savedEntities).hasSize(2);
        }
    }

    @Nested
    @DisplayName("deleteByMemberId 메서드")
    class DeleteByMemberId {

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    createTestTokenEntity(
                            MEMBER_ID,
                            TOKEN_VALUE + "_1",
                            NOW.plusSeconds(EXPIRES_IN_SECONDS),
                            NOW));
            persistAndFlush(
                    createTestTokenEntity(
                            MEMBER_ID,
                            TOKEN_VALUE + "_2",
                            NOW.plusSeconds(EXPIRES_IN_SECONDS),
                            NOW));
            persistAndFlush(
                    createTestTokenEntity(
                            "other-member-id",
                            TOKEN_VALUE + "_other",
                            NOW.plusSeconds(EXPIRES_IN_SECONDS),
                            NOW));
        }

        @Test
        @DisplayName("성공 - 회원 ID로 모든 토큰을 삭제한다")
        void deleteByMemberId_deletesAllTokensForMember() {
            // When
            refreshTokenPersistenceAdapter.deleteByMemberId(MEMBER_ID);
            flushAndClear();

            // Then
            List<RefreshTokenJpaEntity> remainingForMember =
                    query(
                            "SELECT r FROM RefreshTokenJpaEntity r WHERE r.memberId = '"
                                    + MEMBER_ID
                                    + "'",
                            RefreshTokenJpaEntity.class);
            List<RefreshTokenJpaEntity> remainingForOther =
                    query(
                            "SELECT r FROM RefreshTokenJpaEntity r WHERE r.memberId ="
                                    + " 'other-member-id'",
                            RefreshTokenJpaEntity.class);

            assertThat(remainingForMember).isEmpty();
            assertThat(remainingForOther).hasSize(1);
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 회원 ID로 삭제 시도해도 에러 없음")
        void deleteByMemberId_nonExistingMemberId_noError() {
            // When & Then (no exception)
            refreshTokenPersistenceAdapter.deleteByMemberId("non-existing-member-id");
            flushAndClear();

            // 기존 데이터가 유지되는지 확인
            List<RefreshTokenJpaEntity> allTokens =
                    query("SELECT r FROM RefreshTokenJpaEntity r", RefreshTokenJpaEntity.class);
            assertThat(allTokens).hasSize(3);
        }
    }

    @Nested
    @DisplayName("deleteByToken 메서드")
    class DeleteByToken {

        private final String targetToken = TOKEN_VALUE + "_target";

        @BeforeEach
        void setUp() {
            persistAndFlush(
                    createTestTokenEntity(
                            MEMBER_ID, targetToken, NOW.plusSeconds(EXPIRES_IN_SECONDS), NOW));
            persistAndFlush(
                    createTestTokenEntity(
                            MEMBER_ID,
                            TOKEN_VALUE + "_other",
                            NOW.plusSeconds(EXPIRES_IN_SECONDS),
                            NOW));
        }

        @Test
        @DisplayName("성공 - 토큰 값으로 특정 토큰을 삭제한다")
        void deleteByToken_deletesSpecificToken() {
            // When
            refreshTokenPersistenceAdapter.deleteByToken(targetToken);
            flushAndClear();

            // Then
            List<RefreshTokenJpaEntity> remainingTokens =
                    query(
                            "SELECT r FROM RefreshTokenJpaEntity r WHERE r.memberId = '"
                                    + MEMBER_ID
                                    + "'",
                            RefreshTokenJpaEntity.class);

            assertThat(remainingTokens).hasSize(1);
            assertThat(remainingTokens.get(0).getToken()).isEqualTo(TOKEN_VALUE + "_other");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 토큰 삭제 시도해도 에러 없음")
        void deleteByToken_nonExistingToken_noError() {
            // When & Then (no exception)
            refreshTokenPersistenceAdapter.deleteByToken("non-existing-token");
            flushAndClear();

            // 기존 데이터가 유지되는지 확인
            List<RefreshTokenJpaEntity> allTokens =
                    query("SELECT r FROM RefreshTokenJpaEntity r", RefreshTokenJpaEntity.class);
            assertThat(allTokens).hasSize(2);
        }
    }
}
