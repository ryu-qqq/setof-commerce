package com.ryuqq.setof.integration.test.repository.contentpage;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageQueryDslRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ContentPage QueryDSL Repository 통합 테스트.
 *
 * <p>ContentPageQueryDslRepository의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>active 조건 필터링
 *   <li>bypass 옵션
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.CONTENT_PAGE)
@DisplayName("콘텐츠 페이지 QueryDSL Repository 테스트")
class ContentPageQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private ContentPageJpaRepository jpaRepository;
    @Autowired private ContentPageQueryDslRepository queryDslRepository;

    // ========================================================================
    // fetchById 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchById 테스트")
    class FetchByIdTest {

        @Test
        @DisplayName("활성 콘텐츠 페이지를 ID로 조회합니다")
        void shouldFetchActiveEntityById() {
            // given
            ContentPageJpaEntity entity =
                    jpaRepository.save(ContentPageJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found = queryDslRepository.fetchById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getTitle())
                    .isEqualTo(ContentPageJpaEntityFixtures.DEFAULT_TITLE);
        }

        @Test
        @DisplayName("삭제된 콘텐츠 페이지는 ID 조회에서 제외됩니다")
        void shouldNotFetchDeletedEntity() {
            // given
            ContentPageJpaEntity deleted = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found = queryDslRepository.fetchById(deleted.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("비활성 콘텐츠 페이지는 fetchById로 조회됩니다 (active 조건 없음)")
        void shouldFetchInactiveEntityById() {
            // given
            ContentPageJpaEntity inactive = jpaRepository.save(createInactiveEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found = queryDslRepository.fetchById(inactive.getId());

            // then
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void shouldReturnEmptyForNonExistentId() {
            // when
            Optional<ContentPageJpaEntity> found = queryDslRepository.fetchById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    // ========================================================================
    // fetchByIdWithBypass 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchByIdWithBypass 테스트")
    class FetchByIdWithBypassTest {

        @Test
        @DisplayName("bypass=false이면 활성 콘텐츠 페이지만 반환합니다")
        void shouldFetchActiveEntityWhenBypassFalse() {
            // given
            ContentPageJpaEntity active =
                    jpaRepository.save(ContentPageJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found =
                    queryDslRepository.fetchByIdWithBypass(active.getId(), false);

            // then
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("bypass=false이면 비활성 콘텐츠 페이지는 반환되지 않습니다")
        void shouldNotFetchInactiveEntityWhenBypassFalse() {
            // given
            ContentPageJpaEntity inactive = jpaRepository.save(createInactiveEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found =
                    queryDslRepository.fetchByIdWithBypass(inactive.getId(), false);

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("bypass=true이면 비활성 콘텐츠 페이지도 반환됩니다")
        void shouldFetchInactiveEntityWhenBypassTrue() {
            // given
            ContentPageJpaEntity inactive = jpaRepository.save(createInactiveEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found =
                    queryDslRepository.fetchByIdWithBypass(inactive.getId(), true);

            // then
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("bypass=true여도 삭제된 콘텐츠 페이지는 반환되지 않습니다")
        void shouldNotFetchDeletedEntityEvenWhenBypassTrue() {
            // given
            ContentPageJpaEntity deleted = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            Optional<ContentPageJpaEntity> found =
                    queryDslRepository.fetchByIdWithBypass(deleted.getId(), true);

            // then
            assertThat(found).isEmpty();
        }
    }

    // ========================================================================
    // fetchOnDisplayContentPageIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchOnDisplayContentPageIds 테스트")
    class FetchOnDisplayContentPageIdsTest {

        @Test
        @DisplayName("활성 콘텐츠 페이지 ID 목록을 반환합니다")
        void shouldFetchActiveContentPageIds() {
            // given
            ContentPageJpaEntity active1 =
                    jpaRepository.save(ContentPageJpaEntityFixtures.newEntity());
            ContentPageJpaEntity active2 =
                    jpaRepository.save(ContentPageJpaEntityFixtures.newEntity());
            ContentPageJpaEntity inactive = jpaRepository.save(createInactiveEntity());
            ContentPageJpaEntity deleted = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            List<Long> ids = queryDslRepository.fetchOnDisplayContentPageIds();

            // then
            assertThat(ids).contains(active1.getId(), active2.getId());
            assertThat(ids).doesNotContain(inactive.getId(), deleted.getId());
        }

        @Test
        @DisplayName("활성 페이지가 없으면 빈 리스트를 반환합니다")
        void shouldReturnEmptyListWhenNoActivePages() {
            // given
            jpaRepository.save(createInactiveEntity());
            jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            List<Long> ids = queryDslRepository.fetchOnDisplayContentPageIds();

            // then
            assertThat(ids).isEmpty();
        }

        @Test
        @DisplayName("콘텐츠 페이지가 하나도 없으면 빈 리스트를 반환합니다")
        void shouldReturnEmptyListWhenNoPagesExist() {
            // when (아무 데이터도 저장하지 않음)
            List<Long> ids = queryDslRepository.fetchOnDisplayContentPageIds();

            // then
            assertThat(ids).isEmpty();
        }
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private ContentPageJpaEntity createDeletedEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                null,
                ContentPageJpaEntityFixtures.DEFAULT_TITLE,
                ContentPageJpaEntityFixtures.DEFAULT_MEMO,
                ContentPageJpaEntityFixtures.DEFAULT_IMAGE_URL,
                ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    private ContentPageJpaEntity createInactiveEntity() {
        Instant now = Instant.now();
        return ContentPageJpaEntity.create(
                null,
                ContentPageJpaEntityFixtures.DEFAULT_TITLE,
                ContentPageJpaEntityFixtures.DEFAULT_MEMO,
                ContentPageJpaEntityFixtures.DEFAULT_IMAGE_URL,
                ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }
}
