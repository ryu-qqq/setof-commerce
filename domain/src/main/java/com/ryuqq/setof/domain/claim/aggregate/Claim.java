package com.ryuqq.setof.domain.claim.aggregate;

import com.ryuqq.setof.domain.claim.exception.ClaimShippingException;
import com.ryuqq.setof.domain.claim.exception.ClaimStatusException;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.claim.vo.ClaimReason;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.claim.vo.InspectionResult;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Claim - 클레임 Aggregate Root
 *
 * <p>주문에 대한 취소/반품/교환/환불 요청을 관리하는 핵심 도메인 객체입니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * REQUESTED → APPROVED → IN_PROGRESS → COMPLETED
 *     │
 *     └→ REJECTED
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - 모든 메서드 직접 구현
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드를 통해서만 수행
 *   <li>불변 필드 + 상태 변경 메서드
 *   <li>Instant.now() 직접 호출 금지 - 파라미터로 시간 전달
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public final class Claim {

    private final Long id;
    private final ClaimId claimId;
    private final ClaimNumber claimNumber;
    private final String orderId;
    private final String orderItemId;
    private final ClaimType claimType;
    private final ClaimReason claimReason;
    private final String claimReasonDetail;
    private final Integer quantity;
    private final BigDecimal refundAmount;
    private ClaimStatus status;
    private String processedBy;
    private Instant processedAt;
    private String rejectReason;
    private String returnTrackingNumber;
    private String returnCarrier;
    private String exchangeTrackingNumber;
    private String exchangeCarrier;
    private final Instant createdAt;
    private Instant updatedAt;

    // ========== 반품 배송 관련 필드 ==========
    private ReturnShippingMethod returnShippingMethod;
    private ReturnShippingStatus returnShippingStatus;
    private Instant returnPickupScheduledAt;
    private String returnPickupAddress;
    private String returnCustomerPhone;
    private Instant returnReceivedAt;
    private InspectionResult inspectionResult;
    private String inspectionNote;
    private Instant exchangeShippedAt;
    private Instant exchangeDeliveredAt;

    private Claim(
            Long id,
            ClaimId claimId,
            ClaimNumber claimNumber,
            String orderId,
            String orderItemId,
            ClaimType claimType,
            ClaimReason claimReason,
            String claimReasonDetail,
            Integer quantity,
            BigDecimal refundAmount,
            ClaimStatus status,
            String processedBy,
            Instant processedAt,
            String rejectReason,
            String returnTrackingNumber,
            String returnCarrier,
            String exchangeTrackingNumber,
            String exchangeCarrier,
            Instant createdAt,
            Instant updatedAt,
            ReturnShippingMethod returnShippingMethod,
            ReturnShippingStatus returnShippingStatus,
            Instant returnPickupScheduledAt,
            String returnPickupAddress,
            String returnCustomerPhone,
            Instant returnReceivedAt,
            InspectionResult inspectionResult,
            String inspectionNote,
            Instant exchangeShippedAt,
            Instant exchangeDeliveredAt) {
        this.id = id;
        this.claimId = claimId;
        this.claimNumber = claimNumber;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.claimType = claimType;
        this.claimReason = claimReason;
        this.claimReasonDetail = claimReasonDetail;
        this.quantity = quantity;
        this.refundAmount = refundAmount;
        this.status = status;
        this.processedBy = processedBy;
        this.processedAt = processedAt;
        this.rejectReason = rejectReason;
        this.returnTrackingNumber = returnTrackingNumber;
        this.returnCarrier = returnCarrier;
        this.exchangeTrackingNumber = exchangeTrackingNumber;
        this.exchangeCarrier = exchangeCarrier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.returnShippingMethod = returnShippingMethod;
        this.returnShippingStatus = returnShippingStatus;
        this.returnPickupScheduledAt = returnPickupScheduledAt;
        this.returnPickupAddress = returnPickupAddress;
        this.returnCustomerPhone = returnCustomerPhone;
        this.returnReceivedAt = returnReceivedAt;
        this.inspectionResult = inspectionResult;
        this.inspectionNote = inspectionNote;
        this.exchangeShippedAt = exchangeShippedAt;
        this.exchangeDeliveredAt = exchangeDeliveredAt;
    }

    /**
     * 신규 클레임 요청 생성
     *
     * @param orderId 주문 ID
     * @param orderItemId 주문 상품 ID (전체 주문 클레임이면 null)
     * @param claimType 클레임 유형
     * @param claimReason 클레임 사유
     * @param claimReasonDetail 상세 사유
     * @param quantity 클레임 수량
     * @param refundAmount 환불 예정 금액
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @return 신규 Claim (REQUESTED 상태)
     */
    public static Claim request(
            String orderId,
            String orderItemId,
            ClaimType claimType,
            ClaimReason claimReason,
            String claimReasonDetail,
            Integer quantity,
            BigDecimal refundAmount,
            Instant now) {
        validateRequired(orderId, "orderId");
        validateRequired(claimType, "claimType");
        validateRequired(claimReason, "claimReason");
        validateRequired(now, "now");

        // 반품이 필요한 클레임인 경우 배송 상태 초기화
        ReturnShippingStatus initialShippingStatus =
                claimType.requiresReturn() ? ReturnShippingStatus.PENDING : null;

        return new Claim(
                null,
                ClaimId.generate(),
                ClaimNumber.generate(),
                orderId,
                orderItemId,
                claimType,
                claimReason,
                claimReasonDetail,
                quantity,
                refundAmount,
                ClaimStatus.REQUESTED,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                now,
                now,
                null, // returnShippingMethod - 승인 후 결정
                initialShippingStatus,
                null, // returnPickupScheduledAt
                null, // returnPickupAddress
                null, // returnCustomerPhone
                null, // returnReceivedAt
                null, // inspectionResult
                null, // inspectionNote
                null, // exchangeShippedAt
                null // exchangeDeliveredAt
                );
    }

    /** 영속화된 클레임 복원 */
    public static Claim restore(
            Long id,
            ClaimId claimId,
            ClaimNumber claimNumber,
            String orderId,
            String orderItemId,
            ClaimType claimType,
            ClaimReason claimReason,
            String claimReasonDetail,
            Integer quantity,
            BigDecimal refundAmount,
            ClaimStatus status,
            String processedBy,
            Instant processedAt,
            String rejectReason,
            String returnTrackingNumber,
            String returnCarrier,
            String exchangeTrackingNumber,
            String exchangeCarrier,
            Instant createdAt,
            Instant updatedAt,
            ReturnShippingMethod returnShippingMethod,
            ReturnShippingStatus returnShippingStatus,
            Instant returnPickupScheduledAt,
            String returnPickupAddress,
            String returnCustomerPhone,
            Instant returnReceivedAt,
            InspectionResult inspectionResult,
            String inspectionNote,
            Instant exchangeShippedAt,
            Instant exchangeDeliveredAt) {
        return new Claim(
                id,
                claimId,
                claimNumber,
                orderId,
                orderItemId,
                claimType,
                claimReason,
                claimReasonDetail,
                quantity,
                refundAmount,
                status,
                processedBy,
                processedAt,
                rejectReason,
                returnTrackingNumber,
                returnCarrier,
                exchangeTrackingNumber,
                exchangeCarrier,
                createdAt,
                updatedAt,
                returnShippingMethod,
                returnShippingStatus,
                returnPickupScheduledAt,
                returnPickupAddress,
                returnCustomerPhone,
                returnReceivedAt,
                inspectionResult,
                inspectionNote,
                exchangeShippedAt,
                exchangeDeliveredAt);
    }

    // ========== 상태 변경 메서드 ==========

    /**
     * 클레임 승인
     *
     * @param adminId 승인 처리자 ID
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimStatusException REQUESTED 상태가 아니면
     */
    public void approve(String adminId, Instant now) {
        if (!status.canApprove()) {
            throw ClaimStatusException.cannotApprove(status);
        }
        validateRequired(now, "now");
        this.status = ClaimStatus.APPROVED;
        this.processedBy = adminId;
        this.processedAt = now;
        this.updatedAt = now;
    }

    /**
     * 클레임 반려
     *
     * @param adminId 반려 처리자 ID
     * @param reason 반려 사유
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimStatusException REQUESTED 상태가 아니면
     */
    public void reject(String adminId, String reason, Instant now) {
        if (!status.canReject()) {
            throw ClaimStatusException.cannotReject(status);
        }
        validateRequired(reason, "rejectReason");
        validateRequired(now, "now");
        this.status = ClaimStatus.REJECTED;
        this.processedBy = adminId;
        this.processedAt = now;
        this.rejectReason = reason;
        this.updatedAt = now;
    }

    /**
     * 처리 시작 (수거 시작 등)
     *
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimStatusException APPROVED 상태가 아니면
     */
    public void startProcessing(Instant now) {
        if (!status.canStartProcessing()) {
            throw ClaimStatusException.cannotStartProcessing(status);
        }
        validateRequired(now, "now");
        this.status = ClaimStatus.IN_PROGRESS;
        this.updatedAt = now;
    }

    /**
     * 클레임 완료 처리
     *
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimStatusException IN_PROGRESS/APPROVED 상태가 아니면
     */
    public void complete(Instant now) {
        if (!status.canComplete()) {
            throw ClaimStatusException.cannotComplete(status);
        }
        validateRequired(now, "now");
        this.status = ClaimStatus.COMPLETED;
        this.updatedAt = now;
    }

    /**
     * 고객에 의한 클레임 취소
     *
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimStatusException REQUESTED/APPROVED 상태가 아니면
     */
    public void cancelByCustomer(Instant now) {
        if (!status.canCancel()) {
            throw ClaimStatusException.cannotCancel(status);
        }
        validateRequired(now, "now");
        this.status = ClaimStatus.CANCELLED;
        this.updatedAt = now;
    }

    // ========== 반품 배송 관련 메서드 ==========

    /**
     * 방문수거 예약
     *
     * <p>클레임 승인 후 택배사에 방문수거를 예약합니다.
     *
     * @param scheduledAt 수거 예약 일시
     * @param pickupAddress 수거지 주소
     * @param customerPhone 고객 연락처
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimShippingException 반품이 필요 없는 클레임 유형이거나 수거 예약 불가 상태인 경우
     */
    public void scheduleReturnPickup(
            Instant scheduledAt, String pickupAddress, String customerPhone, Instant now) {
        validateReturnRequired();
        validateRequired(now, "now");
        if (returnShippingStatus == null || !returnShippingStatus.canSchedulePickup()) {
            throw ClaimShippingException.cannotSchedulePickup(returnShippingStatus);
        }
        if (scheduledAt == null || scheduledAt.isBefore(now)) {
            throw ClaimShippingException.invalidPickupSchedule("수거 예약 일시는 현재 시간 이후여야 합니다");
        }
        validateRequired(pickupAddress, "pickupAddress");

        this.returnShippingMethod = ReturnShippingMethod.SELLER_PICKUP;
        this.returnShippingStatus = ReturnShippingStatus.PICKUP_SCHEDULED;
        this.returnPickupScheduledAt = scheduledAt;
        this.returnPickupAddress = pickupAddress;
        this.returnCustomerPhone = customerPhone;
        this.updatedAt = now;

        // 배송 시작 시 클레임 처리 진행 상태로 전환
        if (status.canStartProcessing()) {
            startProcessing(now);
        }
    }

    /**
     * 반품 배송 정보 등록 (송장 번호 등록)
     *
     * <p>고객이 직접 발송하거나, 착불 송장 발급 시 사용합니다.
     *
     * @param shippingMethod 배송 방식
     * @param trackingNumber 송장 번호
     * @param carrier 배송사
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimShippingException 반품이 필요 없는 클레임 유형이거나 송장 등록 불가 상태인 경우
     */
    public void registerReturnShipping(
            ReturnShippingMethod shippingMethod,
            String trackingNumber,
            String carrier,
            Instant now) {
        validateReturnRequired();
        validateRequired(now, "now");
        if (returnShippingStatus != null && !returnShippingStatus.canRegisterTracking()) {
            throw ClaimShippingException.cannotRegisterReturnShipping(returnShippingStatus);
        }
        validateRequired(trackingNumber, "trackingNumber");
        validateRequired(carrier, "carrier");

        this.returnShippingMethod = shippingMethod;
        this.returnTrackingNumber = trackingNumber;
        this.returnCarrier = carrier;
        this.returnShippingStatus = ReturnShippingStatus.IN_TRANSIT;
        this.updatedAt = now;

        // 송장 등록 시 클레임 처리 진행 상태로 전환
        if (status.canStartProcessing()) {
            startProcessing(now);
        }
    }

    /**
     * 반품 배송 상태 업데이트
     *
     * <p>택배사 Webhook이나 관리자에 의해 배송 상태가 변경될 때 사용합니다.
     *
     * @param newStatus 새로운 배송 상태
     * @param trackingNumber 송장 번호 (수거 완료 시 업데이트)
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     */
    public void updateReturnShippingStatus(
            ReturnShippingStatus newStatus, String trackingNumber, Instant now) {
        validateReturnRequired();
        validateRequired(now, "now");

        this.returnShippingStatus = newStatus;

        // 송장 번호 업데이트 (제공된 경우)
        if (trackingNumber != null && !trackingNumber.isBlank()) {
            this.returnTrackingNumber = trackingNumber;
        }
        this.updatedAt = now;
    }

    /**
     * 반품 수령 확인 및 검수 결과 등록
     *
     * <p>판매자가 반품 상품을 수령하고 검수 결과를 등록합니다.
     *
     * @param result 검수 결과 (PASS, FAIL, PARTIAL)
     * @param note 검수 메모 (선택)
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimShippingException 반품이 필요 없는 클레임 유형이거나 수령 확인 불가 상태인 경우
     */
    public void confirmReturnReceived(InspectionResult result, String note, Instant now) {
        validateReturnRequired();
        validateRequired(now, "now");
        if (returnShippingStatus == null || !returnShippingStatus.canConfirmReceived()) {
            throw ClaimShippingException.cannotConfirmReturnReceived(returnShippingStatus);
        }
        validateRequired(result, "inspectionResult");

        this.returnShippingStatus = ReturnShippingStatus.RECEIVED;
        this.returnReceivedAt = now;
        this.inspectionResult = result;
        this.inspectionNote = note;
        this.updatedAt = now;

        // 검수 결과에 따른 상태 전이
        if (result == InspectionResult.PASS) {
            // 검수 합격 - 반품 클레임은 완료, 교환 클레임은 교환품 발송 대기
            if (claimType == ClaimType.RETURN && status.canComplete()) {
                complete(now);
            }
        } else if (result == InspectionResult.FAIL) {
            // 검수 불합격 - 클레임 거부
            if (status != ClaimStatus.REJECTED) {
                this.status = ClaimStatus.REJECTED;
                this.rejectReason = "검수 불합격: " + (note != null ? note : "상품 상태 불량");
            }
        }
        // PARTIAL의 경우 IN_PROGRESS 유지 (감가 환불 처리 필요)
    }

    /**
     * 교환품 발송 등록
     *
     * <p>교환 클레임에서 교환품을 발송할 때 사용합니다. 반품 수령이 완료되어야 교환품 발송이 가능합니다.
     *
     * @param trackingNumber 송장 번호
     * @param carrier 배송사
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimShippingException 교환 클레임이 아니거나 반품 수령이 완료되지 않은 경우
     */
    public void registerExchangeShipping(String trackingNumber, String carrier, Instant now) {
        validateExchangeApplicable();
        validateRequired(now, "now");
        if (returnShippingStatus != ReturnShippingStatus.RECEIVED) {
            throw ClaimShippingException.cannotRegisterExchangeShipping(
                    "반품 수령이 완료되어야 교환품 발송이 가능합니다");
        }
        if (inspectionResult != null && !inspectionResult.isRefundable()) {
            throw ClaimShippingException.cannotRegisterExchangeShipping("검수 불합격으로 교환이 불가합니다");
        }
        validateRequired(trackingNumber, "trackingNumber");
        validateRequired(carrier, "carrier");

        this.exchangeTrackingNumber = trackingNumber;
        this.exchangeCarrier = carrier;
        this.exchangeShippedAt = now;
        this.updatedAt = now;
    }

    /**
     * 교환품 수령 확인
     *
     * <p>고객이 교환품을 수령했음을 확인합니다.
     *
     * @param now 현재 시간 (테스트 가능성을 위해 외부에서 전달)
     * @throws ClaimShippingException 교환품이 발송되지 않은 경우
     */
    public void confirmExchangeDelivered(Instant now) {
        validateExchangeApplicable();
        validateRequired(now, "now");
        if (exchangeShippedAt == null) {
            throw ClaimShippingException.cannotRegisterExchangeShipping("교환품이 발송되지 않았습니다");
        }

        this.exchangeDeliveredAt = now;
        this.updatedAt = now;

        // 교환품 수령 시 클레임 완료 처리
        if (status.canComplete()) {
            complete(now);
        }
    }

    // ========== 반품 배송 검증 메서드 ==========

    private void validateReturnRequired() {
        if (!claimType.requiresReturn()) {
            throw ClaimShippingException.returnNotRequired(claimType.name());
        }
    }

    private void validateExchangeApplicable() {
        if (claimType != ClaimType.EXCHANGE) {
            throw ClaimShippingException.exchangeNotApplicable(claimType.name());
        }
    }

    // ========== 조회 메서드 ==========

    public Long id() {
        return id;
    }

    public ClaimId claimId() {
        return claimId;
    }

    public ClaimNumber claimNumber() {
        return claimNumber;
    }

    public String orderId() {
        return orderId;
    }

    public String orderItemId() {
        return orderItemId;
    }

    public ClaimType claimType() {
        return claimType;
    }

    public ClaimReason claimReason() {
        return claimReason;
    }

    public String claimReasonDetail() {
        return claimReasonDetail;
    }

    public Integer quantity() {
        return quantity;
    }

    public BigDecimal refundAmount() {
        return refundAmount;
    }

    public ClaimStatus status() {
        return status;
    }

    public String processedBy() {
        return processedBy;
    }

    public Instant processedAt() {
        return processedAt;
    }

    public String rejectReason() {
        return rejectReason;
    }

    public String returnTrackingNumber() {
        return returnTrackingNumber;
    }

    public String returnCarrier() {
        return returnCarrier;
    }

    public String exchangeTrackingNumber() {
        return exchangeTrackingNumber;
    }

    public String exchangeCarrier() {
        return exchangeCarrier;
    }

    public ReturnShippingMethod returnShippingMethod() {
        return returnShippingMethod;
    }

    public ReturnShippingStatus returnShippingStatus() {
        return returnShippingStatus;
    }

    public Instant returnPickupScheduledAt() {
        return returnPickupScheduledAt;
    }

    public String returnPickupAddress() {
        return returnPickupAddress;
    }

    public String returnCustomerPhone() {
        return returnCustomerPhone;
    }

    public Instant returnReceivedAt() {
        return returnReceivedAt;
    }

    public InspectionResult inspectionResult() {
        return inspectionResult;
    }

    public String inspectionNote() {
        return inspectionNote;
    }

    public Instant exchangeShippedAt() {
        return exchangeShippedAt;
    }

    public Instant exchangeDeliveredAt() {
        return exchangeDeliveredAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    // ========== 비즈니스 로직 ==========

    /**
     * 신규 클레임인지 확인 (아직 영속화되지 않음)
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * 전체 주문 클레임인지 확인
     *
     * @return orderItemId가 null이면 true (전체 주문)
     */
    public boolean isFullOrderClaim() {
        return orderItemId == null;
    }

    /**
     * 환불이 필요한 클레임인지 확인
     *
     * @return 취소/반품/부분환불이면 true
     */
    public boolean requiresRefund() {
        return claimType.requiresRefund();
    }

    /**
     * 반품 수거가 필요한 클레임인지 확인
     *
     * @return 반품/교환이면 true
     */
    public boolean requiresReturn() {
        return claimType.requiresReturn();
    }

    /**
     * 고객 귀책 클레임인지 확인
     *
     * @return 고객 귀책 사유이면 true
     */
    public boolean isCustomerFault() {
        return claimReason.isCustomerFault();
    }

    // ========== Validation ==========

    private static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        if (value instanceof String str && str.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }

    // ========== Object Methods ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return Objects.equals(claimId, claim.claimId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimId);
    }

    @Override
    public String toString() {
        return "Claim{"
                + "claimId="
                + claimId
                + ", claimNumber="
                + claimNumber
                + ", orderId='"
                + orderId
                + '\''
                + ", claimType="
                + claimType
                + ", status="
                + status
                + '}';
    }
}
