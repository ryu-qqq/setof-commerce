package com.ryuqq.setof.application.navigation.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.navigation.NavigationQueryFixtures;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuQueryFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuReadManager;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSortKey;
import java.util.Collections;
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
@DisplayName("SearchNavigationMenusService 단위 테스트")
class SearchNavigationMenusServiceTest {

    @InjectMocks private SearchNavigationMenusService sut;

    @Mock private NavigationMenuReadManager readManager;
    @Mock private NavigationMenuQueryFactory queryFactory;

    @Nested
    @DisplayName("execute() - 네비게이션 메뉴 목록 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 네비게이션 메뉴 목록을 조회하고 반환한다")
        void execute_ValidParams_ReturnsNavigationMenuList() {
            // given
            NavigationMenuSearchParams params = NavigationQueryFixtures.searchParams();
            NavigationMenuSearchCriteria criteria = defaultCriteria();
            List<NavigationMenu> expected = NavigationQueryFixtures.activeNavigationMenus();

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(expected);

            // when
            List<NavigationMenu> result = sut.execute(params);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().create(params);
            then(readManager).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void execute_NoResults_ReturnsEmptyList() {
            // given
            NavigationMenuSearchParams params = NavigationQueryFixtures.searchParams();
            NavigationMenuSearchCriteria criteria = defaultCriteria();

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<NavigationMenu> result = sut.execute(params);

            // then
            assertThat(result).isEmpty();
            then(queryFactory).should().create(params);
            then(readManager).should().findByCriteria(criteria);
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
}
