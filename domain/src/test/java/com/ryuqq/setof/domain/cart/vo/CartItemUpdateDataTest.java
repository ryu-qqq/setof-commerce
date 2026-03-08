package com.ryuqq.setof.domain.cart.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.cart.CartFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartItemUpdateData Value Object 단위 테스트")
class CartItemUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성")
    class CreationTest {

        @Test
        @DisplayName("수량과 시간으로 CartItemUpdateData를 생성한다")
        void createWithQuantityAndOccurredAt() {
            // given
            CartQuantity quantity = CartFixtures.quantity(5);
            Instant occurredAt = CommonVoFixtures.now();

            // when
            CartItemUpdateData updateData = CartItemUpdateData.of(quantity, occurredAt);

            // then
            assertThat(updateData.quantity()).isEqualTo(quantity);
            assertThat(updateData.occurredAt()).isEqualTo(occurredAt);
        }

        @Test
        @DisplayName("기본 UpdateData 생성")
        void createDefaultUpdateData() {
            // when
            CartItemUpdateData updateData = CartFixtures.defaultUpdateData();

            // then
            assertThat(updateData.quantity()).isNotNull();
            assertThat(updateData.quantity().value()).isEqualTo(5);
            assertThat(updateData.occurredAt()).isNotNull();
        }

        @Test
        @DisplayName("다양한 수량으로 UpdateData를 생성한다")
        void createUpdateDataWithVariousQuantities() {
            // when
            CartItemUpdateData updateData = CartFixtures.updateData(10);

            // then
            assertThat(updateData.quantity().value()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("동등성(equals/hashCode) 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 수량과 시간이면 동등하다")
        void sameValuesAreEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            CartQuantity quantity = CartQuantity.of(5);

            CartItemUpdateData data1 = CartItemUpdateData.of(quantity, now);
            CartItemUpdateData data2 = CartItemUpdateData.of(quantity, now);

            // then
            assertThat(data1).isEqualTo(data2);
            assertThat(data1.hashCode()).isEqualTo(data2.hashCode());
        }

        @Test
        @DisplayName("수량이 다르면 동등하지 않다")
        void differentQuantitiesAreNotEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            CartItemUpdateData data1 = CartItemUpdateData.of(CartQuantity.of(3), now);
            CartItemUpdateData data2 = CartItemUpdateData.of(CartQuantity.of(5), now);

            // then
            assertThat(data1).isNotEqualTo(data2);
        }
    }
}
