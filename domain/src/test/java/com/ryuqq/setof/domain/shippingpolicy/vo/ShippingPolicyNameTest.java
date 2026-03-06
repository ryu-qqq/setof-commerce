package com.ryuqq.setof.domain.shippingpolicy.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingPolicyName Value Object 테스트")
class ShippingPolicyNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 정책명으로 ShippingPolicyName을 생성한다")
        void createWithValidName() {
            // when
            ShippingPolicyName name = ShippingPolicyName.of("기본 배송 정책");

            // then
            assertThat(name.value()).isEqualTo("기본 배송 정책");
        }

        @Test
        @DisplayName("1자 정책명으로 생성한다")
        void createWithMinLengthName() {
            // when
            ShippingPolicyName name = ShippingPolicyName.of("A");

            // then
            assertThat(name.value()).isEqualTo("A");
        }

        @Test
        @DisplayName("100자 정책명으로 생성한다")
        void createWithMaxLengthName() {
            // given
            String maxLengthName = "A".repeat(100);

            // when
            ShippingPolicyName name = ShippingPolicyName.of(maxLengthName);

            // then
            assertThat(name.value()).isEqualTo(maxLengthName);
        }

        @Test
        @DisplayName("앞뒤 공백이 있는 정책명은 trim되어 저장된다")
        void createWithWhitespaceTrimmed() {
            // when
            ShippingPolicyName name = ShippingPolicyName.of("  기본 배송 정책  ");

            // then
            assertThat(name.value()).isEqualTo("기본 배송 정책");
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("null 값으로 생성하면 예외가 발생한다")
        void createWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> ShippingPolicyName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("배송 정책명은 필수입니다");
        }

        @Test
        @DisplayName("빈 문자열로 생성하면 예외가 발생한다")
        void createWithEmptyStringThrowsException() {
            // when & then
            assertThatThrownBy(() -> ShippingPolicyName.of(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("배송 정책명은 필수입니다");
        }

        @Test
        @DisplayName("공백 문자열로 생성하면 예외가 발생한다")
        void createWithBlankStringThrowsException() {
            // when & then
            assertThatThrownBy(() -> ShippingPolicyName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("배송 정책명은 필수입니다");
        }

        @Test
        @DisplayName("101자 이상의 정책명은 예외가 발생한다")
        void createWithTooLongNameThrowsException() {
            // given
            String tooLongName = "A".repeat(101);

            // when & then
            assertThatThrownBy(() -> ShippingPolicyName.of(tooLongName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1~100자");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 ShippingPolicyName은 동등하다")
        void sameValueIsEqual() {
            // given
            ShippingPolicyName name1 = ShippingPolicyName.of("기본 배송 정책");
            ShippingPolicyName name2 = ShippingPolicyName.of("기본 배송 정책");

            // then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 ShippingPolicyName은 동등하지 않다")
        void differentValueIsNotEqual() {
            // given
            ShippingPolicyName name1 = ShippingPolicyName.of("기본 배송 정책");
            ShippingPolicyName name2 = ShippingPolicyName.of("특별 배송 정책");

            // then
            assertThat(name1).isNotEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("생성 후 value는 변경되지 않는다")
        void valueIsImmutable() {
            // given
            ShippingPolicyName name = ShippingPolicyName.of("기본 배송 정책");
            String originalValue = name.value();

            // then
            assertThat(name.value()).isEqualTo(originalValue);
        }
    }
}
