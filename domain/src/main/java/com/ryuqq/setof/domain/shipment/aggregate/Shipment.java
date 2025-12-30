package com.ryuqq.setof.domain.shipment.aggregate;

import com.ryuqq.setof.domain.shipment.exception.InvalidStatusTransitionException;
import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;
import com.ryuqq.setof.domain.shipment.vo.InvoiceNumber;
import com.ryuqq.setof.domain.shipment.vo.Sender;
import com.ryuqq.setof.domain.shipment.vo.ShipmentId;
import com.ryuqq.setof.domain.shipment.vo.ShipmentType;
import com.ryuqq.setof.domain.shipment.vo.TrackingInfo;
import java.time.Instant;

/**
 * Shipment Aggregate Root
 *
 * <p>운송장 정보를 나타내는 도메인 엔티티입니다.
 *
 * <p>Order와 N:1 관계를 가집니다. 같은 결제건(checkoutId)의 같은 셀러(sellerId) 주문들은 하나의 Shipment로 묶일 수 있습니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 금지
 * </ul>
 */
public class Shipment {

    private final ShipmentId id;
    private final Long sellerId;
    private final Long checkoutId;
    private final Long carrierId;
    private final InvoiceNumber invoiceNumber;
    private final Sender sender;
    private final ShipmentType type;
    private final DeliveryStatus status;
    private final TrackingInfo trackingInfo;
    private final Instant shippedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Shipment(
            ShipmentId id,
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            InvoiceNumber invoiceNumber,
            Sender sender,
            ShipmentType type,
            DeliveryStatus status,
            TrackingInfo trackingInfo,
            Instant shippedAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.checkoutId = checkoutId;
        this.carrierId = carrierId;
        this.invoiceNumber = invoiceNumber;
        this.sender = sender;
        this.type = type;
        this.status = status;
        this.trackingInfo = trackingInfo;
        this.shippedAt = shippedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 운송장 생성
     *
     * @param sellerId 셀러 ID
     * @param checkoutId 결제건 ID
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @param sender 발송인 정보
     * @param type 배송 유형
     * @param now 현재 시각
     * @return Shipment 인스턴스
     */
    public static Shipment forNew(
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            InvoiceNumber invoiceNumber,
            Sender sender,
            ShipmentType type,
            Instant now) {
        return new Shipment(
                null,
                sellerId,
                checkoutId,
                carrierId,
                invoiceNumber,
                sender,
                type,
                DeliveryStatus.defaultStatus(),
                TrackingInfo.empty(),
                null,
                now,
                now);
    }

    /**
     * 신규 운송장 생성 (기본 배송 유형)
     *
     * @param sellerId 셀러 ID
     * @param checkoutId 결제건 ID
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @param sender 발송인 정보
     * @param now 현재 시각
     * @return Shipment 인스턴스
     */
    public static Shipment forNew(
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            InvoiceNumber invoiceNumber,
            Sender sender,
            Instant now) {
        return forNew(
                sellerId,
                checkoutId,
                carrierId,
                invoiceNumber,
                sender,
                ShipmentType.defaultType(),
                now);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * @param id 운송장 ID
     * @param sellerId 셀러 ID
     * @param checkoutId 결제건 ID
     * @param carrierId 택배사 ID
     * @param invoiceNumber 운송장 번호
     * @param sender 발송인 정보
     * @param type 배송 유형
     * @param status 배송 상태
     * @param trackingInfo 추적 정보
     * @param shippedAt 발송 시각
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return Shipment 인스턴스
     */
    public static Shipment reconstitute(
            ShipmentId id,
            Long sellerId,
            Long checkoutId,
            Long carrierId,
            InvoiceNumber invoiceNumber,
            Sender sender,
            ShipmentType type,
            DeliveryStatus status,
            TrackingInfo trackingInfo,
            Instant shippedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new Shipment(
                id,
                sellerId,
                checkoutId,
                carrierId,
                invoiceNumber,
                sender,
                type,
                status,
                trackingInfo,
                shippedAt,
                createdAt,
                updatedAt);
    }

    // ========== 상태 변경 메서드 ==========

    /**
     * 배송 상태 변경
     *
     * @param newStatus 새 배송 상태
     * @param now 현재 시각
     * @return 상태 변경된 Shipment 인스턴스
     * @throws InvalidStatusTransitionException 상태 전이가 허용되지 않는 경우
     */
    public Shipment changeStatus(DeliveryStatus newStatus, Instant now) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(this.status.name(), newStatus.name());
        }

        Instant newShippedAt = this.shippedAt;
        TrackingInfo newTrackingInfo = this.trackingInfo;

        // IN_TRANSIT로 처음 변경될 때 발송 시각 기록
        if (newStatus == DeliveryStatus.IN_TRANSIT && this.shippedAt == null) {
            newShippedAt = now;
        }

        // DELIVERED로 변경될 때 배송 완료 시각 기록
        if (newStatus == DeliveryStatus.DELIVERED) {
            newTrackingInfo = this.trackingInfo.markDelivered(now);
        }

        return new Shipment(
                this.id,
                this.sellerId,
                this.checkoutId,
                this.carrierId,
                this.invoiceNumber,
                this.sender,
                this.type,
                newStatus,
                newTrackingInfo,
                newShippedAt,
                this.createdAt,
                now);
    }

