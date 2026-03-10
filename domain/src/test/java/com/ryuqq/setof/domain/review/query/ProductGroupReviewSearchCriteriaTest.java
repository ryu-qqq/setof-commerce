package com.ryuqq.setof.domain.review.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupReviewSearchCriteria 테스트")
class ProductGroupReviewSearchCriteriaTest {

    @Nested
    @DisplayName("of() - 정적 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("productGroupId와 queryContext로 검색 조건을 생성한다")
        void createWithProductGroupIdAndQueryContext() {
            // given
            long productGroupId = 500L;
            QueryContext<ProductGroupReviewSortKey> queryContext =
                    QueryContext.defaultOf(ProductGroupReviewSortKey.defaultKey());

            // when
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(productGroupId, queryContext);

            // then
            assertThat(criteria.productGroupId()).isEqualTo(500L);
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("queryContext가 null이면 기본 queryContext가 사용된다")
        void nullQueryContextUsesDefault() {
            // when
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(500L, null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodsTest {

        @Test
        @DisplayName("size()는 queryContext의 size를 반환한다")
        void sizeReturnsQueryContextSize() {
            // given
            QueryContext<ProductGroupReviewSortKey> queryContext =
                    QueryContext.of(
                            ProductGroupReviewSortKey.REVIEW_ID,
                            SortDirection.DESC,
                            PageRequest.of(0, 20));
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(500L, queryContext);

            // then
            assertThat(criteria.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("offset()은 queryContext의 offset을 반환한다")
        void offsetReturnsQueryContextOffset() {
            // given
            QueryContext<ProductGroupReviewSortKey> queryContext =
                    QueryContext.of(
                            ProductGroupReviewSortKey.REVIEW_ID,
                            SortDirection.DESC,
                            PageRequest.of(2, 10));
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(500L, queryContext);

            // then
            assertThat(criteria.offset()).isEqualTo(20L);
        }

        @Test
        @DisplayName("page()는 queryContext의 page를 반환한다")
        void pageReturnsQueryContextPage() {
            // given
            QueryContext<ProductGroupReviewSortKey> queryContext =
                    QueryContext.of(
                            ProductGroupReviewSortKey.REVIEW_ID,
                            SortDirection.DESC,
                            PageRequest.of(3, 10));
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(500L, queryContext);

            // then
            assertThat(criteria.page()).isEqualTo(3);
        }

        @Test
        @DisplayName("sortKey()는 queryContext의 정렬 키를 반환한다")
        void sortKeyReturnsQueryContextSortKey() {
            // given
            QueryContext<ProductGroupReviewSortKey> queryContext =
                    QueryContext.of(
                            ProductGroupReviewSortKey.RATING,
                            SortDirection.DESC,
                            PageRequest.of(0, 10));
            ProductGroupReviewSearchCriteria criteria =
                    ProductGroupReviewSearchCriteria.of(500L, queryContext);

            // then
            assertThat(criteria.sortKey()).isEqualTo(ProductGroupReviewSortKey.RATING);
        }
    }
}
