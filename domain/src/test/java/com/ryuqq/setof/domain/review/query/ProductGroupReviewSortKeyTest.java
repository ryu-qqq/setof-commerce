package com.ryuqq.setof.domain.review.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupReviewSortKey 테스트")
class ProductGroupReviewSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(ProductGroupReviewSortKey.REVIEW_ID).isInstanceOf(SortKey.class);
        }
    }

    @Nested
    @DisplayName("fieldName() 테스트")
    class FieldNameTest {

        @Test
        @DisplayName("REVIEW_ID의 필드명은 reviewId이다")
        void reviewIdFieldName() {
            // then
            assertThat(ProductGroupReviewSortKey.REVIEW_ID.fieldName()).isEqualTo("reviewId");
        }

        @Test
        @DisplayName("RATING의 필드명은 rating이다")
        void ratingFieldName() {
            // then
            assertThat(ProductGroupReviewSortKey.RATING.fieldName()).isEqualTo("rating");
        }
    }

    @Nested
    @DisplayName("defaultKey() 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("기본 정렬 키는 REVIEW_ID이다")
        void defaultKeyIsReviewId() {
            // then
            assertThat(ProductGroupReviewSortKey.defaultKey())
                    .isEqualTo(ProductGroupReviewSortKey.REVIEW_ID);
        }
    }

    @Nested
    @DisplayName("fromOrderType() 테스트")
    class FromOrderTypeTest {

        @Test
        @DisplayName("HIGH_RATING 입력 시 RATING을 반환한다")
        void highRatingReturnsRating() {
            // then
            assertThat(ProductGroupReviewSortKey.fromOrderType("HIGH_RATING"))
                    .isEqualTo(ProductGroupReviewSortKey.RATING);
        }

        @Test
        @DisplayName("대소문자 무관하게 HIGH_RATING을 처리한다")
        void caseInsensitiveHighRating() {
            // then
            assertThat(ProductGroupReviewSortKey.fromOrderType("high_rating"))
                    .isEqualTo(ProductGroupReviewSortKey.RATING);
        }

        @Test
        @DisplayName("HIGH_RATING 이외의 값은 REVIEW_ID를 반환한다")
        void unknownOrderTypeReturnsReviewId() {
            // then
            assertThat(ProductGroupReviewSortKey.fromOrderType("LATEST"))
                    .isEqualTo(ProductGroupReviewSortKey.REVIEW_ID);
        }

        @Test
        @DisplayName("null 입력 시 REVIEW_ID를 반환한다")
        void nullOrderTypeReturnsReviewId() {
            // then
            assertThat(ProductGroupReviewSortKey.fromOrderType(null))
                    .isEqualTo(ProductGroupReviewSortKey.REVIEW_ID);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductGroupReviewSortKey.values())
                    .containsExactly(
                            ProductGroupReviewSortKey.REVIEW_ID, ProductGroupReviewSortKey.RATING);
        }
    }
}
