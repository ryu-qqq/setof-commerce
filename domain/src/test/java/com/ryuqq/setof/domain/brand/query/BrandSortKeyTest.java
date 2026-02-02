package com.ryuqq.setof.domain.brand.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandSortKey 테스트")
class BrandSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(BrandSortKey.CREATED_AT).isInstanceOf(SortKey.class);
        }

        @Test
        @DisplayName("CREATED_AT의 fieldName은 'createdAt'이다")
        void createdAtFieldName() {
            assertThat(BrandSortKey.CREATED_AT.fieldName()).isEqualTo("createdAt");
        }

        @Test
        @DisplayName("DISPLAY_ORDER의 fieldName은 'displayOrder'이다")
        void displayOrderFieldName() {
            assertThat(BrandSortKey.DISPLAY_ORDER.fieldName()).isEqualTo("displayOrder");
        }

        @Test
        @DisplayName("BRAND_NAME의 fieldName은 'brandName'이다")
        void brandNameFieldName() {
            assertThat(BrandSortKey.BRAND_NAME.fieldName()).isEqualTo("brandName");
        }
    }

    @Nested
    @DisplayName("defaultKey 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("defaultKey()는 DISPLAY_ORDER를 반환한다")
        void defaultKeyReturnsDisplayOrder() {
            // when
            BrandSortKey defaultKey = BrandSortKey.defaultKey();

            // then
            assertThat(defaultKey).isEqualTo(BrandSortKey.DISPLAY_ORDER);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(BrandSortKey.values())
                    .containsExactly(
                            BrandSortKey.CREATED_AT,
                            BrandSortKey.DISPLAY_ORDER,
                            BrandSortKey.BRAND_NAME);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            // then
            assertThat(BrandSortKey.valueOf("CREATED_AT")).isEqualTo(BrandSortKey.CREATED_AT);
            assertThat(BrandSortKey.valueOf("DISPLAY_ORDER")).isEqualTo(BrandSortKey.DISPLAY_ORDER);
            assertThat(BrandSortKey.valueOf("BRAND_NAME")).isEqualTo(BrandSortKey.BRAND_NAME);
        }
    }
}
