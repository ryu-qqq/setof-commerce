package com.ryuqq.setof.domain.faq.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("FaqSortKey 테스트")
class FaqSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            assertThat(FaqSortKey.DISPLAY_ORDER).isInstanceOf(SortKey.class);
        }

        @Test
        @DisplayName("DISPLAY_ORDER의 fieldName은 'displayOrder'이다")
        void displayOrderFieldName() {
            assertThat(FaqSortKey.DISPLAY_ORDER.fieldName()).isEqualTo("displayOrder");
        }

        @Test
        @DisplayName("TOP_DISPLAY_ORDER의 fieldName은 'topDisplayOrder'이다")
        void topDisplayOrderFieldName() {
            assertThat(FaqSortKey.TOP_DISPLAY_ORDER.fieldName()).isEqualTo("topDisplayOrder");
        }
    }

    @Nested
    @DisplayName("defaultKey() 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("defaultKey()는 DISPLAY_ORDER를 반환한다")
        void defaultKeyReturnsDisplayOrder() {
            // when
            FaqSortKey defaultKey = FaqSortKey.defaultKey();

            // then
            assertThat(defaultKey).isEqualTo(FaqSortKey.DISPLAY_ORDER);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            assertThat(FaqSortKey.values())
                    .containsExactly(FaqSortKey.DISPLAY_ORDER, FaqSortKey.TOP_DISPLAY_ORDER);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            assertThat(FaqSortKey.valueOf("DISPLAY_ORDER")).isEqualTo(FaqSortKey.DISPLAY_ORDER);
            assertThat(FaqSortKey.valueOf("TOP_DISPLAY_ORDER"))
                    .isEqualTo(FaqSortKey.TOP_DISPLAY_ORDER);
        }
    }
}
