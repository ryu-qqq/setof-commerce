package com.ryuqq.setof.domain.claim.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.claim.ClaimFixture;
import com.ryuqq.setof.domain.claim.exception.ClaimShippingException;
import com.ryuqq.setof.domain.claim.exception.ClaimStatusException;
import com.ryuqq.setof.domain.claim.vo.ClaimReason;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.claim.vo.InspectionResult;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Claim Aggregate")
class ClaimTest {

    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("request() - 신규 클레임 생성")
    class Request {

        @Test
        @DisplayName("반품 클레임을 생성할 수 있다")
        void shouldCreateReturnClaim() {
            // given
            String orderId = "ORD-20240115-0001";
            String orderItemId = "ITEM-001";
            ClaimType claimType = ClaimType.RETURN;
            ClaimReason claimReason = ClaimReason.CHANGE_OF_MIND;
            String reasonDetail = "단순 변심";
            Integer quantity = 1;
            BigDecimal refundAmount = BigDecimal.valueOf(50000);

            // when
            Claim claim =
                    Claim.request(
                            orderId,
                            orderItemId,
                            claimType,
                            claimReason,
                            reasonDetail,
                            quantity,
                            refundAmount,
                            NOW);

            // then
            assertNotNull(claim.claimId());
            assertNotNull(claim.claimNumber());
            assertEquals(orderId, claim.orderId());
            assertEquals(orderItemId, claim.orderItemId());
            assertEquals(claimType, claim.claimType());
            assertEquals(claimReason, claim.claimReason());
            assertEquals(ClaimStatus.REQUESTED, claim.status());
            assertEquals(ReturnShippingStatus.PENDING, claim.returnShippingStatus());
        }

        @Test
        @DisplayName("취소 클레임은 반품 배송 상태가 null이다")
        void shouldCreateCancelClaimWithoutShippingStatus() {
            // given
            String orderId = "ORD-20240115-0001";
            ClaimType claimType = ClaimType.CANCEL;
            ClaimReason claimReason = ClaimReason.CHANGE_OF_MIND;

            // when
            Claim claim =
                    Claim.request(
                            orderId,
                            null,
                            claimType,
                            claimReason,
                            "주문 취소",
                            null,
                            BigDecimal.valueOf(100000),
                            NOW);

            // then
            assertEquals(claimType, claim.claimType());
            assertNull(claim.returnShippingStatus());
        }

