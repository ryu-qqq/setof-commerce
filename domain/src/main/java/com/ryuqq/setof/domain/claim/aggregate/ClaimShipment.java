package com.ryuqq.setof.domain.claim.aggregate;

import com.ryuqq.setof.domain.claim.exception.InvalidClaimShipmentStatusTransitionException;
import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.claim.vo.ClaimShipmentMethod;
import com.ryuqq.setof.domain.claim.vo.ClaimShipmentStatus;
import com.ryuqq.setof.domain.claim.vo.ContactInfo;
import com.ryuqq.setof.domain.claim.vo.ShippingFeeInfo;
import java.time.Instant;

/**
 * 클레임 배송(수거) Aggregate Root.
 *
 * <p>반품/교환 시 상품 수거의 생명주기를 관리합니다. 이 Aggregate는 Refund BC와 Exchange BC에서 공유됩니다. Cancel BC에서는 물리적 반송이
 * 없으므로 사용하지 않습니다.
 */
public class ClaimShipment {

    private final ClaimShipmentId id;
    private ClaimShipmentStatus shipmentStatus;
    private ClaimShipmentMethod shipmentMethod;
    private final ContactInfo senderContact;
    private final ShippingFeeInfo shippingFeeInfo;
    private final Instant createdAt;
    private Instant updatedAt;

    private ClaimShipment(
            ClaimShipmentId id,
            ClaimShipmentStatus shipmentStatus,
            ClaimShipmentMethod shipmentMethod,
            ContactInfo senderContact,
            ShippingFeeInfo shippingFeeInfo,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.shipmentStatus = shipmentStatus;
        this.shipmentMethod = shipmentMethod;
        this.senderContact = senderContact;
        this.shippingFeeInfo = shippingFeeInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 클레임 배송 생성. PENDING 상태로 시작합니다.
     *
     * @param shipmentMethod 수거 방법
     * @param senderContact 수거지 연락처
     * @param shippingFeeInfo 배송비 정보
     * @param now 현재 시간
     * @return 새 클레임 배송
     */
    public static ClaimShipment forNew(
            ClaimShipmentMethod shipmentMethod,
            ContactInfo senderContact,
            ShippingFeeInfo shippingFeeInfo,
            Instant now) {
        return new ClaimShipment(
                ClaimShipmentId.forNew(),
                ClaimShipmentStatus.PENDING,
                shipmentMethod,
                senderContact,
                shippingFeeInfo,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 클레임 배송 ID
     * @param shipmentStatus 배송 상태
     * @param shipmentMethod 수거 방법
     * @param senderContact 수거지 연락처
     * @param shippingFeeInfo 배송비 정보
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 클레임 배송
     */
    public static ClaimShipment reconstitute(
            ClaimShipmentId id,
            ClaimShipmentStatus shipmentStatus,
            ClaimShipmentMethod shipmentMethod,
            ContactInfo senderContact,
            ShippingFeeInfo shippingFeeInfo,
            Instant createdAt,
            Instant updatedAt) {
        return new ClaimShipment(
                id,
                shipmentStatus,
                shipmentMethod,
                senderContact,
                shippingFeeInfo,
                createdAt,
                updatedAt);
    }

    /**
     * 새로 생성된 클레임 배송인지 확인합니다.
     *
     * @return 새 클레임 배송 여부
     */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 수거 진행 시작. PENDING -> IN_TRANSIT 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void startTransit(Instant now) {
        transitTo(ClaimShipmentStatus.IN_TRANSIT, now);
    }

    /**
     * 수거 완료. IN_TRANSIT -> DELIVERED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void complete(Instant now) {
        transitTo(ClaimShipmentStatus.DELIVERED, now);
    }

    /**
     * 수거 실패. PENDING 또는 IN_TRANSIT -> FAILED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void fail(Instant now) {
        transitTo(ClaimShipmentStatus.FAILED, now);
    }

    /**
     * 수거 재시도. FAILED -> PENDING 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void retry(Instant now) {
        transitTo(ClaimShipmentStatus.PENDING, now);
    }

    /**
     * 운송장 정보를 업데이트합니다.
     *
     * @param carrierCode 택배사 코드
     * @param trackingNumber 운송장 번호
     * @param now 현재 시간
     */
    public void updateTrackingInfo(String carrierCode, String trackingNumber, Instant now) {
        this.shipmentMethod =
                ClaimShipmentMethod.of(this.shipmentMethod.type(), carrierCode, trackingNumber);
        this.updatedAt = now;
    }

    private void transitTo(ClaimShipmentStatus target, Instant now) {
        if (!this.shipmentStatus.canTransitionTo(target)) {
            throw new InvalidClaimShipmentStatusTransitionException(this.shipmentStatus, target);
        }
        this.shipmentStatus = target;
        this.updatedAt = now;
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return shipmentStatus.isFinal();
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return shipmentStatus.isActive();
    }

    /**
     * 수거 완료 상태인지 확인합니다.
     *
     * @return 수거 완료 여부
     */
    public boolean isCompleted() {
        return shipmentStatus.isCompleted();
    }

    // Accessors
    public ClaimShipmentId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ClaimShipmentStatus shipmentStatus() {
        return shipmentStatus;
    }

    public ClaimShipmentMethod shipmentMethod() {
        return shipmentMethod;
    }

    public ContactInfo senderContact() {
        return senderContact;
    }

    public ShippingFeeInfo shippingFeeInfo() {
        return shippingFeeInfo;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
