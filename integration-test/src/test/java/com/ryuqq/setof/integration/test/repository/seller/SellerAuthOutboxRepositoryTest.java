package com.ryuqq.setof.integration.test.repository.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerAuthOutboxJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAuthOutboxJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAuthOutboxJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SellerAuthOutbox JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 인증 Outbox JPA Repository 테스트")
class SellerAuthOutboxRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerAuthOutboxJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("PENDING 상태 Outbox 저장 성공")
        void shouldSavePendingEntity() {
            // given
            SellerAuthOutboxJpaEntity entity = SellerAuthOutboxJpaEntityFixtures.newPendingEntity();

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<SellerAuthOutboxJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getStatus()).isEqualTo(SellerAuthOutboxJpaEntity.Status.PENDING);
            assertThat(found.get().getPayload())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_PAYLOAD);
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            SellerAuthOutboxJpaEntity entity = SellerAuthOutboxJpaEntityFixtures.newPendingEntity();

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAuthOutboxJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getSellerId())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_SELLER_ID);
            assertThat(found.getPayload())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_PAYLOAD);
            assertThat(found.getStatus()).isEqualTo(SellerAuthOutboxJpaEntity.Status.PENDING);
            assertThat(found.getRetryCount())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_RETRY_COUNT);
            assertThat(found.getMaxRetry())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_MAX_RETRY);
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getProcessedAt()).isNull();
            assertThat(found.getErrorMessage()).isNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<SellerAuthOutboxJpaEntity> entities =
                    List.of(
                            SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(1L),
                            SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(2L),
                            SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(3L));

            // when
            List<SellerAuthOutboxJpaEntity> saved = jpaRepository.saveAll(entities);
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(jpaRepository.count()).isEqualTo(3);
        }

        @Test
        @DisplayName("커스텀 페이로드로 저장")
        void shouldSaveWithCustomPayload() {
            // given
            String customPayload = "{\"customField\":\"customValue\"}";
            SellerAuthOutboxJpaEntity entity =
                    SellerAuthOutboxJpaEntityFixtures.entityWithPayload(customPayload);

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAuthOutboxJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getPayload()).isEqualTo(customPayload);
        }

        @Test
        @DisplayName("커스텀 최대 재시도 횟수로 저장")
        void shouldSaveWithCustomMaxRetry() {
            // given
            int customMaxRetry = 5;
            SellerAuthOutboxJpaEntity entity =
                    SellerAuthOutboxJpaEntityFixtures.entityWithMaxRetry(customMaxRetry);

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAuthOutboxJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getMaxRetry()).isEqualTo(customMaxRetry);
        }
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 Outbox 조회 성공")
        void shouldFindById() {
            // given
            SellerAuthOutboxJpaEntity entity =
                    jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntity());
            flushAndClear();

            // when
            Optional<SellerAuthOutboxJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getPayload())
                    .isEqualTo(SellerAuthOutboxJpaEntityFixtures.DEFAULT_PAYLOAD);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerAuthOutboxJpaEntity> found = jpaRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID는 true 반환")
        void shouldReturnTrueWhenExists() {
            // given
            SellerAuthOutboxJpaEntity entity =
                    jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntity());
            flushAndClear();

            // when
            boolean exists = jpaRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID는 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // when
            boolean exists = jpaRepository.existsById(999999L);

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("count 테스트")
    class CountTest {

        @Test
        @DisplayName("전체 개수 카운트")
        void shouldCountAll() {
            // given
            jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(1L));
            jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(2L));
            jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(3L));
            flushAndClear();

            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("빈 테이블 카운트")
        void shouldReturnZeroWhenEmpty() {
            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("delete 테스트")
    class DeleteTest {

        @Test
        @DisplayName("엔티티 삭제 성공")
        void shouldDeleteEntity() {
            // given
            SellerAuthOutboxJpaEntity entity =
                    jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntity());
            Long id = entity.getId();
            flushAndClear();

            // when
            jpaRepository.deleteById(id);
            flushAndClear();

            // then
            assertThat(jpaRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("전체 삭제 성공")
        void shouldDeleteAll() {
            // given
            jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(1L));
            jpaRepository.save(SellerAuthOutboxJpaEntityFixtures.newPendingEntityWithSellerId(2L));
            flushAndClear();

            // when
            jpaRepository.deleteAll();
            flushAndClear();

            // then
            assertThat(jpaRepository.count()).isZero();
        }
    }

    @Nested
    @DisplayName("상태별 저장 및 조회 테스트")
    class StatusTest {

        @Test
        @DisplayName("PENDING 상태 저장 및 조회")
        void shouldSaveAndRetrievePendingStatus() {
            // given
            SellerAuthOutboxJpaEntity entity = SellerAuthOutboxJpaEntityFixtures.newPendingEntity();

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAuthOutboxJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getStatus()).isEqualTo(SellerAuthOutboxJpaEntity.Status.PENDING);
        }
    }

    @Nested
    @DisplayName("재시도 횟수 테스트")
    class RetryCountTest {

        @Test
        @DisplayName("재시도 횟수가 있는 엔티티 저장 및 조회")
        void shouldSaveAndRetrieveWithRetryCount() {
            // given
            SellerAuthOutboxJpaEntity entity =
                    SellerAuthOutboxJpaEntityFixtures.retriedPendingEntity(2);

            // when
            SellerAuthOutboxJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAuthOutboxJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getRetryCount()).isEqualTo(2);
            assertThat(found.getErrorMessage()).isNotNull();
        }
    }
}
