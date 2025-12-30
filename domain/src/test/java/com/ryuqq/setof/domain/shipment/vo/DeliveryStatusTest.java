package com.ryuqq.setof.domain.shipment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** DeliveryStatus Enum 테스트 */
@DisplayName("DeliveryStatus Enum")
class DeliveryStatusTest {

    @Nested
    @DisplayName("기본 속성 테스트")
    class BasicProperties {

        @Test
        @DisplayName("PENDING은 발송대기 상태이다")
        void pendingShouldBeWaitingStatus() {
            // given
            DeliveryStatus status = DeliveryStatus.PENDING;

            // then
            assertEquals("발송대기", status.description());
            assertEquals(10, status.order());
            assertFalse(status.isTerminal());
            assertTrue(status.isInProgress());
            assertFalse(status.isDelivered());
        }

        @Test
        @DisplayName("DELIVERED는 종료 상태이다")
        void deliveredShouldBeTerminalStatus() {
            // given
            DeliveryStatus status = DeliveryStatus.DELIVERED;

            // then
            assertEquals("배송완료", status.description());
            assertTrue(status.isTerminal());
            assertFalse(status.isInProgress());
            assertTrue(status.isDelivered());
        }

        @Test
        @DisplayName("기본 상태는 PENDING이다")
        void defaultStatusShouldBePending() {
            // when
            DeliveryStatus status = DeliveryStatus.defaultStatus();

            // then
            assertEquals(DeliveryStatus.PENDING, status);
        }
    }

    @Nested
    @DisplayName("canTransitionTo() - 상태 전이 가능 여부")
    class CanTransitionTo {

        @Test
        @DisplayName("PENDING에서 IN_TRANSIT로 전이할 수 있다")
        void canTransitionFromPendingToInTransit() {
            // given
            DeliveryStatus from = DeliveryStatus.PENDING;

            // when & then
            assertTrue(from.canTransitionTo(DeliveryStatus.IN_TRANSIT));
        }

        @Test
        @DisplayName("PENDING에서 DELIVERED로 바로 전이할 수 있다")
        void canTransitionFromPendingToDelivered() {
            // given
            DeliveryStatus from = DeliveryStatus.PENDING;

            // when & then
            assertTrue(from.canTransitionTo(DeliveryStatus.DELIVERED));
        }

        @Test
        @DisplayName("IN_TRANSIT에서 OUT_FOR_DELIVERY로 전이할 수 있다")
        void canTransitionFromInTransitToOutForDelivery() {
            // given
            DeliveryStatus from = DeliveryStatus.IN_TRANSIT;

            // when & then
            assertTrue(from.canTransitionTo(DeliveryStatus.OUT_FOR_DELIVERY));
        }

        @Test
        @DisplayName("DELIVERED에서는 다른 상태로 전이할 수 없다")
        void cannotTransitionFromDelivered() {
            // given
            DeliveryStatus from = DeliveryStatus.DELIVERED;

            // when & then
            assertFalse(from.canTransitionTo(DeliveryStatus.PENDING));
            assertFalse(from.canTransitionTo(DeliveryStatus.IN_TRANSIT));
            assertFalse(from.canTransitionTo(DeliveryStatus.OUT_FOR_DELIVERY));
        }

        @Test
        @DisplayName("같은 상태로는 전이할 수 없다")
        void cannotTransitionToSameStatus() {
            // given
            DeliveryStatus from = DeliveryStatus.IN_TRANSIT;

            // when & then
            assertFalse(from.canTransitionTo(DeliveryStatus.IN_TRANSIT));
        }

        @Test
        @DisplayName("역방향으로는 전이할 수 없다")
        void cannotTransitionBackward() {
            // given
            DeliveryStatus from = DeliveryStatus.OUT_FOR_DELIVERY;

            // when & then
            assertFalse(from.canTransitionTo(DeliveryStatus.PENDING));
            assertFalse(from.canTransitionTo(DeliveryStatus.IN_TRANSIT));
        }
    }
}
