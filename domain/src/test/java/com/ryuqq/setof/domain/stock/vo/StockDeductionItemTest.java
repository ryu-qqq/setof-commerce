package com.ryuqq.setof.domain.stock.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("StockDeductionItem Value Object 단위 테스트")
class StockDeductionItemTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("양수의 productId와 quantity로 생성한다")
        void createWithValidValues() {
            // when
            StockDeductionItem item = new StockDeductionItem(10L, 3);

            // then
            assertThat(item.productId()).isEqualTo(10L);
            assertThat(item.quantity()).isEqualTo(3);
        }

        @Test
        @DisplayName("productId가 0이면 예외가 발생한다")
        void createWithZeroProductId_ThrowsException() {
            assertThatThrownBy(() -> new StockDeductionItem(0L, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productId must be positive");
        }

        @Test
        @DisplayName("productId가 음수이면 예외가 발생한다")
        void createWithNegativeProductId_ThrowsException() {
            assertThatThrownBy(() -> new StockDeductionItem(-1L, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productId must be positive");
        }

        @Test
        @DisplayName("quantity가 0이면 예외가 발생한다")
        void createWithZeroQuantity_ThrowsException() {
            assertThatThrownBy(() -> new StockDeductionItem(1L, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("quantity must be positive");
        }

        @Test
        @DisplayName("quantity가 음수이면 예외가 발생한다")
        void createWithNegativeQuantity_ThrowsException() {
            assertThatThrownBy(() -> new StockDeductionItem(1L, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("quantity must be positive");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 productId와 quantity는 동일하다")
        void sameValuesAreEqual() {
            // given
            StockDeductionItem item1 = new StockDeductionItem(10L, 3);
            StockDeductionItem item2 = new StockDeductionItem(10L, 3);

            // then
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        }

        @Test
        @DisplayName("다른 productId는 동일하지 않다")
        void differentProductIdNotEqual() {
            // given
            StockDeductionItem item1 = new StockDeductionItem(10L, 3);
            StockDeductionItem item2 = new StockDeductionItem(20L, 3);

            // then
            assertThat(item1).isNotEqualTo(item2);
        }

        @Test
        @DisplayName("다른 quantity는 동일하지 않다")
        void differentQuantityNotEqual() {
            // given
            StockDeductionItem item1 = new StockDeductionItem(10L, 3);
            StockDeductionItem item2 = new StockDeductionItem(10L, 5);

            // then
            assertThat(item1).isNotEqualTo(item2);
        }
    }
}
