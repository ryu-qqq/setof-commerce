package com.ryuqq.setof.domain.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import com.ryuqq.setof.domain.shipment.exception.InvalidStatusTransitionException;
import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.Sender;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import com.ryuqq.setof.domain.shipment.vo.ShipmentType;
import com.ryuqq.setof.domain.shipment.vo.TrackingInfo;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Shipment Aggregate 테스트
 *
 * <p>운송장 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Shipment Aggregate")
class ShipmentTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant UPDATED_TIME = Instant.parse("2025-01-02T00:00:00Z");
    private static final Long SELLER_ID = 100L;
    private static final Long CHECKOUT_ID = 200L;
    private static final Long CARRIER_ID = 1L;

    @Nested
    @DisplayName("forNew() - 신규 운송장 생성")
    class ForNew {

        @Test
        @DisplayName("신규 운송장을 생성할 수 있다")
        void shouldCreateNewShipment() {
            // given
            InvoiceNumber invoiceNumber = InvoiceNumber.of("1234567890");
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구");

            // when
            Shipment shipment =
                    Shipment.forNew(
                            SELLER_ID, CHECKOUT_ID, CARRIER_ID, invoiceNumber, sender, FIXED_TIME);

            // then
            assertNotNull(shipment);
            assertNull(shipment.getId());
            assertTrue(shipment.isNew());
            assertEquals(SELLER_ID, shipment.getSellerId());
            assertEquals(CHECKOUT_ID, shipment.getCheckoutId());
            assertEquals(CARRIER_ID, shipment.getCarrierId());
            assertEquals("1234567890", shipment.getInvoiceNumberValue());
            assertEquals("테스트셀러", shipment.getSenderName());
            assertEquals(DeliveryStatus.PENDING, shipment.getStatus());
            assertEquals(ShipmentType.STANDARD, shipment.getType());
            assertFalse(shipment.isShipped());
            assertFalse(shipment.isDelivered());
        }

        @Test
        @DisplayName("배송 유형을 지정하여 운송장을 생성할 수 있다")
        void shouldCreateWithSpecificType() {
            // given
            InvoiceNumber invoiceNumber = InvoiceNumber.of("1234567890");
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", null);

            // when
            Shipment shipment =
                    Shipment.forNew(
                            SELLER_ID,
                            CHECKOUT_ID,
                            CARRIER_ID,
                            invoiceNumber,
                            sender,
                            ShipmentType.SAME_DAY,
                            FIXED_TIME);

            // then
            assertEquals(ShipmentType.SAME_DAY, shipment.getType());
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속화 데이터 복원")
    class Reconstitute {

        @Test
        @DisplayName("영속화된 데이터로 운송장을 복원할 수 있다")
        void shouldReconstituteShipment() {
            // given
            ShipmentId id = ShipmentId.of(1L);
            InvoiceNumber invoiceNumber = InvoiceNumber.of("1234567890");
            Sender sender = Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구");
            TrackingInfo trackingInfo = TrackingInfo.of("강남집배센터", "배송중", FIXED_TIME);

            // when
            Shipment shipment =
                    Shipment.reconstitute(
                            id,
                            SELLER_ID,
                            CHECKOUT_ID,
                            CARRIER_ID,
                            invoiceNumber,
                            sender,
                            ShipmentType.STANDARD,
                            DeliveryStatus.IN_TRANSIT,
                            trackingInfo,
                            FIXED_TIME,
                            FIXED_TIME,
                            FIXED_TIME);

            // then
            assertNotNull(shipment);
            assertEquals(1L, shipment.getIdValue());
            assertFalse(shipment.isNew());
            assertEquals(DeliveryStatus.IN_TRANSIT, shipment.getStatus());
            assertEquals("강남집배센터", shipment.getLastLocation());
        }
    }

    @Nested
    @DisplayName("changeStatus() - 배송 상태 변경")
    class ChangeStatus {

        @Test
        @DisplayName("PENDING에서 IN_TRANSIT로 상태를 변경할 수 있다")
        void shouldChangeToPending() {
            // given
            Shipment shipment = createPendingShipment();

            // when
            Shipment updated = shipment.changeStatus(DeliveryStatus.IN_TRANSIT, UPDATED_TIME);

            // then
            assertEquals(DeliveryStatus.IN_TRANSIT, updated.getStatus());
            assertNotNull(updated.getShippedAt());
            assertEquals(UPDATED_TIME, updated.getShippedAt());
        }

        @Test
        @DisplayName("IN_TRANSIT에서 OUT_FOR_DELIVERY로 상태를 변경할 수 있다")
        void shouldChangeToOutForDelivery() {
            // given
            Shipment shipment = createInTransitShipment();

            // when
            Shipment updated = shipment.changeStatus(DeliveryStatus.OUT_FOR_DELIVERY, UPDATED_TIME);

            // then
            assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, updated.getStatus());
        }

        @Test
        @DisplayName("OUT_FOR_DELIVERY에서 DELIVERED로 상태를 변경할 수 있다")
        void shouldChangeToDelivered() {
            // given
            Shipment shipment = createOutForDeliveryShipment();

            // when
            Shipment updated = shipment.changeStatus(DeliveryStatus.DELIVERED, UPDATED_TIME);

            // then
            assertEquals(DeliveryStatus.DELIVERED, updated.getStatus());
            assertTrue(updated.isDelivered());
            assertNotNull(updated.getTrackingInfo().deliveredAt());
        }

        @Test
        @DisplayName("DELIVERED 상태에서는 다른 상태로 변경할 수 없다")
        void shouldNotChangeFromDelivered() {
            // given
            Shipment shipment = createDeliveredShipment();

            // when & then
            assertThrows(
                    InvalidStatusTransitionException.class,
                    () -> shipment.changeStatus(DeliveryStatus.IN_TRANSIT, UPDATED_TIME));
        }

        @Test
        @DisplayName("역방향 상태 변경은 불가능하다")
        void shouldNotAllowBackwardTransition() {
            // given
            Shipment shipment = createInTransitShipment();

            // when & then
            assertThrows(
                    InvalidStatusTransitionException.class,
                    () -> shipment.changeStatus(DeliveryStatus.PENDING, UPDATED_TIME));
        }
    }

    @Nested
    @DisplayName("updateTracking() - 추적 정보 업데이트")
    class UpdateTracking {

        @Test
        @DisplayName("추적 정보를 업데이트할 수 있다")
        void shouldUpdateTrackingInfo() {
            // given
            Shipment shipment = createInTransitShipment();
            Instant trackedAt = Instant.parse("2025-01-01T10:00:00Z");

            // when
            Shipment updated =
                    shipment.updateTracking("서울 강남 집배센터", "배송 출발", trackedAt, UPDATED_TIME);

            // then
            assertEquals("서울 강남 집배센터", updated.getLastLocation());
            assertEquals("배송 출발", updated.getLastMessage());
            assertEquals(trackedAt, updated.getTrackingInfo().lastTrackedAt());
        }
    }

    @Nested
    @DisplayName("changeInvoice() - 운송장 번호 변경")
    class ChangeInvoice {

        @Test
        @DisplayName("PENDING 상태에서 운송장 번호를 변경할 수 있다")
        void shouldChangeInvoiceWhenPending() {
            // given
            Shipment shipment = createPendingShipment();
            InvoiceNumber newInvoice = InvoiceNumber.of("9876543210");
            Long newCarrierId = 2L;

            // when
            Shipment updated = shipment.changeInvoice(newCarrierId, newInvoice, UPDATED_TIME);

            // then
            assertEquals("9876543210", updated.getInvoiceNumberValue());
            assertEquals(newCarrierId, updated.getCarrierId());
        }

        @Test
        @DisplayName("발송 후에는 운송장 번호를 변경할 수 없다")
        void shouldNotChangeInvoiceAfterShipped() {
            // given
            Shipment shipment = createInTransitShipment();
            InvoiceNumber newInvoice = InvoiceNumber.of("9876543210");

            // when & then
            assertThrows(
                    InvalidStatusTransitionException.class,
                    () -> shipment.changeInvoice(2L, newInvoice, UPDATED_TIME));
        }
    }

    @Nested
    @DisplayName("canBundleWith() - 주문 묶음 가능 여부")
    class CanBundleWith {

        @Test
        @DisplayName("같은 셀러와 같은 결제건이면 묶음이 가능하다")
        void shouldBundleWithSameSellerAndCheckout() {
            // given
            Shipment shipment = createPendingShipment();

            // when & then
            assertTrue(shipment.canBundleWith(SELLER_ID, CHECKOUT_ID));
        }

        @Test
        @DisplayName("다른 셀러면 묶음이 불가능하다")
        void shouldNotBundleWithDifferentSeller() {
            // given
            Shipment shipment = createPendingShipment();

            // when & then
            assertFalse(shipment.canBundleWith(999L, CHECKOUT_ID));
        }

        @Test
        @DisplayName("다른 결제건이면 묶음이 불가능하다")
        void shouldNotBundleWithDifferentCheckout() {
            // given
            Shipment shipment = createPendingShipment();

            // when & then
            assertFalse(shipment.canBundleWith(SELLER_ID, 999L));
        }
    }

    // ========== Helper Methods ==========

    private Shipment createPendingShipment() {
        InvoiceNumber invoiceNumber = InvoiceNumber.of("1234567890");
        Sender sender = Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구");
        return Shipment.forNew(
                SELLER_ID, CHECKOUT_ID, CARRIER_ID, invoiceNumber, sender, FIXED_TIME);
    }

    private Shipment createInTransitShipment() {
        return Shipment.reconstitute(
                ShipmentId.of(1L),
                SELLER_ID,
                CHECKOUT_ID,
                CARRIER_ID,
                InvoiceNumber.of("1234567890"),
                Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구"),
                ShipmentType.STANDARD,
                DeliveryStatus.IN_TRANSIT,
                TrackingInfo.of("물류센터", "배송 중", FIXED_TIME),
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    private Shipment createOutForDeliveryShipment() {
        return Shipment.reconstitute(
                ShipmentId.of(1L),
                SELLER_ID,
                CHECKOUT_ID,
                CARRIER_ID,
                InvoiceNumber.of("1234567890"),
                Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구"),
                ShipmentType.STANDARD,
                DeliveryStatus.OUT_FOR_DELIVERY,
                TrackingInfo.of("강남집배센터", "배송 출발", FIXED_TIME),
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    private Shipment createDeliveredShipment() {
        return Shipment.reconstitute(
                ShipmentId.of(1L),
                SELLER_ID,
                CHECKOUT_ID,
                CARRIER_ID,
                InvoiceNumber.of("1234567890"),
                Sender.of("테스트셀러", "010-1234-5678", "서울시 강남구"),
                ShipmentType.STANDARD,
                DeliveryStatus.DELIVERED,
                TrackingInfo.delivered("고객 주소지", "배송 완료", FIXED_TIME, FIXED_TIME),
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }
}
