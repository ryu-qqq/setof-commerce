package com.ryuqq.setof.adapter.out.persistence.navigation.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.mapper.NavigationMenuJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuQueryDslRepository;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.util.List;
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
    // 1. fetchNavigationMenus 테스트
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
}
