package com.ryuqq.setof.domain.order.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.order.exception.InvalidOrderItemStatusTransitionException;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.setof.commerce.domain.order.OrderFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OrderItem Aggregate 단위 테스트")
class OrderItemTest {

    @Nested
    @DisplayName("forNew() - 신규 주문 아이템 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 주문 아이템을 PENDING 상태로 생성한다")
        void createNewOrderItemWithPendingStatus() {
            // given
            Instant now = CommonVoFixtures.now();

            // when
            OrderItem item =
                    OrderItem.forNew(
                            OrderFixtures.defaultOrderId(),
                            com.ryuqq.setof.domain.seller.id.SellerId.of(1L),
                            com.ryuqq.setof.domain.productgroup.id.ProductGroupId.of(1L),
                            com.ryuqq.setof.domain.product.id.ProductId.of(10L),
                            com.ryuqq.setof.domain.order.vo.OrderItemQuantity.of(2),
                            OrderFixtures.defaultOrderItemPrice(),
                            OrderFixtures.defaultOrderProductSnapshot(),
                            now);

            // then
            assertThat(item.isNew()).isTrue();
            assertThat(item.status()).isEqualTo(OrderItemStatus.PENDING);
            assertThat(item.quantityValue()).isEqualTo(2);
            assertThat(item.orderAmount())
                    .isEqualTo(OrderFixtures.defaultOrderItemPrice().orderAmount());
            assertThat(item.createdAt()).isEqualTo(now);
            assertThat(item.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("상태 전이 메서드 테스트")
    class StatusTransitionTest {

        @Test
        @DisplayName("PENDING 상태에서 confirm()을 호출하면 CONFIRMED로 전이한다")
        void confirmFromPending() {
            // given
            OrderItem item = OrderFixtures.pendingOrderItem();
            Instant now = CommonVoFixtures.now();

            // when
            item.confirm(now);

            // then
            assertThat(item.status()).isEqualTo(OrderItemStatus.CONFIRMED);
            assertThat(item.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("CONFIRMED 상태에서 readyToShip()을 호출하면 SHIPPING_READY로 전이한다")
        void readyToShipFromConfirmed() {
            // given
            OrderItem item = OrderFixtures.confirmedOrderItem();
            Instant now = CommonVoFixtures.now();

            // when
            item.readyToShip(now);

            // then
            assertThat(item.status()).isEqualTo(OrderItemStatus.SHIPPING_READY);
        }

        @Test
        @DisplayName("DELIVERED 상태에서 confirmPurchase()를 호출하면 PURCHASE_CONFIRMED로 전이한다")
        void confirmPurchaseFromDelivered() {
            // given
            OrderItem item = OrderFixtures.deliveredOrderItem();
            Instant now = CommonVoFixtures.now();

            // when
            item.confirmPurchase(now);

            // then
            assertThat(item.status()).isEqualTo(OrderItemStatus.PURCHASE_CONFIRMED);
        }
    }

    @Nested
    @DisplayName("허용되지 않는 상태 전이 테스트")
    class InvalidTransitionTest {

        @Test
        @DisplayName("PENDING 상태에서 ship()을 호출하면 예외가 발생한다")
        void shipFromPending_ThrowsException() {
            // given
            OrderItem item = OrderFixtures.pendingOrderItem();

            // when & then
            assertThatThrownBy(() -> item.ship(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidOrderItemStatusTransitionException.class);
        }

        @Test
        @DisplayName("SETTLED 상태에서 추가 전이를 시도하면 예외가 발생한다")
        void transitionFromSettled_ThrowsException() {
            // given
            OrderItem item = OrderFixtures.settledOrderItem();

            // when & then
            assertThatThrownBy(() -> item.confirm(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidOrderItemStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("비즈니스 쿼리 메서드 테스트")
    class BusinessQueryTest {

        @Test
        @DisplayName("PENDING 상태의 아이템은 환불 가능하다")
        void pendingItemIsRefundable() {
            assertThat(OrderFixtures.pendingOrderItem().isRefundable()).isTrue();
        }

        @Test
        @DisplayName("DELIVERED 상태의 아이템은 환불 가능하다")
        void deliveredItemIsRefundable() {
            assertThat(OrderFixtures.deliveredOrderItem().isRefundable()).isTrue();
        }

        @Test
        @DisplayName("SETTLED 상태의 아이템은 환불 불가능하다")
        void settledItemIsNotRefundable() {
            assertThat(OrderFixtures.settledOrderItem().isRefundable()).isFalse();
        }

        @Test
        @DisplayName("SETTLED 상태의 아이템은 isSettled()가 true를 반환한다")
        void settledItemIsSettled() {
            assertThat(OrderFixtures.settledOrderItem().isSettled()).isTrue();
        }

        @Test
        @DisplayName("PENDING 상태의 아이템은 배송 단계가 아니다")
        void pendingItemIsNotDeliveryStep() {
            assertThat(OrderFixtures.pendingOrderItem().isDeliveryStep()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 OrderItemId의 원시값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            OrderItem item = OrderFixtures.pendingOrderItem();

            // then
            assertThat(item.idValue()).isEqualTo(200L);
        }

        @Test
        @DisplayName("snapshot()은 주문 시점 상품 스냅샷을 반환한다")
        void snapshotReturnsOrderProductSnapshot() {
            // given
            OrderItem item = OrderFixtures.pendingOrderItem();

            // then
            assertThat(item.snapshot()).isNotNull();
            assertThat(item.snapshot().productGroupName()).isEqualTo("테스트 상품 A");
        }
    }
}
