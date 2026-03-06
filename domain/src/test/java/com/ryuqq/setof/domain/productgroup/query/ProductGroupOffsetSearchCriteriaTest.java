package com.ryuqq.setof.domain.productgroup.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupOffsetSearchCriteria 테스트")
class ProductGroupOffsetSearchCriteriaTest {

    private QueryContext<ProductGroupSortKey> defaultQueryContext() {
        return QueryContext.defaultOf(ProductGroupSortKey.defaultKey());
    }

    @Nested
    @DisplayName("defaultCriteria() - 기본 검색 조건")
    class DefaultCriteriaTest {

        @Test
        @DisplayName("기본 검색 조건을 생성한다")
        void createDefaultCriteria() {
            // when
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();

            // then
            assertThat(criteria.statuses()).isEmpty();
            assertThat(criteria.sellerIds()).isEmpty();
            assertThat(criteria.brandIds()).isEmpty();
            assertThat(criteria.categoryIds()).isEmpty();
            assertThat(criteria.productGroupIds()).isEmpty();
            assertThat(criteria.searchField()).isNull();
            assertThat(criteria.searchWord()).isNull();
            assertThat(criteria.dateRange()).isNull();
            assertThat(criteria.queryContext()).isNotNull();
        }
    }

    @Nested
    @DisplayName("of() - 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("모든 필터를 포함하여 생성한다")
        void createWithAllFilters() {
            // given
            List<ProductGroupStatus> statuses = List.of(ProductGroupStatus.ACTIVE);
            List<Long> sellerIds = List.of(1L, 2L);
            List<Long> brandIds = List.of(10L);
            List<Long> categoryIds = List.of(100L);
            List<Long> productGroupIds = List.of(1000L);
            DateRange dateRange = DateRange.lastDays(7);
            QueryContext<ProductGroupSortKey> queryContext = defaultQueryContext();

            // when
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            statuses,
                            sellerIds,
                            brandIds,
                            categoryIds,
                            productGroupIds,
                            ProductGroupSearchField.PRODUCT_GROUP_NAME,
                            "검색어",
                            null,
                            null,
                            dateRange,
                            queryContext);

            // then
            assertThat(criteria.statuses()).containsExactly(ProductGroupStatus.ACTIVE);
            assertThat(criteria.sellerIds()).containsExactlyInAnyOrder(1L, 2L);
            assertThat(criteria.brandIds()).containsExactly(10L);
            assertThat(criteria.categoryIds()).containsExactly(100L);
            assertThat(criteria.productGroupIds()).containsExactly(1000L);
            assertThat(criteria.searchField())
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
            assertThat(criteria.searchWord()).isEqualTo("검색어");
            assertThat(criteria.dateRange()).isEqualTo(dateRange);
        }

        @Test
        @DisplayName("null 목록은 빈 목록으로 처리된다")
        void nullListsAreConvertedToEmpty() {
            // when
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            // then
            assertThat(criteria.statuses()).isEmpty();
            assertThat(criteria.sellerIds()).isEmpty();
            assertThat(criteria.brandIds()).isEmpty();
            assertThat(criteria.categoryIds()).isEmpty();
            assertThat(criteria.productGroupIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasStatusFilter() - 상태 필터 여부")
    class HasStatusFilterTest {

        @Test
        @DisplayName("상태 필터가 있으면 true를 반환한다")
        void returnsTrueWhenStatusFilterExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(ProductGroupStatus.ACTIVE),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasStatusFilter()).isTrue();
        }

        @Test
        @DisplayName("상태 필터가 없으면 false를 반환한다")
        void returnsFalseWhenNoStatusFilter() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasStatusFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasSellerFilter() - 판매자 필터 여부")
    class HasSellerFilterTest {

        @Test
        @DisplayName("판매자 필터가 있으면 true를 반환한다")
        void returnsTrueWhenSellerFilterExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(1L),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasSellerFilter()).isTrue();
        }

        @Test
        @DisplayName("판매자 필터가 없으면 false를 반환한다")
        void returnsFalseWhenNoSellerFilter() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasSellerFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasBrandFilter() - 브랜드 필터 여부")
    class HasBrandFilterTest {

