package com.ryuqq.setof.application.review.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.review.ReviewQueryFixtures;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.AvailableReviewSortKey;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.MyReviewSortKey;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReviewQueryFactory 단위 테스트")
class ReviewQueryFactoryTest {

    private final CommonVoFactory commonVoFactory = new CommonVoFactory();
    private final ReviewQueryFactory sut = new ReviewQueryFactory(commonVoFactory);

    @Nested
    @DisplayName("createAvailableReviewCriteria() - AvailableReviewSearchParams → Criteria 변환")
    class CreateAvailableReviewCriteriaTest {

        @Test
        @DisplayName("legacyUserId가 있는 파라미터를 AvailableReviewSearchCriteria로 변환한다")
        void createAvailableReviewCriteria_WithLegacyUserId_ReturnsCriteria() {
            // given
            AvailableReviewSearchParams params = ReviewQueryFixtures.availableReviewSearchParams();

            // when
            AvailableReviewSearchCriteria result = sut.createAvailableReviewCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.legacyMemberIdValue()).isEqualTo(params.legacyUserId());
            assertThat(result.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("기본 정렬 키가 설정된다")
        void createAvailableReviewCriteria_DefaultSortKey_IsSet() {
            // given
            AvailableReviewSearchParams params = ReviewQueryFixtures.availableReviewSearchParams();

            // when
            AvailableReviewSearchCriteria result = sut.createAvailableReviewCriteria(params);

            // then
            assertThat(result.queryContext().sortKey())
                    .isEqualTo(AvailableReviewSortKey.defaultKey());
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("첫 페이지 요청 시 cursor가 null이다")
        void createAvailableReviewCriteria_FirstPage_HasNullCursor() {
            // given
            AvailableReviewSearchParams params = ReviewQueryFixtures.availableReviewSearchParams();

            // when
            AvailableReviewSearchCriteria result = sut.createAvailableReviewCriteria(params);

            // then
            assertThat(result.cursor()).isNull();
            assertThat(result.hasCursor()).isFalse();
        }

        @Test
        @DisplayName("lastOrderId가 있으면 cursor에 반영된다")
        void createAvailableReviewCriteria_WithLastOrderId_SetsCursor() {
            // given
            AvailableReviewSearchParams params =
                    ReviewQueryFixtures.availableReviewSearchParams(100L, 700L);

            // when
            AvailableReviewSearchCriteria result = sut.createAvailableReviewCriteria(params);

            // then
            assertThat(result.cursor()).isEqualTo(700L);
            assertThat(result.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("size가 criteria의 size에 반영된다")
        void createAvailableReviewCriteria_WithSize_SetsSizeCorrectly() {
            // given
            int customSize = 10;
            AvailableReviewSearchParams params =
                    ReviewQueryFixtures.availableReviewSearchParams(customSize);

            // when
            AvailableReviewSearchCriteria result = sut.createAvailableReviewCriteria(params);

            // then
            assertThat(result.size()).isEqualTo(customSize);
        }
    }

    @Nested
    @DisplayName("createMyReviewCriteria() - MyReviewSearchParams → Criteria 변환")
    class CreateMyReviewCriteriaTest {

        @Test
        @DisplayName("legacyUserId가 있는 파라미터를 MyReviewSearchCriteria로 변환한다")
        void createMyReviewCriteria_WithLegacyUserId_ReturnsCriteria() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams();

            // when
            MyReviewSearchCriteria result = sut.createMyReviewCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.legacyMemberIdValue()).isEqualTo(params.legacyUserId());
        }

        @Test
        @DisplayName("기본 정렬 키가 설정된다")
        void createMyReviewCriteria_DefaultSortKey_IsSet() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams();

            // when
            MyReviewSearchCriteria result = sut.createMyReviewCriteria(params);

            // then
            assertThat(result.queryContext().sortKey()).isEqualTo(MyReviewSortKey.defaultKey());
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("첫 페이지 요청 시 cursor가 null이다")
        void createMyReviewCriteria_FirstPage_HasNullCursor() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams();

            // when
            MyReviewSearchCriteria result = sut.createMyReviewCriteria(params);

            // then
            assertThat(result.cursor()).isNull();
            assertThat(result.hasCursor()).isFalse();
        }

        @Test
        @DisplayName("lastReviewId가 있으면 cursor에 반영된다")
        void createMyReviewCriteria_WithLastReviewId_SetsCursor() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams(100L, 50L);

            // when
            MyReviewSearchCriteria result = sut.createMyReviewCriteria(params);

            // then
            assertThat(result.cursor()).isEqualTo(50L);
            assertThat(result.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("size가 criteria의 size에 반영된다")
        void createMyReviewCriteria_WithSize_SetsSizeCorrectly() {
            // given
            int customSize = 15;
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams(customSize);

            // when
            MyReviewSearchCriteria result = sut.createMyReviewCriteria(params);

            // then
            assertThat(result.size()).isEqualTo(customSize);
        }
    }

    @Nested
    @DisplayName(
            "createProductGroupReviewCriteria() - ProductGroupReviewSearchParams → Criteria 변환")
    class CreateProductGroupReviewCriteriaTest {

        @Test
        @DisplayName("파라미터를 ProductGroupReviewSearchCriteria로 변환한다")
        void createProductGroupReviewCriteria_ValidParams_ReturnsCriteria() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams();

            // when
            ProductGroupReviewSearchCriteria result = sut.createProductGroupReviewCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId()).isEqualTo(params.productGroupId());
        }

        @Test
        @DisplayName("정렬 타입 RECENT은 적절한 SortKey로 변환된다")
        void createProductGroupReviewCriteria_RecentOrderType_UsesCorrectSortKey() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams(500L, "RECENT");

            // when
            ProductGroupReviewSearchCriteria result = sut.createProductGroupReviewCriteria(params);

            // then
            assertThat(result.sortKey())
                    .isEqualTo(ProductGroupReviewSortKey.fromOrderType("RECENT"));
        }

        @Test
        @DisplayName("정렬 타입 HIGH_RATING은 적절한 SortKey로 변환된다")
        void createProductGroupReviewCriteria_HighRatingOrderType_UsesCorrectSortKey() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams(500L, "HIGH_RATING");

            // when
            ProductGroupReviewSearchCriteria result = sut.createProductGroupReviewCriteria(params);

            // then
            assertThat(result.sortKey())
                    .isEqualTo(ProductGroupReviewSortKey.fromOrderType("HIGH_RATING"));
        }

        @Test
        @DisplayName("size가 criteria의 size에 반영된다")
        void createProductGroupReviewCriteria_WithSize_SetsSizeCorrectly() {
            // given
            int customSize = 10;
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams(0, customSize);

            // when
            ProductGroupReviewSearchCriteria result = sut.createProductGroupReviewCriteria(params);

            // then
            assertThat(result.size()).isEqualTo(customSize);
        }

        @Test
        @DisplayName("정렬 방향이 DESC로 설정된다")
        void createProductGroupReviewCriteria_SortDirection_IsDescending() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams();

            // when
            ProductGroupReviewSearchCriteria result = sut.createProductGroupReviewCriteria(params);

            // then
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.DESC);
        }
    }
}
