package com.ryuqq.setof.domain.productgroup.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SearchField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupSearchField enum 테스트")
class ProductGroupSearchFieldTest {

    @Nested
    @DisplayName("SearchField 인터페이스 구현 테스트")
    class SearchFieldInterfaceTest {

        @Test
        @DisplayName("SearchField 인터페이스를 구현한다")
        void implementsSearchField() {
            assertThat(ProductGroupSearchField.PRODUCT_GROUP_NAME).isInstanceOf(SearchField.class);
        }
    }

    @Nested
    @DisplayName("PRODUCT_GROUP_NAME 테스트")
    class ProductGroupNameTest {

        @Test
        @DisplayName("fieldName은 'productGroupName'이다")
        void fieldNameIsProductGroupName() {
            assertThat(ProductGroupSearchField.PRODUCT_GROUP_NAME.fieldName())
                    .isEqualTo("productGroupName");
        }
    }

    @Nested
    @DisplayName("fromString() - 문자열 변환 테스트")
    class FromStringTest {

        @Test
        @DisplayName("fieldName 문자열로 변환한다")
        void fromFieldName() {
            assertThat(ProductGroupSearchField.fromString("productGroupName"))
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("enum 이름 문자열로 변환한다")
        void fromEnumName() {
            assertThat(ProductGroupSearchField.fromString("PRODUCT_GROUP_NAME"))
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("fieldName은 대소문자 무관 변환된다")
        void fromStringCaseInsensitiveForFieldName() {
            // fieldName "productGroupName"은 대소문자 무관 매칭
            assertThat(ProductGroupSearchField.fromString("ProductGroupName"))
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
            assertThat(ProductGroupSearchField.fromString("PRODUCTGROUPNAME"))
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("enum name은 대소문자 무관 변환된다")
        void fromStringCaseInsensitiveForEnumName() {
            // enum name "PRODUCT_GROUP_NAME"은 대소문자 무관 매칭
            assertThat(ProductGroupSearchField.fromString("product_group_name"))
                    .isEqualTo(ProductGroupSearchField.PRODUCT_GROUP_NAME);
        }

        @Test
        @DisplayName("null이면 null을 반환한다")
        void returnsNullForNull() {
            assertThat(ProductGroupSearchField.fromString(null)).isNull();
        }

        @Test
        @DisplayName("빈 문자열이면 null을 반환한다")
        void returnsNullForBlank() {
            assertThat(ProductGroupSearchField.fromString("")).isNull();
            assertThat(ProductGroupSearchField.fromString("   ")).isNull();
        }

        @Test
        @DisplayName("알 수 없는 문자열이면 null을 반환한다")
        void returnsNullForUnknownString() {
            assertThat(ProductGroupSearchField.fromString("unknown")).isNull();
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 검색 필드 값이 존재한다")
        void allValuesExist() {
            assertThat(ProductGroupSearchField.values())
                    .containsExactly(ProductGroupSearchField.PRODUCT_GROUP_NAME);
        }
    }
}
