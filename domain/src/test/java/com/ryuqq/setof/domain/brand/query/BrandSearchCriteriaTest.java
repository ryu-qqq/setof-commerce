package com.ryuqq.setof.domain.brand.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandSearchCriteria 테스트")
class BrandSearchCriteriaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 검색 조건을 생성한다")
        void createWithOf() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.of(
                            BrandSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(0, 20));

            // when
            BrandSearchCriteria criteria = BrandSearchCriteria.of(true, "테스트브랜드", queryContext);

            // then
            assertThat(criteria.displayed()).isTrue();
            assertThat(criteria.brandName()).isEqualTo("테스트브랜드");
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("defaultOf()로 기본 검색 조건을 생성한다")
        void createDefaultOf() {
            // when
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // then
            assertThat(criteria.displayed()).isNull();
            assertThat(criteria.brandName()).isNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(BrandSortKey.DISPLAY_ORDER);
        }

        @Test
        @DisplayName("displayedOnly()로 표시 중인 브랜드만 조회하는 조건을 생성한다")
        void createDisplayedOnly() {
            // when
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // then
            assertThat(criteria.displayed()).isTrue();
            assertThat(criteria.brandName()).isNull();
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null queryContext는 기본값으로 대체된다")
        void nullQueryContextDefaultsToDefault() {
            // when
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
            assertThat(criteria.queryContext().sortKey()).isEqualTo(BrandSortKey.DISPLAY_ORDER);
        }

        @Test
        @DisplayName("brandName은 trim된다")
        void brandNameIsTrimmed() {
            // when
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null, "  테스트브랜드  ", QueryContext.defaultOf(BrandSortKey.DISPLAY_ORDER));

            // then
            assertThat(criteria.brandName()).isEqualTo("테스트브랜드");
        }

        @Test
        @DisplayName("빈 문자열 brandName은 null로 변환된다")
        void blankBrandNameBecomesNull() {
            // when
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null, "   ", QueryContext.defaultOf(BrandSortKey.DISPLAY_ORDER));

            // then
            assertThat(criteria.brandName()).isNull();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("hasBrandNameFilter()는 브랜드명 필터가 있으면 true를 반환한다")
        void hasBrandNameFilterReturnsTrueWhenBrandNameExists() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            null, "테스트", QueryContext.defaultOf(BrandSortKey.DISPLAY_ORDER));

            // then
            assertThat(criteria.hasBrandNameFilter()).isTrue();
        }

        @Test
        @DisplayName("hasBrandNameFilter()는 브랜드명이 없으면 false를 반환한다")
        void hasBrandNameFilterReturnsFalseWhenNoBrandName() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // then
            assertThat(criteria.hasBrandNameFilter()).isFalse();
        }

        @Test
        @DisplayName("hasDisplayedFilter()는 표시 필터가 있으면 true를 반환한다")
        void hasDisplayedFilterReturnsTrueWhenDisplayedExists() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            // then
            assertThat(criteria.hasDisplayedFilter()).isTrue();
        }

        @Test
        @DisplayName("hasDisplayedFilter()는 표시 필터가 없으면 false를 반환한다")
        void hasDisplayedFilterReturnsFalseWhenNoDisplayed() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // then
            assertThat(criteria.hasDisplayedFilter()).isFalse();
        }

        @Test
        @DisplayName("size()는 페이지 크기를 반환한다")
        void returnsSize() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();

            // then
            assertThat(criteria.size()).isEqualTo(PageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("offset()은 오프셋을 반환한다")
        void returnsOffset() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.of(
                            BrandSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(2, 20));
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, queryContext);

            // then
            assertThat(criteria.offset()).isEqualTo(40);
        }

        @Test
        @DisplayName("page()는 현재 페이지를 반환한다")
        void returnsPage() {
            // given
            QueryContext<BrandSortKey> queryContext =
                    QueryContext.of(
                            BrandSortKey.CREATED_AT, SortDirection.DESC, PageRequest.of(3, 20));
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, queryContext);

            // then
            assertThat(criteria.page()).isEqualTo(3);
        }
    }
}
