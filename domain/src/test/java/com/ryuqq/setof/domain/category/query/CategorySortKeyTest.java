package com.ryuqq.setof.domain.category.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CategorySortKey 테스트")
class CategorySortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(CategorySortKey.CREATED_AT).isInstanceOf(SortKey.class);
        }

        @Test
        @DisplayName("CREATED_AT의 fieldName은 'createdAt'이다")
        void createdAtFieldName() {
            assertThat(CategorySortKey.CREATED_AT.fieldName()).isEqualTo("createdAt");
        }

        @Test
        @DisplayName("CATEGORY_NAME의 fieldName은 'categoryName'이다")
        void categoryNameFieldName() {
            assertThat(CategorySortKey.CATEGORY_NAME.fieldName()).isEqualTo("categoryName");
        }

        @Test
        @DisplayName("CATEGORY_DEPTH의 fieldName은 'categoryDepth'이다")
        void categoryDepthFieldName() {
            assertThat(CategorySortKey.CATEGORY_DEPTH.fieldName()).isEqualTo("categoryDepth");
        }

        @Test
        @DisplayName("DISPLAY_NAME의 fieldName은 'displayName'이다")
        void displayNameFieldName() {
            assertThat(CategorySortKey.DISPLAY_NAME.fieldName()).isEqualTo("displayName");
        }
    }

    @Nested
    @DisplayName("defaultKey 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("defaultKey()는 CREATED_AT을 반환한다")
        void defaultKeyReturnsCreatedAt() {
            // when
            CategorySortKey defaultKey = CategorySortKey.defaultKey();

            // then
            assertThat(defaultKey).isEqualTo(CategorySortKey.CREATED_AT);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(CategorySortKey.values())
                    .containsExactly(
                            CategorySortKey.CREATED_AT,
                            CategorySortKey.CATEGORY_NAME,
                            CategorySortKey.CATEGORY_DEPTH,
                            CategorySortKey.DISPLAY_NAME);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            // then
            assertThat(CategorySortKey.valueOf("CREATED_AT")).isEqualTo(CategorySortKey.CREATED_AT);
            assertThat(CategorySortKey.valueOf("CATEGORY_NAME"))
                    .isEqualTo(CategorySortKey.CATEGORY_NAME);
            assertThat(CategorySortKey.valueOf("CATEGORY_DEPTH"))
                    .isEqualTo(CategorySortKey.CATEGORY_DEPTH);
            assertThat(CategorySortKey.valueOf("DISPLAY_NAME"))
                    .isEqualTo(CategorySortKey.DISPLAY_NAME);
        }
    }
}
