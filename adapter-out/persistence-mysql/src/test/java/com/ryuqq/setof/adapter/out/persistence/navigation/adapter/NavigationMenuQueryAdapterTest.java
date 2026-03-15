package com.ryuqq.setof.adapter.out.persistence.navigation.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.mapper.NavigationMenuJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuQueryDslRepository;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSortKey;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * NavigationMenuQueryAdapterTest - 네비게이션 메뉴 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("NavigationMenuQueryAdapter 단위 테스트")
class NavigationMenuQueryAdapterTest {

    @Mock private NavigationMenuQueryDslRepository queryDslRepository;

    @Mock private NavigationMenuJpaEntityMapper mapper;

    @InjectMocks private NavigationMenuQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 NavigationMenu Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            long menuId = NavigationMenuJpaEntityFixtures.DEFAULT_ID;
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity(menuId);
            NavigationMenu domain = NavigationFixtures.activeNavigationMenu(menuId);

            given(queryDslRepository.findById(menuId)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<NavigationMenu> result = queryAdapter.findById(menuId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
            then(queryDslRepository).should().findById(menuId);
            then(mapper).should().toDomain(entity);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            long nonExistentId = 999L;
            given(queryDslRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<NavigationMenu> result = queryAdapter.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findById(nonExistentId);
            then(mapper).shouldHaveNoInteractions();
        }
    }

    // ========================================================================
    // 2. fetchNavigationMenus 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchNavigationMenus 메서드 테스트")
    class FetchNavigationMenusTest {

        @Test
        @DisplayName("전시 중인 네비게이션 메뉴 목록을 반환합니다")
        void fetchNavigationMenus_ReturnsMenuList() {
            // given
            NavigationMenuJpaEntity entity1 = NavigationMenuJpaEntityFixtures.activeEntity(1L);
            NavigationMenuJpaEntity entity2 = NavigationMenuJpaEntityFixtures.activeEntity(2L);
            NavigationMenu domain1 = NavigationFixtures.activeNavigationMenu(1L);
            NavigationMenu domain2 = NavigationFixtures.activeNavigationMenu(2L);

            given(queryDslRepository.fetchDisplayMenus()).willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<NavigationMenu> result = queryAdapter.fetchNavigationMenus();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository).should().fetchDisplayMenus();
        }

        @Test
        @DisplayName("전시 중인 메뉴가 없으면 빈 리스트를 반환합니다")
        void fetchNavigationMenus_WithNoResults_ReturnsEmptyList() {
            // given
            given(queryDslRepository.fetchDisplayMenus()).willReturn(List.of());

            // when
            List<NavigationMenu> result = queryAdapter.fetchNavigationMenus();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("단일 메뉴만 있을 때 단건 목록을 반환합니다")
        void fetchNavigationMenus_WithSingleMenu_ReturnsSingleElementList() {
            // given
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity();
            NavigationMenu domain = NavigationFixtures.activeNavigationMenu();

            given(queryDslRepository.fetchDisplayMenus()).willReturn(List.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            List<NavigationMenu> result = queryAdapter.fetchNavigationMenus();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(domain);
        }
    }

    // ========================================================================
    // 3. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("전시 기간 조건으로 메뉴 목록을 반환합니다")
        void findByCriteria_WithDisplayPeriodCondition_ReturnsMenuList() {
            // given
            Instant startAfter = Instant.now().minusSeconds(3600);
            Instant endBefore = Instant.now().plusSeconds(86400);
            NavigationMenuSearchCriteria criteria =
                    NavigationMenuSearchCriteria.of(
                            startAfter,
                            endBefore,
                            QueryContext.defaultOf(NavigationMenuSortKey.CREATED_AT));

            NavigationMenuJpaEntity entity1 = NavigationMenuJpaEntityFixtures.activeEntity(1L);
            NavigationMenuJpaEntity entity2 = NavigationMenuJpaEntityFixtures.activeEntity(2L);
            NavigationMenu domain1 = NavigationFixtures.activeNavigationMenu(1L);
            NavigationMenu domain2 = NavigationFixtures.activeNavigationMenu(2L);

            given(queryDslRepository.searchMenus(startAfter, endBefore))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<NavigationMenu> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository).should().searchMenus(startAfter, endBefore);
        }

        @Test
        @DisplayName("조건 없이 조회 시 전체 메뉴를 반환합니다")
        void findByCriteria_WithNullConditions_ReturnsAllMenus() {
            // given
            NavigationMenuSearchCriteria criteria =
                    NavigationMenuSearchCriteria.of(
                            null, null, QueryContext.defaultOf(NavigationMenuSortKey.CREATED_AT));

            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity();
            NavigationMenu domain = NavigationFixtures.activeNavigationMenu();

            given(queryDslRepository.searchMenus(null, null)).willReturn(List.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            List<NavigationMenu> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            then(queryDslRepository).should().searchMenus(null, null);
        }

        @Test
        @DisplayName("결과가 없으면 빈 목록을 반환합니다")
        void findByCriteria_WithNoResults_ReturnsEmptyList() {
            // given
            NavigationMenuSearchCriteria criteria =
                    NavigationMenuSearchCriteria.of(
                            null, null, QueryContext.defaultOf(NavigationMenuSortKey.CREATED_AT));

            given(queryDslRepository.searchMenus(null, null)).willReturn(List.of());

            // when
            List<NavigationMenu> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(mapper).shouldHaveNoInteractions();
        }
    }
}
