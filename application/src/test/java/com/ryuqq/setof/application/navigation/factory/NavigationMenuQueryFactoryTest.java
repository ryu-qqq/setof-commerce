package com.ryuqq.setof.application.navigation.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.navigation.NavigationQueryFixtures;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSortKey;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NavigationMenuQueryFactory 단위 테스트")
class NavigationMenuQueryFactoryTest {

    private final NavigationMenuQueryFactory sut = new NavigationMenuQueryFactory();

    @Nested
    @DisplayName("create() - SearchParams → SearchCriteria 변환")
    class CreateTest {

        @Test
        @DisplayName("기본 SearchParams로부터 NavigationMenuSearchCriteria를 생성한다")
        void create_DefaultParams_CreatesCriteria() {
            // given
            NavigationMenuSearchParams params = NavigationQueryFixtures.searchParams();

            // when
            NavigationMenuSearchCriteria result = sut.create(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.displayPeriodStart()).isNull();
            assertThat(result.displayPeriodEnd()).isNull();
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("전시 기간 필터가 설정된 SearchCriteria를 생성한다")
        void create_WithDisplayPeriod_CreatesWithPeriodFilter() {
            // given
            Instant start = Instant.parse("2025-01-01T00:00:00Z");
            Instant end = Instant.parse("2025-12-31T23:59:59Z");
            NavigationMenuSearchParams params =
                    NavigationQueryFixtures.searchParamsWithPeriod(start, end);

            // when
            NavigationMenuSearchCriteria result = sut.create(params);

            // then
            assertThat(result.displayPeriodStart()).isEqualTo(start);
            assertThat(result.displayPeriodEnd()).isEqualTo(end);
        }

        @Test
        @DisplayName("정렬 기준은 DISPLAY_ORDER ASC로 고정된다")
        void create_SortKeyIsDisplayOrderAsc() {
            // given
            NavigationMenuSearchParams params = NavigationQueryFixtures.searchParams();

            // when
            NavigationMenuSearchCriteria result = sut.create(params);

            // then
            assertThat(result.queryContext().sortKey())
                    .isEqualTo(NavigationMenuSortKey.DISPLAY_ORDER);
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.ASC);
        }

        @Test
        @DisplayName("페이징 정보는 UNPAGED(전체 조회)로 고정된다")
        void create_PageRequestIsUnpaged() {
            // given
            NavigationMenuSearchParams params = NavigationQueryFixtures.searchParams();

            // when
            NavigationMenuSearchCriteria result = sut.create(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isZero();
            assertThat(result.queryContext().pageRequest().size())
                    .isEqualTo(PageRequest.UNPAGED_SIZE);
        }

        @Test
        @DisplayName("전시 기간 시작만 있어도 SearchCriteria를 생성한다")
        void create_WithStartOnlyPeriod_CreatesCriteria() {
            // given
            Instant start = Instant.parse("2025-01-01T00:00:00Z");
            NavigationMenuSearchParams params =
                    NavigationQueryFixtures.searchParamsWithStartOnly(start);

            // when
            NavigationMenuSearchCriteria result = sut.create(params);

            // then
            assertThat(result.displayPeriodStart()).isEqualTo(start);
            assertThat(result.displayPeriodEnd()).isNull();
        }
    }
}