        @Test
        @DisplayName("브랜드 필터가 있으면 true를 반환한다")
        void returnsTrueWhenBrandFilterExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(10L),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasBrandFilter()).isTrue();
        }

        @Test
        @DisplayName("브랜드 필터가 없으면 false를 반환한다")
        void returnsFalseWhenNoBrandFilter() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasBrandFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasCategoryFilter() - 카테고리 필터 여부")
    class HasCategoryFilterTest {

        @Test
        @DisplayName("카테고리 필터가 있으면 true를 반환한다")
        void returnsTrueWhenCategoryFilterExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(100L),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasCategoryFilter()).isTrue();
        }

        @Test
        @DisplayName("카테고리 필터가 없으면 false를 반환한다")
        void returnsFalseWhenNoCategoryFilter() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasCategoryFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasProductGroupIdFilter() - 상품그룹 ID 필터 여부")
    class HasProductGroupIdFilterTest {

        @Test
        @DisplayName("상품그룹 ID 필터가 있으면 true를 반환한다")
        void returnsTrueWhenProductGroupIdFilterExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(1000L),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasProductGroupIdFilter()).isTrue();
        }

        @Test
        @DisplayName("상품그룹 ID 필터가 없으면 false를 반환한다")
        void returnsFalseWhenNoProductGroupIdFilter() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasProductGroupIdFilter()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasDateRange() - 날짜 범위 필터 여부")
    class HasDateRangeTest {

        @Test
        @DisplayName("유효한 날짜 범위가 있으면 true를 반환한다")
        void returnsTrueWhenDateRangeExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            DateRange.lastDays(7),
                            defaultQueryContext());

            assertThat(criteria.hasDateRange()).isTrue();
        }

        @Test
        @DisplayName("날짜 범위가 null이면 false를 반환한다")
        void returnsFalseWhenDateRangeIsNull() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasDateRange()).isFalse();
        }

        @Test
        @DisplayName("날짜 범위가 비어있으면 false를 반환한다")
        void returnsFalseWhenDateRangeIsEmpty() {
            // given: startDate와 endDate 모두 null인 DateRange
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            new DateRange(null, null),
                            defaultQueryContext());

            assertThat(criteria.hasDateRange()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasSearchCondition() - 검색 조건 여부")
    class HasSearchConditionTest {

        @Test
        @DisplayName("검색어가 있으면 true를 반환한다")
        void returnsTrueWhenSearchWordExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            ProductGroupSearchField.PRODUCT_GROUP_NAME,
                            "테스트상품",
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasSearchCondition()).isTrue();
        }

        @Test
        @DisplayName("검색어가 null이면 false를 반환한다")
        void returnsFalseWhenSearchWordIsNull() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasSearchCondition()).isFalse();
        }

        @Test
        @DisplayName("검색어가 공백이면 false를 반환한다")
        void returnsFalseWhenSearchWordIsBlank() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            "   ",
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasSearchCondition()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasSearchField() - 검색 필드 여부")
    class HasSearchFieldTest {

        @Test
        @DisplayName("검색 필드가 있으면 true를 반환한다")
        void returnsTrueWhenSearchFieldExists() {
            // given
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            ProductGroupSearchField.PRODUCT_GROUP_NAME,
                            "검색어",
                            null,
                            null,
                            null,
                            defaultQueryContext());

            assertThat(criteria.hasSearchField()).isTrue();
        }

        @Test
        @DisplayName("검색 필드가 null이면 false를 반환한다")
        void returnsFalseWhenSearchFieldIsNull() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.hasSearchField()).isFalse();
        }
    }

    @Nested
    @DisplayName("페이징 편의 메서드 테스트")
    class PagingMethodTest {

        @Test
        @DisplayName("size()는 페이지 크기를 반환한다")
        void returnsSizeFromQueryContext() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.size()).isGreaterThan(0);
        }

        @Test
        @DisplayName("offset()은 오프셋을 반환한다")
        void returnsOffsetFromQueryContext() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.offset()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("page()는 페이지 번호를 반환한다")
        void returnsPageFromQueryContext() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.defaultCriteria();
            assertThat(criteria.page()).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("statuses 목록은 불변이다")
        void statusesListIsUnmodifiable() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(ProductGroupStatus.ACTIVE),
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> criteria.statuses().add(ProductGroupStatus.SOLD_OUT))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("sellerIds 목록은 불변이다")
        void sellerIdsListIsUnmodifiable() {
            ProductGroupOffsetSearchCriteria criteria =
                    ProductGroupOffsetSearchCriteria.of(
                            List.of(),
                            List.of(1L),
                            List.of(),
                            List.of(),
                            List.of(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            defaultQueryContext());

            org.assertj.core.api.Assertions.assertThatThrownBy(() -> criteria.sellerIds().add(2L))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
