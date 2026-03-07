package com.ryuqq.setof.domain.shippingaddress.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddressId Value Object 단위 테스트")
class ShippingAddressIdTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("of()로 ShippingAddressId를 생성한다")
        void createWithOf() {
            // when
            ShippingAddressId id = ShippingAddressId.of(123L);

            // then
            assertThat(id.value()).isEqualTo(123L);
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> ShippingAddressId.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("forNew() - 신규 ID 생성")
    class ForNewTest {

        @Test
        @DisplayName("forNew()로 신규 배송지 ID를 생성한다")
        void createWithForNew() {
            // when
            ShippingAddressId id = ShippingAddressId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("value가 null이면 isNew()는 true를 반환한다")
        void isNewReturnsTrueWhenNull() {
            // given
            ShippingAddressId id = ShippingAddressId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("value가 있으면 isNew()는 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            ShippingAddressId id = ShippingAddressId.of(1L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ShippingAddressId는 동등하다")
        void sameValueEquals() {
            // given
            ShippingAddressId id1 = ShippingAddressId.of(100L);
            ShippingAddressId id2 = ShippingAddressId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ShippingAddressId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ShippingAddressId id1 = ShippingAddressId.of(100L);
            ShippingAddressId id2 = ShippingAddressId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        @DisplayName("forNew()로 생성한 두 ID는 동등하다")
        void twoForNewIdsAreEqual() {
            // given
            ShippingAddressId id1 = ShippingAddressId.forNew();
            ShippingAddressId id2 = ShippingAddressId.forNew();

            // then
            assertThat(id1).isEqualTo(id2);
        }
    }
}
