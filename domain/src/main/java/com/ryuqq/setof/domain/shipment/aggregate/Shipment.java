package com.ryuqq.setof.domain.shipment.aggregate;

import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.shipment.exception.InvalidDeliveryStatusTransitionException;
import com.ryuqq.setof.domain.shipment.id.ShipmentId;
import com.ryuqq.setof.domain.shipment.vo.DeliveryStatus;
import com.ryuqq.setof.domain.shipment.vo.SenderInfo;
import com.ryuqq.setof.domain.shipment.vo.ShipmentType;
import com.ryuqq.setof.domain.shipment.vo.TrackingInfo;
import java.time.Instant;

/** 배송 Aggregate Root. */
public class Shipment {

    private final ShipmentId id;
    private final OrderItemId orderItemId;
    private final ShipmentType shipmentType;
    private DeliveryStatus deliveryStatus;
    private final SenderInfo senderInfo;
    private TrackingInfo trackingInfo;
    private final Instant createdAt;
    private Instant updatedAt;

    private Shipment(
            ShipmentId id,
            OrderItemId orderItemId,
            ShipmentType shipmentType,
            DeliveryStatus deliveryStatus,
            SenderInfo senderInfo,
            TrackingInfo trackingInfo,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.shipmentType = shipmentType;
        this.deliveryStatus = deliveryStatus;
        this.senderInfo = senderInfo;
        this.trackingInfo = trackingInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 배송 생성. PENDING 상태로 시작합니다.
     *
     * @param orderItemId 주문 아이템 ID
     * @param shipmentType 배송 유형
     * @param senderInfo 발송인 정보
     * @param now 현재 시간
     * @return 대기 상태의 새 배송
     */
    public static Shipment forNew(
            OrderItemId orderItemId,
            ShipmentType shipmentType,
            SenderInfo senderInfo,
            Instant now) {
        return new Shipment(
                ShipmentId.forNew(),
                orderItemId,
                shipmentType,
                DeliveryStatus.PENDING,
                senderInfo,
                null,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 배송 ID
     * @param orderItemId 주문 아이템 ID
     * @param shipmentType 배송 유형
     * @param deliveryStatus 배송 상태
     * @param senderInfo 발송인 정보
     * @param trackingInfo 배송 추적 정보 (nullable)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 배송
     */
    public static Shipment reconstitute(
            ShipmentId id,
            OrderItemId orderItemId,
            ShipmentType shipmentType,
            DeliveryStatus deliveryStatus,
            SenderInfo senderInfo,
            TrackingInfo trackingInfo,
            Instant createdAt,
            Instant updatedAt) {
        return new Shipment(
                id,
                orderItemId,
                shipmentType,
                deliveryStatus,
                senderInfo,
                trackingInfo,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 배송 시작. PENDING -> PROCESSING 상태로 전이하며 추적 정보를 설정합니다.
     *
     * @param trackingInfo 배송 추적 정보
     * @param now 현재 시간
     */
    public void startDelivery(TrackingInfo trackingInfo, Instant now) {
        transitTo(DeliveryStatus.PROCESSING, now);
        this.trackingInfo = trackingInfo;
    }

    /**
     * 배송 완료. PROCESSING -> COMPLETED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void completeDelivery(Instant now) {
        transitTo(DeliveryStatus.COMPLETED, now);
    }

    /**
     * 반송 요청. COMPLETED -> RETURN_REQUESTED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void requestReturn(Instant now) {
        transitTo(DeliveryStatus.RETURN_REQUESTED, now);
    }

    /**
     * 반송 배송 시작. RETURN_REQUESTED -> RETURN_PROCESSING 상태로 전이하며 반송 추적 정보를 설정합니다.
     *
     * @param returnTracking 반송 배송 추적 정보
     * @param now 현재 시간
     */
    public void startReturnDelivery(TrackingInfo returnTracking, Instant now) {
        transitTo(DeliveryStatus.RETURN_PROCESSING, now);
        this.trackingInfo = returnTracking;
    }

    /**
     * 반송 완료. RETURN_PROCESSING -> RETURN_COMPLETED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void completeReturn(Instant now) {
        transitTo(DeliveryStatus.RETURN_COMPLETED, now);
    }

    private void transitTo(DeliveryStatus target, Instant now) {
        if (!this.deliveryStatus.canTransitionTo(target)) {
            throw new InvalidDeliveryStatusTransitionException(this.deliveryStatus, target);
        }
        this.deliveryStatus = target;
        this.updatedAt = now;
    }

    /**
     * 배송 완료 상태인지 확인합니다.
     *
     * @return 배송 완료 여부
     */
    public boolean isDelivered() {
        return deliveryStatus.isCompleted();
    }

    /**
     * 반송 단계 상태인지 확인합니다.
     *
     * @return 반송 단계 여부
     */
    public boolean isReturnPhase() {
        return deliveryStatus.isReturnPhase();
    }

    // VO Getters
    public ShipmentId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public OrderItemId orderItemId() {
        return orderItemId;
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public ShipmentType shipmentType() {
        return shipmentType;
    }

    public DeliveryStatus deliveryStatus() {
        return deliveryStatus;
    }

    public SenderInfo senderInfo() {
        return senderInfo;
    }

    public TrackingInfo trackingInfo() {
        return trackingInfo;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
