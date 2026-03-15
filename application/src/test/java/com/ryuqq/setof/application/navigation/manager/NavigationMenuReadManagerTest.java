package com.ryuqq.setof.application.navigation.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.navigation.NavigationQueryFixtures;
import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.exception.NavigationException;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSortKey;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import java.util.Collections;
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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("NavigationMenuReadManager 단위 테스트")
class NavigationMenuReadManagerTest {

    @InjectMocks private NavigationMenuReadManager sut;

    @Mock private NavigationMenuQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 네비게이션 메뉴 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 네비게이션 메뉴를 ID로 조회한다")
        void getById_ExistingNavigationMenu_ReturnsNavigationMenu() {
            // given
            long navigationMenuId = 1L;
            NavigationMenu expected = NavigationFixtures.activeNavigationMenu(navigationMenuId);

            given(queryPort.findById(navigationMenuId)).willReturn(Optional.of(expected));

            // when
            NavigationMenu result = sut.getById(navigationMenuId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(navigationMenuId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 NavigationException이 발생한다")
        void getById_NonExistingNavigationMenu_ThrowsNavigationException() {
            // given
            long navigationMenuId = 999L;

            given(queryPort.findById(navigationMenuId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(navigationMenuId))
                    .isInstanceOf(NavigationException.class);
            then(queryPort).should().findById(navigationMenuId);
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 네비게이션 메뉴 목록 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 네비게이션 메뉴 목록을 조회한다")
        void findByCriteria_ValidCriteria_ReturnsNavigationMenuList() {
            // given
            NavigationMenuSearchCriteria criteria = defaultCriteria();
            List<NavigationMenu> expected = NavigationQueryFixtures.activeNavigationMenus();

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<NavigationMenu> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            NavigationMenuSearchCriteria criteria = defaultCriteria();

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<NavigationMenu> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByCriteria(criteria);
        }
    }

    private NavigationMenuSearchCriteria defaultCriteria() {
        QueryContext<NavigationMenuSortKey> queryContext =
                QueryContext.of(
                        NavigationMenuSortKey.defaultKey(),
                        SortDirection.ASC,
                        PageRequest.of(0, PageRequest.UNPAGED_SIZE));
        return NavigationMenuSearchCriteria.of(null, null, queryContext);
    }
}