    /**
     * 추적 정보 업데이트
     *
     * @param location 새 위치
     * @param message 새 메시지
     * @param trackedAt 추적 시각
     * @param now 현재 시각
     * @return 추적 정보가 업데이트된 Shipment 인스턴스
     */
    public Shipment updateTracking(
            String location, String message, Instant trackedAt, Instant now) {
        TrackingInfo newTrackingInfo =
                this.trackingInfo.updateTracking(location, message, trackedAt);

        return new Shipment(
                this.id,
                this.sellerId,
                this.checkoutId,
                this.carrierId,
                this.invoiceNumber,
                this.sender,
                this.type,
                this.status,
                newTrackingInfo,
                this.shippedAt,
                this.createdAt,
                now);
    }

    /**
     * 배송 완료 처리
     *
     * @param now 현재 시각
     * @return 배송 완료된 Shipment 인스턴스
     * @throws InvalidStatusTransitionException 상태 전이가 허용되지 않는 경우
     */
    public Shipment markDelivered(Instant now) {
        return changeStatus(DeliveryStatus.DELIVERED, now);
    }

    /**
     * 운송장 번호 변경
     *
     * <p>발송 전(PENDING 상태)에만 변경 가능합니다.
     *
     * @param newCarrierId 새 택배사 ID
     * @param newInvoiceNumber 새 운송장 번호
     * @param now 현재 시각
     * @return 운송장 번호가 변경된 Shipment 인스턴스
     * @throws InvalidStatusTransitionException 발송 후 변경 시도
     */
    public Shipment changeInvoice(Long newCarrierId, InvoiceNumber newInvoiceNumber, Instant now) {
        if (this.status != DeliveryStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    this.status.name(), "Cannot change invoice after shipping started");
        }

        return new Shipment(
                this.id,
                this.sellerId,
                this.checkoutId,
                newCarrierId,
                newInvoiceNumber,
                this.sender,
                this.type,
                this.status,
                TrackingInfo.empty(),
                null,
                this.createdAt,
                now);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 운송장 ID 값 반환
     *
     * @return 운송장 ID Long 값 (null if new)
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 운송장 번호 값 반환
     *
     * @return 운송장 번호 문자열
     */
    public String getInvoiceNumberValue() {
        return invoiceNumber.value();
    }

    /**
     * 발송인 이름 반환
     *
     * @return 발송인 이름
     */
    public String getSenderName() {
        return sender.name();
    }

    /**
     * 발송인 연락처 반환
     *
     * @return 발송인 연락처
     */
    public String getSenderPhone() {
        return sender.phone();
    }

    /**
     * 상태 이름 반환
     *
     * @return 상태 문자열
     */
    public String getStatusValue() {
        return status.name();
    }

    /**
     * 배송 유형 이름 반환
     *
     * @return 배송 유형 문자열
     */
    public String getTypeValue() {
        return type.name();
    }

    /**
     * 마지막 추적 위치 반환
     *
     * @return 마지막 위치 (null 가능)
     */
    public String getLastLocation() {
        return trackingInfo.lastLocation();
    }

    /**
     * 마지막 추적 메시지 반환
     *
     * @return 마지막 메시지 (null 가능)
     */
    public String getLastMessage() {
        return trackingInfo.lastMessage();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 배송 완료 여부 확인 (Tell, Don't Ask)
     *
     * @return 배송 완료 상태이면 true
     */
    public boolean isDelivered() {
        return status.isDelivered();
    }

    /**
     * 배송 진행 중 여부 확인
     *
     * @return 배송 진행 중이면 true
     */
    public boolean isInProgress() {
        return status.isInProgress();
    }

    /**
     * 발송 완료 여부 확인
     *
     * @return 발송되었으면 true
     */
    public boolean isShipped() {
        return shippedAt != null;
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 없으면 true (신규)
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * 같은 셀러의 같은 결제건인지 확인
     *
     * <p>Order와 Shipment 묶음 가능 여부 판단에 사용됩니다.
     *
     * @param otherSellerId 다른 셀러 ID
     * @param otherCheckoutId 다른 결제건 ID
     * @return 같은 셀러 + 같은 결제건이면 true
     */
    public boolean canBundleWith(Long otherSellerId, Long otherCheckoutId) {
        return this.sellerId.equals(otherSellerId) && this.checkoutId.equals(otherCheckoutId);
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ShipmentId getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getCheckoutId() {
        return checkoutId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public InvoiceNumber getInvoiceNumber() {
        return invoiceNumber;
    }

    public Sender getSender() {
        return sender;
    }

    public ShipmentType getType() {
        return type;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public TrackingInfo getTrackingInfo() {
        return trackingInfo;
    }

    public Instant getShippedAt() {
        return shippedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
