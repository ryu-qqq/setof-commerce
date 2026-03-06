package com.ryuqq.setof.domain.product.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SkuCode Value Object 테스트")
class SkuCodeTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 문자열로 SkuCode를 생성한다")
        void createWithValidValue() {
            // when
            SkuCode skuCode = SkuCode.of("SKU-001");

            // then
            assertThat(skuCode.value()).isEqualTo("SKU-001");
        }

        @Test
        @DisplayName("null 값으로 SkuCode를 생성할 수 있다 (nullable 허용)")
        void createWithNullValue() {
            // when
            SkuCode skuCode = SkuCode.of(null);

            // then
            assertThat(skuCode.value()).isNull();
        }

        @Test
        @DisplayName("빈 문자열로 SkuCode를 생성할 수 있다")
        void createWithEmptyString() {
            // when
            SkuCode skuCode = SkuCode.of("");

            // then
            assertThat(skuCode.value()).isEmpty();
        }

        @Test
        @DisplayName("100자 SKU 코드를 생성할 수 있다")
        void createWithMaxLengthValue() {
            // given
            String maxLengthValue = "A".repeat(100);

            // when
            SkuCode skuCode = SkuCode.of(maxLengthValue);

            // then
            assertThat(skuCode.value()).hasSize(100);
        }

        @Test
        @DisplayName("101자 SKU 코드로 생성하면 IllegalArgumentException이 발생한다")
        void throwExceptionWhenValueExceedsMaxLength() {
            // given
            String tooLongValue = "A".repeat(101);

            // when & then
            assertThatThrownBy(() -> SkuCode.of(tooLongValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("SKU 코드는 100자를 초과할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("isEmpty() - 비어있음 여부 확인")
    class IsEmptyTest {

        @Test
        @DisplayName("null 값이면 isEmpty()가 true이다")
        void isEmptyReturnsTrueForNull() {
            // when
            SkuCode skuCode = SkuCode.of(null);

            // then
            assertThat(skuCode.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("빈 문자열이면 isEmpty()가 true이다")
        void isEmptyReturnsTrueForEmptyString() {
            // when
            SkuCode skuCode = SkuCode.of("");

            // then
            assertThat(skuCode.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("공백 문자열이면 isEmpty()가 true이다")
        void isEmptyReturnsTrueForBlankString() {
            // when
            SkuCode skuCode = SkuCode.of("   ");

            // then
            assertThat(skuCode.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("유효한 값이면 isEmpty()가 false이다")
        void isEmptyReturnsFalseForValidValue() {
            // when
            SkuCode skuCode = SkuCode.of("SKU-001");

            // then
            assertThat(skuCode.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 SkuCode는 동일하다")
        void equalWithSameValue() {
            // given
            SkuCode sku1 = SkuCode.of("SKU-001");
            SkuCode sku2 = SkuCode.of("SKU-001");

            // then
            assertThat(sku1).isEqualTo(sku2);
            assertThat(sku1.hashCode()).isEqualTo(sku2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 SkuCode는 동일하지 않다")
        void notEqualWithDifferentValue() {
            // given
            SkuCode sku1 = SkuCode.of("SKU-001");
            SkuCode sku2 = SkuCode.of("SKU-002");

            // then
            assertThat(sku1).isNotEqualTo(sku2);
        }

        @Test
        @DisplayName("null 값을 가진 두 SkuCode는 동일하다")
        void nullSkuCodesAreEqual() {
            // given
            SkuCode sku1 = SkuCode.of(null);
            SkuCode sku2 = SkuCode.of(null);

            // then
            assertThat(sku1).isEqualTo(sku2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("SkuCode는 record 타입으로 불변이다")
        void skuCodeIsImmutable() {
            // given
            SkuCode skuCode = SkuCode.of("SKU-001");

            // then
            assertThat(skuCode.value()).isEqualTo("SKU-001");
        }
    }
}
