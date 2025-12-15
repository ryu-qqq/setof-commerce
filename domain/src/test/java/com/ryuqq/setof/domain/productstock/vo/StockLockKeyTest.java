package com.ryuqq.setof.domain.productstock.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StockLockKey 테스트")
class StockLockKeyTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTests {

        @Test
        @DisplayName("유효한 productId로 생성")
        void shouldCreateWithValidProductId() {
            // given
            Long productId = 100L;

            // when
            StockLockKey lockKey = new StockLockKey(productId);

            // then
            assertThat(lockKey.productId()).isEqualTo(productId);
        }

        @Test
        @DisplayName("null productId로 생성 시 예외 발생")
        void shouldThrowExceptionWhenProductIdIsNull() {
            // when & then
            assertThatThrownBy(() -> new StockLockKey(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productId must be positive");
        }

        @Test
        @DisplayName("0 이하 productId로 생성 시 예외 발생")
        void shouldThrowExceptionWhenProductIdIsZeroOrNegative() {
            // when & then
            assertThatThrownBy(() -> new StockLockKey(0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productId must be positive");

            assertThatThrownBy(() -> new StockLockKey(-1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productId must be positive");
        }
    }

    @Nested
    @DisplayName("value() 테스트")
    class ValueTests {

        @Test
        @DisplayName("올바른 키 형식 반환")
        void shouldReturnCorrectKeyFormat() {
            // given
            Long productId = 123L;
            StockLockKey lockKey = new StockLockKey(productId);

            // when
            String value = lockKey.value();

            // then
            assertThat(value).isEqualTo("lock:stock:product:123");
        }

        @Test
        @DisplayName("다른 productId는 다른 키 생성")
        void shouldGenerateDifferentKeysForDifferentProductIds() {
            // given
            StockLockKey lockKey1 = new StockLockKey(100L);
            StockLockKey lockKey2 = new StockLockKey(200L);

            // when & then
            assertThat(lockKey1.value()).isNotEqualTo(lockKey2.value());
        }
    }

    @Nested
    @DisplayName("equals/hashCode 테스트")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("같은 productId는 동등함")
        void shouldBeEqualWhenSameProductId() {
            // given
            StockLockKey lockKey1 = new StockLockKey(100L);
            StockLockKey lockKey2 = new StockLockKey(100L);

            // when & then
            assertThat(lockKey1).isEqualTo(lockKey2);
            assertThat(lockKey1.hashCode()).isEqualTo(lockKey2.hashCode());
        }

        @Test
        @DisplayName("다른 productId는 동등하지 않음")
        void shouldNotBeEqualWhenDifferentProductId() {
            // given
            StockLockKey lockKey1 = new StockLockKey(100L);
            StockLockKey lockKey2 = new StockLockKey(200L);

            // when & then
            assertThat(lockKey1).isNotEqualTo(lockKey2);
        }
    }
}
