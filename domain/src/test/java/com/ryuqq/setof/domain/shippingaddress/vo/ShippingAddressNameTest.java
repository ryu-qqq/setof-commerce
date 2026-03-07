package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressName Value Object 단위 테스트")
class ShippingAddressNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 이름으로 ShippingAddressName을 생성한다")
        void createWithValidValue() {
            // when
            ShippingAddressName name = ShippingAddressName.of("집");

            // then
            assertThat(name.value()).isEqualTo("집");
        }

        @Test
        @DisplayName("앞뒤 공백이 제거된다")
        void trimsLeadingAndTrailingWhitespace() {
            // when
            ShippingAddressName name = ShippingAddressName.of("  회사  ");

            // then
            assertThat(name.value()).isEqualTo("회사");
        }

        @Test
        @DisplayName("null을 전달하면 예외가 발생한다")
        void nullThrowsException() {
            assertThatThrownBy(() -> ShippingAddressName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("배송지 이름은 필수입니다");
        }

        @Test
        @DisplayName("빈 문자열을 전달하면 예외가 발생한다")
        void blankThrowsException() {
            assertThatThrownBy(() -> ShippingAddressName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("배송지 이름은 필수입니다");
        }

        @Test
        @DisplayName("30자를 초과하는 이름은 예외가 발생한다")
        void tooLongValueThrowsException() {
            // given
            String tooLong = "가".repeat(31);

            // when & then
            assertThatThrownBy(() -> ShippingAddressName.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("30");
        }

        @Test
        @DisplayName("정확히 30자인 이름은 생성된다")
        void exactlyMaxLengthIsAllowed() {
            // given
            String exactly30 = "가".repeat(30);

            // when
            ShippingAddressName name = ShippingAddressName.of(exactly30);

            // then
            assertThat(name.value()).hasSize(30);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ShippingAddressName은 동등하다")
        void sameValueAreEqual() {
            // given
            ShippingAddressName name1 = ShippingAddressName.of("집");
            ShippingAddressName name2 = ShippingAddressName.of("집");

            // then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ShippingAddressName은 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ShippingAddressName name1 = ShippingAddressName.of("집");
            ShippingAddressName name2 = ShippingAddressName.of("회사");

            // then
            assertThat(name1).isNotEqualTo(name2);
        }
    }
}
