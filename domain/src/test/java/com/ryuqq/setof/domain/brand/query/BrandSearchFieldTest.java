package com.ryuqq.setof.domain.brand.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SearchField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("BrandSearchField 테스트")
class BrandSearchFieldTest {

    @Nested
    @DisplayName("SearchField 인터페이스 구현 테스트")
    class SearchFieldInterfaceTest {

        @Test
        @DisplayName("SearchField 인터페이스를 구현한다")
        void implementsSearchField() {
            assertThat(BrandSearchField.BRAND_NAME).isInstanceOf(SearchField.class);
        }
    }

    @Nested
    @DisplayName("fieldName() 테스트")
    class FieldNameTest {

        @Test
        @DisplayName("BRAND_NAME의 fieldName은 brandName이다")
        void brandNameFieldName() {
            assertThat(BrandSearchField.BRAND_NAME.fieldName()).isEqualTo("brandName");
        }

        @Test
        @DisplayName("DISPLAY_KOREAN_NAME의 fieldName은 displayKoreanName이다")
        void displayKoreanNameFieldName() {
            assertThat(BrandSearchField.DISPLAY_KOREAN_NAME.fieldName())
                    .isEqualTo("displayKoreanName");
        }

        @Test
        @DisplayName("DISPLAY_ENGLISH_NAME의 fieldName은 displayEnglishName이다")
        void displayEnglishNameFieldName() {
            assertThat(BrandSearchField.DISPLAY_ENGLISH_NAME.fieldName())
                    .isEqualTo("displayEnglishName");
        }
    }

    @Nested
    @DisplayName("fromString() 테스트")
    class FromStringTest {

        @Test
        @DisplayName("필드명으로 BrandSearchField를 찾는다")
        void findByFieldName() {
            assertThat(BrandSearchField.fromString("brandName"))
                    .isEqualTo(BrandSearchField.BRAND_NAME);
            assertThat(BrandSearchField.fromString("displayKoreanName"))
                    .isEqualTo(BrandSearchField.DISPLAY_KOREAN_NAME);
            assertThat(BrandSearchField.fromString("displayEnglishName"))
                    .isEqualTo(BrandSearchField.DISPLAY_ENGLISH_NAME);
        }

        @Test
        @DisplayName("enum 이름으로 BrandSearchField를 찾는다")
        void findByEnumName() {
            assertThat(BrandSearchField.fromString("BRAND_NAME"))
                    .isEqualTo(BrandSearchField.BRAND_NAME);
            assertThat(BrandSearchField.fromString("DISPLAY_KOREAN_NAME"))
                    .isEqualTo(BrandSearchField.DISPLAY_KOREAN_NAME);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("null이나 공백이면 null을 반환한다")
        void nullOrBlankReturnsNull(String value) {
            assertThat(BrandSearchField.fromString(value)).isNull();
        }

        @Test
        @DisplayName("알 수 없는 필드명이면 null을 반환한다")
        void unknownFieldReturnsNull() {
            assertThat(BrandSearchField.fromString("unknownField")).isNull();
        }
    }
}
