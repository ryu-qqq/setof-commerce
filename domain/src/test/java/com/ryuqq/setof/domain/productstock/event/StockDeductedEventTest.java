package com.ryuqq.setof.domain.productstock.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productstock.ProductStockFixture;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StockDeductedEvent 테스트")
class StockDeductedEventTest {

    private static final Instant NOW = Instant.parse("2024-06-01T12:00:00Z");

    @Nested
    @DisplayName("from() 테스트")
    class FromTest {

        @Test
        @DisplayName("ProductStock으로부터 이벤트 생성 성공")
        void shouldCreateFromProductStock() {
            // given
            ProductStock previousStock = ProductStockFixture.createWithQuantity(1L, 100L, 100);
            ProductStock currentStock = previousStock.deduct(30, NOW);
            int deductedQuantity = 30;

            // when
            StockDeductedEvent event =
                    StockDeductedEvent.from(previousStock, currentStock, deductedQuantity, NOW);

            // then
            assertThat(event.productStockId()).isEqualTo(currentStock.getId());
            assertThat(event.productId()).isEqualTo(currentStock.getProductId());
            assertThat(event.previousQuantity()).isEqualTo(100);
            assertThat(event.deductedQuantity()).isEqualTo(30);
            assertThat(event.currentQuantity()).isEqualTo(70);
            assertThat(event.occurredAt()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("전체 재고 차감 이벤트 생성")
        void shouldCreateEventForFullDeduction() {
            // given
            ProductStock previousStock = ProductStockFixture.createWithQuantity(1L, 100L, 50);
            ProductStock currentStock = previousStock.deduct(50, NOW);

            // when
            StockDeductedEvent event =
                    StockDeductedEvent.from(previousStock, currentStock, 50, NOW);

            // then
            assertThat(event.previousQuantity()).isEqualTo(50);
            assertThat(event.deductedQuantity()).isEqualTo(50);
            assertThat(event.currentQuantity()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Record 동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등")
        void shouldBeEqualWhenSameValues() {
            // given
            ProductStock previousStock = ProductStockFixture.createWithQuantity(1L, 100L, 100);
            ProductStock currentStock = previousStock.deduct(30, NOW);

            StockDeductedEvent event1 =
                    StockDeductedEvent.from(previousStock, currentStock, 30, NOW);
            StockDeductedEvent event2 =
                    StockDeductedEvent.from(previousStock, currentStock, 30, NOW);

            // when & then
            assertThat(event1).isEqualTo(event2);
            assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        }
    }

    @Nested
    @DisplayName("DomainEvent 구현 테스트")
    class DomainEventTest {

        @Test
        @DisplayName("occurredAt 필드가 있어야 함")
        void shouldHaveOccurredAt() {
            // given
            ProductStock previousStock = ProductStockFixture.createWithQuantity(1L, 100L, 100);
            ProductStock currentStock = previousStock.deduct(30, NOW);

            // when
            StockDeductedEvent event =
                    StockDeductedEvent.from(previousStock, currentStock, 30, NOW);

            // then
            assertThat(event.occurredAt()).isNotNull();
            assertThat(event.occurredAt()).isEqualTo(NOW);
        }
    }
}
