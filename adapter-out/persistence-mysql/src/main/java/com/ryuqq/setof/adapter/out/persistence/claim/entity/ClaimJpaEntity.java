package com.ryuqq.setof.adapter.out.persistence.claim.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * ClaimJpaEntity - 클레임 JPA Entity
 *
 * <p>claim 테이블과 매핑되는 JPA Entity입니다.
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Entity
@Table(
        name = "claim",
        indexes = {
            @Index(name = "idx_claim_order_id", columnList = "order_id"),
            @Index(name = "idx_claim_status", columnList = "status"),
            @Index(name = "idx_claim_type", columnList = "claim_type")
        })
public class ClaimJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "claim_id", nullable = false, length = 36, unique = true)
    private String claimId;

    @Column(name = "claim_number", nullable = false, length = 30, unique = true)
    private String claimNumber;

    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;

    @Column(name = "order_item_id", length = 36)
    private String orderItemId;

    @Column(name = "claim_type", nullable = false, length = 20)
    private String claimType;

    @Column(name = "claim_reason", nullable = false, length = 50)
    private String claimReason;

    @Column(name = "claim_reason_detail", length = 500)
    private String claimReasonDetail;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "refund_amount", precision = 15, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "processed_by", length = 36)
    private String processedBy;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "return_tracking_number", length = 50)
    private String returnTrackingNumber;

    @Column(name = "return_carrier", length = 20)
    private String returnCarrier;

    @Column(name = "exchange_tracking_number", length = 50)
    private String exchangeTrackingNumber;

    @Column(name = "exchange_carrier", length = 20)
    private String exchangeCarrier;

    // ========== 반품 배송 관련 필드 ==========

    @Column(name = "return_shipping_method", length = 30)
    private String returnShippingMethod;

    @Column(name = "return_shipping_status", length = 30)
    private String returnShippingStatus;

    @Column(name = "return_pickup_scheduled_at")
    private Instant returnPickupScheduledAt;

    @Column(name = "return_pickup_address", length = 500)
    private String returnPickupAddress;

    @Column(name = "return_customer_phone", length = 20)
    private String returnCustomerPhone;

    @Column(name = "return_received_at")
    private Instant returnReceivedAt;

    @Column(name = "inspection_result", length = 20)
    private String inspectionResult;

    @Column(name = "inspection_note", length = 500)
    private String inspectionNote;

    @Column(name = "exchange_shipped_at")
    private Instant exchangeShippedAt;

    @Column(name = "exchange_delivered_at")
    private Instant exchangeDeliveredAt;

    protected ClaimJpaEntity() {
        // JPA 기본 생성자
    }

    private ClaimJpaEntity(
            Long id,
            String claimId,
            String claimNumber,
            String orderId,
            String orderItemId,
            String claimType,
            String claimReason,
            String claimReasonDetail,
            Integer quantity,
            BigDecimal refundAmount,
            String status,
            String processedBy,
            Instant processedAt,
            String rejectReason,
            String returnTrackingNumber,
            String returnCarrier,
            String exchangeTrackingNumber,
            String exchangeCarrier,
            Instant createdAt,
            Instant updatedAt,
            String returnShippingMethod,
            String returnShippingStatus,
            Instant returnPickupScheduledAt,
            String returnPickupAddress,
            String returnCustomerPhone,
            Instant returnReceivedAt,
            String inspectionResult,
            String inspectionNote,
            Instant exchangeShippedAt,
            Instant exchangeDeliveredAt) {
        super(createdAt, updatedAt);
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

    public static ClaimJpaEntity of(
            Long id,
            String claimId,
            String claimNumber,
            String orderId,
            String orderItemId,
            String claimType,
            String claimReason,
            String claimReasonDetail,
            Integer quantity,
            BigDecimal refundAmount,
            String status,
            String processedBy,
            Instant processedAt,
            String rejectReason,
            String returnTrackingNumber,
            String returnCarrier,
            String exchangeTrackingNumber,
            String exchangeCarrier,
            Instant createdAt,
            Instant updatedAt,
            String returnShippingMethod,
            String returnShippingStatus,
            Instant returnPickupScheduledAt,
            String returnPickupAddress,
            String returnCustomerPhone,
            Instant returnReceivedAt,
            String inspectionResult,
            String inspectionNote,
            Instant exchangeShippedAt,
            Instant exchangeDeliveredAt) {
        return new ClaimJpaEntity(
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

    public Long getId() {
        return id;
    }

    public String getClaimId() {
        return claimId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public String getClaimType() {
        return claimType;
    }

    public String getClaimReason() {
        return claimReason;
    }

    public String getClaimReasonDetail() {
        return claimReasonDetail;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public String getReturnTrackingNumber() {
        return returnTrackingNumber;
    }

    public String getReturnCarrier() {
        return returnCarrier;
    }

    public String getExchangeTrackingNumber() {
        return exchangeTrackingNumber;
    }

    public String getExchangeCarrier() {
        return exchangeCarrier;
    }

    public String getReturnShippingMethod() {
        return returnShippingMethod;
    }

    public String getReturnShippingStatus() {
        return returnShippingStatus;
    }

    public Instant getReturnPickupScheduledAt() {
        return returnPickupScheduledAt;
    }

    public String getReturnPickupAddress() {
        return returnPickupAddress;
    }

    public String getReturnCustomerPhone() {
        return returnCustomerPhone;
    }

    public Instant getReturnReceivedAt() {
        return returnReceivedAt;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public String getInspectionNote() {
        return inspectionNote;
    }

    public Instant getExchangeShippedAt() {
        return exchangeShippedAt;
    }

    public Instant getExchangeDeliveredAt() {
        return exchangeDeliveredAt;
    }

    // ========== Update Methods ==========

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateProcessInfo(String processedBy, Instant processedAt, String rejectReason) {
        this.processedBy = processedBy;
        this.processedAt = processedAt;
        this.rejectReason = rejectReason;
    }

    public void updateReturnShipping(String trackingNumber, String carrier) {
        this.returnTrackingNumber = trackingNumber;
        this.returnCarrier = carrier;
    }

    public void updateExchangeShipping(String trackingNumber, String carrier) {
        this.exchangeTrackingNumber = trackingNumber;
        this.exchangeCarrier = carrier;
    }

    public void updateReturnPickupSchedule(
            String shippingMethod,
            String shippingStatus,
            Instant scheduledAt,
            String address,
            String phone) {
        this.returnShippingMethod = shippingMethod;
        this.returnShippingStatus = shippingStatus;
        this.returnPickupScheduledAt = scheduledAt;
        this.returnPickupAddress = address;
        this.returnCustomerPhone = phone;
    }

    public void updateReturnShippingInfo(
            String shippingMethod, String shippingStatus, String trackingNumber, String carrier) {
        this.returnShippingMethod = shippingMethod;
        this.returnShippingStatus = shippingStatus;
        this.returnTrackingNumber = trackingNumber;
        this.returnCarrier = carrier;
    }

    public void updateReturnShippingStatus(String shippingStatus, String trackingNumber) {
        this.returnShippingStatus = shippingStatus;
        if (trackingNumber != null) {
            this.returnTrackingNumber = trackingNumber;
        }
    }

    public void updateReturnReceived(
            String shippingStatus,
            Instant receivedAt,
            String inspectionResult,
            String inspectionNote) {
        this.returnShippingStatus = shippingStatus;
        this.returnReceivedAt = receivedAt;
        this.inspectionResult = inspectionResult;
        this.inspectionNote = inspectionNote;
    }

    public void updateExchangeShipped(String trackingNumber, String carrier, Instant shippedAt) {
        this.exchangeTrackingNumber = trackingNumber;
        this.exchangeCarrier = carrier;
        this.exchangeShippedAt = shippedAt;
    }

    public void updateExchangeDelivered(Instant deliveredAt) {
        this.exchangeDeliveredAt = deliveredAt;
    }
}
