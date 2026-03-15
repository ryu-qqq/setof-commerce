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

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 메뉴를 반환합니다")
        void shouldFindById() {
            // given
            NavigationMenuJpaEntity entity =
                    jpaRepository.save(NavigationMenuJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            java.util.Optional<NavigationMenuJpaEntity> result =
                    queryDslRepository.findById(entity.getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(entity.getId());
        }

        @Test
        @DisplayName("삭제된 메뉴는 조회되지 않습니다 (Soft Delete)")
        void shouldNotFindDeletedMenu() {
            // given
            NavigationMenuJpaEntity deleted = jpaRepository.save(createDeletedMenu());
            flushAndClear();

            // when
            java.util.Optional<NavigationMenuJpaEntity> result =
                    queryDslRepository.findById(deleted.getId());

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void shouldReturnEmptyWhenNotFound() {
            // when
            java.util.Optional<NavigationMenuJpaEntity> result =
                    queryDslRepository.findById(999999L);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("searchMenus 테스트")
    class SearchMenusTest {

        @Test
        @DisplayName("삭제된 메뉴는 검색 결과에서 제외됩니다")
        void shouldExcludeDeletedMenus() {
            // given
            NavigationMenuJpaEntity active =
                    jpaRepository.save(NavigationMenuJpaEntityFixtures.newEntity());
            NavigationMenuJpaEntity deleted = jpaRepository.save(createDeletedMenu());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.searchMenus(null, null);

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).contains(active.getId());
            assertThat(ids).doesNotContain(deleted.getId());
        }

        @Test
        @DisplayName("비활성 메뉴도 검색 결과에 포함됩니다 (searchMenus는 active 조건 없음)")
        void shouldIncludeInactiveMenus() {
            // given
            NavigationMenuJpaEntity active =
                    jpaRepository.save(NavigationMenuJpaEntityFixtures.newEntity());
            NavigationMenuJpaEntity inactive = jpaRepository.save(createInactiveMenu());
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.searchMenus(null, null);

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).contains(active.getId(), inactive.getId());
        }

        @Test
        @DisplayName("displayStartAfter 조건으로 특정 기간 이후 시작 메뉴만 조회합니다")
        void shouldFilterByDisplayStartAfter() {
            // given
            Instant now = Instant.now();
            // 과거에 시작한 메뉴
            NavigationMenuJpaEntity pastStartMenu =
                    jpaRepository.save(
                            NavigationMenuJpaEntity.create(
                                    null,
                                    "과거시작",
                                    NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                                    1,
                                    now.minusSeconds(7200),
                                    now.plusSeconds(86400),
                                    true,
                                    now,
                                    now,
                                    null));
            // 최근에 시작한 메뉴
            NavigationMenuJpaEntity recentStartMenu =
                    jpaRepository.save(
                            NavigationMenuJpaEntity.create(
                                    null,
                                    "최근시작",
                                    NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                                    2,
                                    now.minusSeconds(1800),
                                    now.plusSeconds(86400),
                                    true,
                                    now,
                                    now,
                                    null));
            flushAndClear();

            // when - 1시간 전 이후에 시작한 메뉴만
            List<NavigationMenuJpaEntity> result =
                    queryDslRepository.searchMenus(now.minusSeconds(3600), null);

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).contains(recentStartMenu.getId());
            assertThat(ids).doesNotContain(pastStartMenu.getId());
        }

        @Test
        @DisplayName("displayEndBefore 조건으로 특정 기간 이전 종료 메뉴만 조회합니다")
        void shouldFilterByDisplayEndBefore() {
            // given
            Instant now = Instant.now();
            // 곧 종료되는 메뉴 (1시간 후 종료)
            NavigationMenuJpaEntity soonEndMenu =
                    jpaRepository.save(
                            NavigationMenuJpaEntity.create(
                                    null,
                                    "곧종료",
                                    NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                                    1,
                                    now.minusSeconds(3600),
                                    now.plusSeconds(3600),
                                    true,
                                    now,
                                    now,
                                    null));
            // 나중에 종료되는 메뉴 (1일 후 종료)
            NavigationMenuJpaEntity lateEndMenu =
                    jpaRepository.save(
                            NavigationMenuJpaEntity.create(
                                    null,
                                    "나중종료",
                                    NavigationMenuJpaEntityFixtures.DEFAULT_LINK_URL,
                                    2,
                                    now.minusSeconds(3600),
                                    now.plusSeconds(86400),
                                    true,
                                    now,
                                    now,
                                    null));
            flushAndClear();

            // when - 12시간 이전에 종료되는 메뉴만
            List<NavigationMenuJpaEntity> result =
                    queryDslRepository.searchMenus(null, now.plusSeconds(43200));

            // then
            List<Long> ids = result.stream().map(NavigationMenuJpaEntity::getId).toList();
            assertThat(ids).contains(soonEndMenu.getId());
            assertThat(ids).doesNotContain(lateEndMenu.getId());
        }

        @Test
        @DisplayName("결과는 displayOrder 오름차순으로 정렬됩니다")
        void shouldReturnMenusOrderedByDisplayOrder() {
            // given
            jpaRepository.save(createMenuWithOrder("세번째", 3));
            jpaRepository.save(createMenuWithOrder("첫번째", 1));
            jpaRepository.save(createMenuWithOrder("두번째", 2));
            flushAndClear();

            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.searchMenus(null, null);

            // then
            List<Integer> orders =
                    result.stream().map(NavigationMenuJpaEntity::getDisplayOrder).toList();
            assertThat(orders).isSortedAccordingTo(Integer::compareTo);
        }

        @Test
        @DisplayName("데이터가 없으면 빈 목록을 반환합니다")
        void shouldReturnEmptyListWhenNoData() {
            // when
            List<NavigationMenuJpaEntity> result = queryDslRepository.searchMenus(null, null);

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
