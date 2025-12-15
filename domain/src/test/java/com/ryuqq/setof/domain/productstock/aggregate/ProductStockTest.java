package com.ryuqq.setof.domain.productstock.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.ProductStockFixture;
import com.ryuqq.setof.domain.productstock.exception.NotEnoughStockException;
import com.ryuqq.setof.domain.productstock.exception.StockOverflowException;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import com.ryuqq.setof.domain.productstock.vo.StockQuantity;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductStock Aggregate 테스트")
class ProductStockTest {

    private static final Instant NOW = Instant.parse("2024-06-01T12:00:00Z");

    @Nested
    @DisplayName("create() 테스트")
    class CreateTest {

        @Test
        @DisplayName("신규 재고 생성 성공")
        void shouldCreateNewProductStock() {
            // given
            ProductId productId = ProductId.of(100L);
            StockQuantity quantity = StockQuantity.of(50);
            Instant createdAt = NOW;

            // when
            ProductStock stock = ProductStock.create(productId, quantity, createdAt);

            // then
            assertThat(stock.isNew()).isTrue();
            assertThat(stock.getProductIdValue()).isEqualTo(100L);
            assertThat(stock.getQuantityValue()).isEqualTo(50);
            assertThat(stock.getCreatedAt()).isEqualTo(createdAt);
            assertThat(stock.getUpdatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("productId가 null이면 예외 발생")
        void shouldThrowWhenProductIdIsNull() {
            // given
            StockQuantity quantity = StockQuantity.of(50);

            // when & then
            assertThatThrownBy(() -> ProductStock.create(null, quantity, NOW))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("상품 ID는 필수입니다");
        }

        @Test
        @DisplayName("quantity가 null이면 예외 발생")
        void shouldThrowWhenQuantityIsNull() {
            // given
            ProductId productId = ProductId.of(100L);

            // when & then
            assertThatThrownBy(() -> ProductStock.create(productId, null, NOW))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("재고 수량은 필수입니다");
        }
    }

    @Nested
    @DisplayName("reconstitute() 테스트")
    class ReconstituteTest {

        @Test
        @DisplayName("기존 재고 복원 성공")
        void shouldReconstituteProductStock() {
            // given
            ProductStockId id = ProductStockId.of(1L);
            ProductId productId = ProductId.of(100L);
            StockQuantity quantity = StockQuantity.of(50);
            Long version = 0L;
            Instant createdAt = NOW;
            Instant updatedAt = NOW.plusSeconds(3600);

            // when
            ProductStock stock =
                    ProductStock.reconstitute(
                            id, productId, quantity, version, createdAt, updatedAt);

            // then
            assertThat(stock.isNew()).isFalse();
            assertThat(stock.getIdValue()).isEqualTo(1L);
            assertThat(stock.getProductIdValue()).isEqualTo(100L);
            assertThat(stock.getQuantityValue()).isEqualTo(50);
            assertThat(stock.getVersionValue()).isEqualTo(0L);
            assertThat(stock.getCreatedAt()).isEqualTo(createdAt);
            assertThat(stock.getUpdatedAt()).isEqualTo(updatedAt);
        }
    }

    @Nested
    @DisplayName("deduct() 테스트")
    class DeductTest {

        @Test
        @DisplayName("재고 차감 성공")
        void shouldDeductStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 100);
            Instant now = NOW;

            // when
            ProductStock deducted = stock.deduct(30, now);

            // then
            assertThat(deducted.getQuantityValue()).isEqualTo(70);
            assertThat(deducted.getUpdatedAt()).isEqualTo(now);
            assertThat(stock.getQuantityValue()).isEqualTo(100); // 원본 불변
        }

        @Test
        @DisplayName("재고 부족 시 예외 발생")
        void shouldThrowWhenNotEnoughStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 10);

