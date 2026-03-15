package com.ryuqq.setof.integration.test.repository.navigation;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuQueryDslRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NavigationMenu QueryDSL Repository 통합 테스트.
 *
 * <p>NavigationMenuQueryDslRepository의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>active 조건 필터링
 *   <li>displayOrder 정렬
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.NAVIGATION)
@DisplayName("네비게이션 메뉴 QueryDSL Repository 테스트")
class NavigationMenuQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private NavigationMenuJpaRepository jpaRepository;
    @Autowired private NavigationMenuQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("fetchDisplayMenus 테스트")
    class FetchDisplayMenusTest {

        @Test
        @DisplayName("활성 메뉴를 조회합니다")
        void shouldFetchActiveMenus() {
            // given
            NavigationMenuJpaEntity entity =
                    jpaRepository.save(NavigationMenuJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).anyMatch(m -> m.getId().equals(entity.getId()));
        }

        @Test
        @DisplayName("삭제된 메뉴는 조회되지 않습니다 (Soft Delete)")
        void shouldNotFetchDeletedMenu() {
            // given
            NavigationMenuJpaEntity deleted = jpaRepository.save(createDeletedMenu());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).doesNotContain(deleted.getId());
        }

        @Test
        @DisplayName("비활성 메뉴는 조회되지 않습니다")
        void shouldNotFetchInactiveMenu() {
            // given
            NavigationMenuJpaEntity inactive = jpaRepository.save(createInactiveMenu());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).doesNotContain(inactive.getId());
        }

        @Test
        @DisplayName("활성/비활성/삭제 메뉴 혼재 시 활성 메뉴만 반환합니다")
        void shouldReturnOnlyActiveMenus() {
            // given
            NavigationMenuJpaEntity active1 =
                    jpaRepository.save(NavigationMenuJpaEntityFixtures.newEntity());
            NavigationMenuJpaEntity active2 =
                    jpaRepository.save(
                            NavigationMenuJpaEntityFixtures.activeEntityWithTitle("쇼핑", 2));
            NavigationMenuJpaEntity inactive = jpaRepository.save(createInactiveMenu());
            NavigationMenuJpaEntity deleted = jpaRepository.save(createDeletedMenu());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then - 활성 메뉴만 포함
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).contains(active1.getId(), active2.getId());
            assertThat(ids).doesNotContain(inactive.getId(), deleted.getId());
        }

        @Test
        @DisplayName("메뉴는 displayOrder 오름차순으로 정렬됩니다")
        void shouldReturnMenusOrderedByDisplayOrder() {
            // given
            jpaRepository.save(createMenuWithOrder("세번째", 3));
            jpaRepository.save(createMenuWithOrder("첫번째", 1));
            jpaRepository.save(createMenuWithOrder("두번째", 2));
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then
            List<Integer> orders =
                    result.stream().map(NavigationMenuJpaEntity::getDisplayOrder).toList();
            assertThat(orders).isSortedAccordingTo(Integer::compareTo);
        }

        @Test
        @DisplayName("메뉴가 없으면 빈 목록을 반환합니다")
        void shouldReturnEmptyListWhenNoMenus() {
            // when (아무 데이터도 저장하지 않음)
            List<NavigationMenuJpaEntity> result = queryDslRepository.fetchDisplayMenus();

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private NavigationMenuJpaEntity createDeletedMenu() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                null,
                NavigationMenuJpaEntityFixtures.DEFAULT_TITLE,
                NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    private NavigationMenuJpaEntity createInactiveMenu() {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                null,
                NavigationMenuJpaEntityFixtures.DEFAULT_TITLE,
                NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    private NavigationMenuJpaEntity createMenuWithOrder(String title, int displayOrder) {
        Instant now = Instant.now();
        return NavigationMenuJpaEntity.create(
                null,
                title,
                NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                displayOrder,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }
}
