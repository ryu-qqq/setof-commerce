package com.ryuqq.setof.domain.review.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("AvailableReviewSortKey 테스트")
class AvailableReviewSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(AvailableReviewSortKey.ORDER_ID).isInstanceOf(SortKey.class);
        }
    }

    @Nested
    @DisplayName("fieldName() 테스트")
    class FieldNameTest {

        @Test
        @DisplayName("ORDER_ID의 필드명은 orderId이다")
        void orderIdFieldName() {
            // then
            assertThat(AvailableReviewSortKey.ORDER_ID.fieldName()).isEqualTo("orderId");
        }
    }

    @Nested
    @DisplayName("defaultKey() 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("기본 정렬 키는 ORDER_ID이다")
        void defaultKeyIsOrderId() {
            // then
            assertThat(AvailableReviewSortKey.defaultKey())
                    .isEqualTo(AvailableReviewSortKey.ORDER_ID);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(AvailableReviewSortKey.values())
                    .containsExactly(AvailableReviewSortKey.ORDER_ID);
        }
    }
}