            // when & then
            assertThatThrownBy(() -> stock.deduct(20, NOW))
                    .isInstanceOf(NotEnoughStockException.class);
        }

        @Test
        @DisplayName("전체 재고 차감 성공")
        void shouldDeductAllStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 50);

            // when
            ProductStock deducted = stock.deduct(50, NOW);

            // then
            assertThat(deducted.getQuantityValue()).isEqualTo(0);
            assertThat(deducted.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("restore() 테스트")
    class RestoreTest {

        @Test
        @DisplayName("재고 복원 성공")
        void shouldRestoreStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 50);
            Instant now = NOW;

            // when
            ProductStock restored = stock.restore(30, now);

            // then
            assertThat(restored.getQuantityValue()).isEqualTo(80);
            assertThat(restored.getUpdatedAt()).isEqualTo(now);
            assertThat(stock.getQuantityValue()).isEqualTo(50); // 원본 불변
        }

        @Test
        @DisplayName("빈 재고에서 복원 성공")
        void shouldRestoreFromEmptyStock() {
            // given
            ProductStock stock = ProductStockFixture.createEmpty();

            // when
            ProductStock restored = stock.restore(100, NOW);

            // then
            assertThat(restored.getQuantityValue()).isEqualTo(100);
            assertThat(restored.hasStock()).isTrue();
        }

        @Test
        @DisplayName("최대 재고 초과 시 예외 발생")
        void shouldThrowWhenStockOverflow() {
            // given
            ProductStock stock =
                    ProductStockFixture.createWithQuantity(1L, 100L, Integer.MAX_VALUE - 10);

            // when & then
            assertThatThrownBy(() -> stock.restore(20, NOW))
                    .isInstanceOf(StockOverflowException.class);
        }
    }

    @Nested
    @DisplayName("setQuantity() 테스트")
    class SetQuantityTest {

        @Test
        @DisplayName("재고 수량 설정 성공")
        void shouldSetQuantity() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 50);
            Instant now = NOW;

            // when
            ProductStock updated = stock.setQuantity(200, now);

            // then
            assertThat(updated.getQuantityValue()).isEqualTo(200);
            assertThat(updated.getUpdatedAt()).isEqualTo(now);
            assertThat(stock.getQuantityValue()).isEqualTo(50); // 원본 불변
        }

        @Test
        @DisplayName("재고를 0으로 설정 성공")
        void shouldSetQuantityToZero() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 50);

            // when
            ProductStock updated = stock.setQuantity(0, NOW);

            // then
            assertThat(updated.getQuantityValue()).isEqualTo(0);
            assertThat(updated.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("isAvailable() 테스트")
    class IsAvailableTest {

        @Test
        @DisplayName("재고가 충분하면 true 반환")
        void shouldReturnTrueWhenEnoughStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 100);

            // when & then
            assertThat(stock.isAvailable(50)).isTrue();
            assertThat(stock.isAvailable(100)).isTrue();
        }

        @Test
        @DisplayName("재고가 부족하면 false 반환")
        void shouldReturnFalseWhenNotEnoughStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 10);

            // when & then
            assertThat(stock.isAvailable(20)).isFalse();
        }
    }

    @Nested
    @DisplayName("hasStock() / isEmpty() 테스트")
    class StockStatusTest {

        @Test
        @DisplayName("재고가 있으면 hasStock() true, isEmpty() false")
        void shouldReturnTrueWhenHasStock() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 10);

            // when & then
            assertThat(stock.hasStock()).isTrue();
            assertThat(stock.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("재고가 없으면 hasStock() false, isEmpty() true")
        void shouldReturnFalseWhenEmpty() {
            // given
            ProductStock stock = ProductStockFixture.createEmpty();

            // when & then
            assertThat(stock.hasStock()).isFalse();
            assertThat(stock.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Getter 테스트")
    class GetterTest {

        @Test
        @DisplayName("모든 getter가 올바른 값 반환")
        void shouldReturnCorrectValues() {
            // given
            ProductStock stock = ProductStockFixture.createDefault();

            // when & then
            assertThat(stock.getId()).isNotNull();
            assertThat(stock.getProductId()).isNotNull();
            assertThat(stock.getQuantity()).isNotNull();
            assertThat(stock.getCreatedAt()).isNotNull();
            assertThat(stock.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Law of Demeter helper 메서드가 올바른 값 반환")
        void shouldReturnCorrectHelperValues() {
            // given
            ProductStock stock = ProductStockFixture.createWithQuantity(1L, 100L, 50);

            // when & then
            assertThat(stock.getIdValue()).isEqualTo(1L);
            assertThat(stock.getProductIdValue()).isEqualTo(100L);
            assertThat(stock.getQuantityValue()).isEqualTo(50);
        }
    }
}