        @Test
        @DisplayName("orderId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOrderIdIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Claim.request(
                                    null,
                                    "ITEM-001",
                                    ClaimType.RETURN,
                                    ClaimReason.CHANGE_OF_MIND,
                                    "사유",
                                    1,
                                    BigDecimal.valueOf(10000),
                                    NOW));
        }
    }

    @Nested
    @DisplayName("scheduleReturnPickup() - 수거 예약")
    class ScheduleReturnPickup {

        @Test
        @DisplayName("승인된 반품 클레임에 수거 예약을 할 수 있다")
        void shouldSchedulePickupForApprovedClaim() {
            // given
            Claim claim = ClaimFixture.createApprovedReturnClaim();
            Instant scheduledAt = NOW.plusSeconds(86400);
            String pickupAddress = "서울시 강남구 테헤란로 123";
            String customerPhone = "010-1234-5678";

            // when
            claim.scheduleReturnPickup(scheduledAt, pickupAddress, customerPhone, NOW);

            // then
            assertEquals(ReturnShippingMethod.SELLER_PICKUP, claim.returnShippingMethod());
            assertEquals(ReturnShippingStatus.PICKUP_SCHEDULED, claim.returnShippingStatus());
            assertEquals(scheduledAt, claim.returnPickupScheduledAt());
            assertEquals(pickupAddress, claim.returnPickupAddress());
            assertEquals(customerPhone, claim.returnCustomerPhone());
            assertEquals(ClaimStatus.IN_PROGRESS, claim.status());
        }

        @Test
        @DisplayName("이미 수거 예약된 클레임에 다시 예약하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyScheduled() {
            // given
            Claim claim = ClaimFixture.createPickupScheduledClaim();

            // when & then
            assertThrows(
                    ClaimShippingException.class,
                    () -> claim.scheduleReturnPickup(NOW, "주소", "010-0000-0000", NOW));
        }

        @Test
        @DisplayName("취소 클레임에 수거 예약하면 예외가 발생한다")
        void shouldThrowExceptionForCancelClaim() {
            // given
            Claim claim = ClaimFixture.createCancelClaim();

            // when & then
            assertThrows(
                    ClaimShippingException.class,
                    () -> claim.scheduleReturnPickup(NOW, "주소", "010-0000-0000", NOW));
        }
    }

    @Nested
    @DisplayName("registerReturnShipping() - 반품 송장 등록")
    class RegisterReturnShipping {

        @Test
        @DisplayName("승인된 클레임에 고객 직접 발송 송장을 등록할 수 있다")
        void shouldRegisterCustomerShipping() {
            // given
            Claim claim = ClaimFixture.createApprovedReturnClaim();
            String trackingNumber = "1234567890";
            String carrier = "CJ대한통운";

            // when
            claim.registerReturnShipping(
                    ReturnShippingMethod.CUSTOMER_SHIP, trackingNumber, carrier, NOW);

            // then
            assertEquals(ReturnShippingMethod.CUSTOMER_SHIP, claim.returnShippingMethod());
            assertEquals(ReturnShippingStatus.IN_TRANSIT, claim.returnShippingStatus());
            assertEquals(trackingNumber, claim.returnTrackingNumber());
            assertEquals(carrier, claim.returnCarrier());
            assertEquals(ClaimStatus.IN_PROGRESS, claim.status());
        }

        @Test
        @DisplayName("이미 배송 중인 클레임에 송장 등록하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyInTransit() {
            // given
            Claim claim = ClaimFixture.createReturnReceivedClaim();

            // when & then
            assertThrows(
                    ClaimShippingException.class,
                    () ->
                            claim.registerReturnShipping(
                                    ReturnShippingMethod.CUSTOMER_SHIP, "111", "택배", NOW));
        }
    }

    @Nested
    @DisplayName("updateReturnShippingStatus() - 반품 배송 상태 업데이트")
    class UpdateReturnShippingStatus {

        @Test
        @DisplayName("수거 예약 → 수거 완료로 상태를 변경할 수 있다")
        void shouldUpdateToPickedUp() {
            // given
            Claim claim = ClaimFixture.createPickupScheduledClaim();

            // when
            claim.updateReturnShippingStatus(ReturnShippingStatus.PICKED_UP, null, NOW);

            // then
            assertEquals(ReturnShippingStatus.PICKED_UP, claim.returnShippingStatus());
        }

        @Test
        @DisplayName("송장번호와 함께 배송 중으로 상태를 변경할 수 있다")
        void shouldUpdateToInTransitWithTracking() {
            // given
            Claim claim = ClaimFixture.createPickupScheduledClaim();
            claim.updateReturnShippingStatus(ReturnShippingStatus.PICKED_UP, null, NOW);
            String trackingNumber = "9876543210";

            // when
            claim.updateReturnShippingStatus(ReturnShippingStatus.IN_TRANSIT, trackingNumber, NOW);

            // then
            assertEquals(ReturnShippingStatus.IN_TRANSIT, claim.returnShippingStatus());
            assertEquals(trackingNumber, claim.returnTrackingNumber());
        }
    }

    @Nested
    @DisplayName("confirmReturnReceived() - 반품 수령 확인 및 검수")
    class ConfirmReturnReceived {

        @Test
        @DisplayName("검수 합격 시 반품이 완료된다")
        void shouldCompleteReturnOnInspectionPass() {
            // given
            Claim claim = ClaimFixture.createReturnReceivedClaim();
            // 배송 상태를 IN_TRANSIT으로 변경 (canConfirmReceived는 IN_TRANSIT일 때만 true)
            claim = createClaimWithInTransitStatus();
            String note = "검수 완료 - 이상 없음";

            // when
            claim.confirmReturnReceived(InspectionResult.PASS, note, NOW);

            // then
            assertEquals(ReturnShippingStatus.RECEIVED, claim.returnShippingStatus());
            assertEquals(InspectionResult.PASS, claim.inspectionResult());
            assertEquals(note, claim.inspectionNote());
            assertNotNull(claim.returnReceivedAt());
            assertEquals(ClaimStatus.COMPLETED, claim.status());
        }

        @Test
        @DisplayName("검수 불합격 시 클레임이 거부된다")
        void shouldRejectClaimOnInspectionFail() {
            // given
            Claim claim = createClaimWithInTransitStatus();
            String note = "상품 훼손 - 고객 귀책";

            // when
            claim.confirmReturnReceived(InspectionResult.FAIL, note, NOW);

            // then
            assertEquals(InspectionResult.FAIL, claim.inspectionResult());
            assertEquals(ClaimStatus.REJECTED, claim.status());
        }

        @Test
        @DisplayName("부분 합격 시 처리 중 상태가 유지된다 (감가 환불 처리 필요)")
        void shouldKeepInProgressOnPartialPass() {
            // given
            Claim claim = createClaimWithInTransitStatus();
            String note = "일부 훼손 - 감가 환불 필요";

            // when
            claim.confirmReturnReceived(InspectionResult.PARTIAL, note, NOW);

            // then
            assertEquals(InspectionResult.PARTIAL, claim.inspectionResult());
            assertEquals(ClaimStatus.IN_PROGRESS, claim.status());
        }

        private Claim createClaimWithInTransitStatus() {
            return ClaimFixture.createCustom(
                    100L,
                    "CLM-TEST-001",
                    "ORD-TEST-001",
                    ClaimType.RETURN,
                    ClaimStatus.IN_PROGRESS,
                    ReturnShippingStatus.IN_TRANSIT);
        }
    }

    @Nested
    @DisplayName("registerExchangeShipping() - 교환품 발송 등록")
    class RegisterExchangeShipping {

        @Test
        @DisplayName("검수 합격한 교환 클레임에 교환품 발송을 등록할 수 있다")
        void shouldRegisterExchangeShipping() {
            // given
            Claim claim = ClaimFixture.createInspectedReturnClaim();
            // 교환 클레임으로 변경
            claim = createExchangeClaimWithPassedInspection();
            String trackingNumber = "2222222222";
            String carrier = "CJ대한통운";

            // when
            claim.registerExchangeShipping(trackingNumber, carrier, NOW);

            // then
            assertEquals(trackingNumber, claim.exchangeTrackingNumber());
            assertEquals(carrier, claim.exchangeCarrier());
            assertNotNull(claim.exchangeShippedAt());
        }

        @Test
        @DisplayName("반품 클레임에 교환품 발송하면 예외가 발생한다")
        void shouldThrowExceptionForReturnClaim() {
            // given
            Claim claim = ClaimFixture.createInspectedReturnClaim();

            // when & then
            assertThrows(
                    ClaimShippingException.class,
                    () -> claim.registerExchangeShipping("111", "택배", NOW));
        }

        private Claim createExchangeClaimWithPassedInspection() {
            return ClaimFixture.createExchangeShippedClaim();
        }
    }

    @Nested
    @DisplayName("confirmExchangeDelivered() - 교환품 배송 완료 확인")
    class ConfirmExchangeDelivered {

        @Test
        @DisplayName("교환품 발송된 클레임의 배송 완료를 확인할 수 있다")
        void shouldConfirmExchangeDelivery() {
            // given
            Claim claim = ClaimFixture.createExchangeShippedClaim();

            // when
            claim.confirmExchangeDelivered(NOW);

            // then
            assertNotNull(claim.exchangeDeliveredAt());
            assertEquals(ClaimStatus.COMPLETED, claim.status());
        }

        @Test
        @DisplayName("교환품 발송되지 않은 클레임에 배송 완료하면 예외가 발생한다")
        void shouldThrowExceptionWhenNotShipped() {
            // given
            Claim claim = ClaimFixture.createExchangeClaim();

            // when & then
            assertThrows(ClaimShippingException.class, () -> claim.confirmExchangeDelivered(NOW));
        }
    }

    @Nested
    @DisplayName("기본 상태 전이")
    class StatusTransition {

        @Test
        @DisplayName("REQUESTED → APPROVED 전이가 가능하다")
        void shouldApproveRequestedClaim() {
            // given
            Claim claim = ClaimFixture.createReturnClaim();
            String processedBy = "admin@example.com";

            // when
            claim.approve(processedBy, NOW);

            // then
            assertEquals(ClaimStatus.APPROVED, claim.status());
            assertEquals(processedBy, claim.processedBy());
            assertNotNull(claim.processedAt());
        }

        @Test
        @DisplayName("REQUESTED → REJECTED 전이가 가능하다")
        void shouldRejectRequestedClaim() {
            // given
            Claim claim = ClaimFixture.createReturnClaim();
            String processedBy = "admin@example.com";
            String rejectReason = "반품 기간 만료";

            // when
            claim.reject(processedBy, rejectReason, NOW);

            // then
            assertEquals(ClaimStatus.REJECTED, claim.status());
            assertEquals(rejectReason, claim.rejectReason());
        }

        @Test
        @DisplayName("APPROVED → IN_PROGRESS 전이가 가능하다")
        void shouldStartProcessingApprovedClaim() {
            // given
            Claim claim = ClaimFixture.createApprovedReturnClaim();

            // when
            claim.startProcessing(NOW);

            // then
            assertEquals(ClaimStatus.IN_PROGRESS, claim.status());
        }

        @Test
        @DisplayName("이미 처리된 클레임을 다시 승인하면 예외가 발생한다")
        void shouldThrowExceptionWhenApprovingProcessedClaim() {
            // given
            Claim claim = ClaimFixture.createApprovedReturnClaim();

            // when & then
            assertThrows(ClaimStatusException.class, () -> claim.approve("admin", NOW));
        }
    }

    @Nested
    @DisplayName("비즈니스 규칙 검증")
    class BusinessRules {

        @Test
        @DisplayName("반품 클레임은 반품이 필요하다")
        void returnClaimShouldRequireReturn() {
            // given
            Claim claim = ClaimFixture.createReturnClaim();

            // then
            assertTrue(claim.requiresReturn());
        }

        @Test
        @DisplayName("교환 클레임은 반품이 필요하다")
        void exchangeClaimShouldRequireReturn() {
            // given
            Claim claim = ClaimFixture.createExchangeClaim();

            // then
            assertTrue(claim.requiresReturn());
        }

        @Test
        @DisplayName("취소 클레임은 환불이 필요하다")
        void cancelClaimShouldRequireRefund() {
            // given
            Claim claim = ClaimFixture.createCancelClaim();

            // then
            assertTrue(claim.requiresRefund());
        }

        @Test
        @DisplayName("고객 귀책 사유 확인")
        void shouldIdentifyCustomerFault() {
            // given
            Claim claim = ClaimFixture.createReturnClaim();

            // then
            assertTrue(claim.isCustomerFault());
        }
    }
}
