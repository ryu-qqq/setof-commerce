package com.ryuqq.setof.application.navigation.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.navigation.NavigationQueryFixtures;
import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
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
@DisplayName("NavigationMenuQueryManager 단위 테스트")
class NavigationMenuQueryManagerTest {

    @InjectMocks private NavigationMenuQueryManager sut;

    @Mock private NavigationMenuQueryPort queryPort;

    @Nested
    @DisplayName("fetchNavigationMenus() - 네비게이션 메뉴 목록 조회")
    class FetchNavigationMenusTest {

        @Test
        @DisplayName("전시 중인 네비게이션 메뉴 목록을 조회하여 반환한다")
        void fetchNavigationMenus_ReturnsActiveMenuList() {
            // given
            List<NavigationMenu> expected = NavigationQueryFixtures.activeNavigationMenus();

            given(queryPort.fetchNavigationMenus()).willReturn(expected);

            // when
            List<NavigationMenu> result = sut.fetchNavigationMenus();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchNavigationMenus();
        }

        @Test
        @DisplayName("전시 중인 메뉴가 없으면 빈 목록을 반환한다")
        void fetchNavigationMenus_NoDisplayMenus_ReturnsEmptyList() {
            // given
            given(queryPort.fetchNavigationMenus()).willReturn(List.of());

            // when
            List<NavigationMenu> result = sut.fetchNavigationMenus();

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchNavigationMenus();
        }

        @Test
        @DisplayName("Port에 조회를 위임하고 반환된 목록을 그대로 전달한다")
        void fetchNavigationMenus_DelegatesTo_QueryPort() {
            // given
            List<NavigationMenu> expected = NavigationQueryFixtures.activeNavigationMenus();

            given(queryPort.fetchNavigationMenus()).willReturn(expected);

            // when
            List<NavigationMenu> result = sut.fetchNavigationMenus();

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchNavigationMenus();
            then(queryPort).shouldHaveNoMoreInteractions();
        }
    }
}
