package com.ryuqq.setof.domain.review.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MyReviewSortKey 테스트")
class MyReviewSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(MyReviewSortKey.REVIEW_ID).isInstanceOf(SortKey.class);
        }
    }

    @Nested
    @DisplayName("fieldName() 테스트")
    class FieldNameTest {

        @Test
        @DisplayName("REVIEW_ID의 필드명은 reviewId이다")
        void reviewIdFieldName() {
            // then
            assertThat(MyReviewSortKey.REVIEW_ID.fieldName()).isEqualTo("reviewId");
        }
    }

    @Nested
    @DisplayName("defaultKey() 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("기본 정렬 키는 REVIEW_ID이다")
        void defaultKeyIsReviewId() {
            // then
            assertThat(MyReviewSortKey.defaultKey()).isEqualTo(MyReviewSortKey.REVIEW_ID);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(MyReviewSortKey.values()).containsExactly(MyReviewSortKey.REVIEW_ID);
        }
    }
}
