package com.ryuqq.setof.domain.refund.aggregate;

import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.refund.exception.InvalidRefundStatusTransitionException;
import com.ryuqq.setof.domain.refund.exception.RefundOnHoldException;
import com.ryuqq.setof.domain.refund.id.RefundId;
import com.ryuqq.setof.domain.refund.vo.HoldInfo;
import com.ryuqq.setof.domain.refund.vo.RefundInfo;
import com.ryuqq.setof.domain.refund.vo.RefundReason;
import com.ryuqq.setof.domain.refund.vo.RefundStatus;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;

/**
 * 반품 Aggregate Root.
 *
 * <p>반품 요청부터 수거, 완료까지의 반품 프로세스를 관리합니다.
 *
 * <p><strong>상태 전이:</strong>
 *
 * <ul>
 *   <li>REQUESTED → COLLECTING → COLLECTED → COMPLETED
 *   <li>REQUESTED, COLLECTING, COLLECTED → REJECTED
 *   <li>REQUESTED → CANCELLED
 * </ul>
 */
public class RefundClaim {

    private final RefundId id;
    private final LegacyOrderId orderId;
    private final SellerId sellerId;
    private RefundStatus refundStatus;
    private final RefundReason reason;
    private final List<RefundItem> refundItems;
    private RefundInfo refundInfo;
    private ClaimShipmentId claimShipmentId;
    private HoldInfo holdInfo;
    private boolean held;
    private final Instant createdAt;
    private Instant updatedAt;

    private RefundClaim(
            RefundId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            RefundStatus refundStatus,
            RefundReason reason,
            List<RefundItem> refundItems,
            RefundInfo refundInfo,
            ClaimShipmentId claimShipmentId,
            HoldInfo holdInfo,
            boolean held,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.refundStatus = refundStatus;
        this.reason = reason;
        this.refundItems = List.copyOf(refundItems);
        this.refundInfo = refundInfo;
        this.claimShipmentId = claimShipmentId;
        this.holdInfo = holdInfo;
        this.held = held;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 반품 요청 생성.
     *
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param reason 반품 사유
     * @param refundItems 반품 아이템 목록
     * @param now 현재 시간
     * @return REQUESTED 상태의 새 반품
     */
    public static RefundClaim forNew(
            LegacyOrderId orderId,
            SellerId sellerId,
            RefundReason reason,
            List<RefundItem> refundItems,
            Instant now) {
        return new RefundClaim(
                RefundId.forNew(),
                orderId,
                sellerId,
                RefundStatus.REQUESTED,
                reason,
                refundItems,
                null,
                null,
                null,
                false,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 반품 ID
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param refundStatus 반품 상태
     * @param reason 반품 사유
     * @param refundItems 반품 아이템 목록
     * @param refundInfo 환불 금액 상세 (nullable)
     * @param claimShipmentId 클레임 배송 ID (nullable)
     * @param holdInfo 보류 정보 (nullable)
     * @param held 보류 여부
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 반품
     */
    public static RefundClaim reconstitute(
            RefundId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            RefundStatus refundStatus,
            RefundReason reason,
            List<RefundItem> refundItems,
            RefundInfo refundInfo,
            ClaimShipmentId claimShipmentId,
            HoldInfo holdInfo,
            boolean held,
            Instant createdAt,
            Instant updatedAt) {
        return new RefundClaim(
                id,
                orderId,
                sellerId,
                refundStatus,
                reason,
                refundItems,
                refundInfo,
                claimShipmentId,
                holdInfo,
                held,
                createdAt,
                updatedAt);
    }

    /**
     * 새로 생성된 반품인지 확인합니다.
     *
     * @return 새 반품 여부
     */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 수거 시작.
     *
     * <p>REQUESTED → COLLECTING 상태로 전이하고 클레임 배송 ID를 설정합니다.
     *
     * @param claimShipmentId 클레임 배송 ID
     * @param now 현재 시간
     */
    public void startCollecting(ClaimShipmentId claimShipmentId, Instant now) {
        validateNotHeld();
        transitTo(RefundStatus.COLLECTING, now);
        this.claimShipmentId = claimShipmentId;
    }

    /**
     * 수거 완료.
     *
     * <p>COLLECTING → COLLECTED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void completeCollection(Instant now) {
        validateNotHeld();
        transitTo(RefundStatus.COLLECTED, now);
    }

    /**
     * 반품 완료 (환불 처리).
     *
     * <p>COLLECTED → COMPLETED 상태로 전이하고 환불 금액 상세를 설정합니다.
     *
     * @param refundInfo 환불 금액 상세
     * @param now 현재 시간
     */
    public void complete(RefundInfo refundInfo, Instant now) {
        validateNotHeld();
        transitTo(RefundStatus.COMPLETED, now);
        this.refundInfo = refundInfo;
    }

    /**
     * 반품 거부.
     *
     * <p>REQUESTED, COLLECTING, COLLECTED → REJECTED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void reject(Instant now) {
        transitTo(RefundStatus.REJECTED, now);
    }

    /**
     * 반품 철회 (구매자).
     *
     * <p>REQUESTED → CANCELLED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void cancel(Instant now) {
        transitTo(RefundStatus.CANCELLED, now);
    }

    /**
     * 반품 보류 처리.
     *
     * <p>수거 단계(COLLECTING, COLLECTED)에서 보류할 수 있습니다.
     *
     * @param holdInfo 보류 정보
     */
    public void hold(HoldInfo holdInfo) {
        this.holdInfo = holdInfo;
        this.held = true;
    }

    /**
     * 보류 해제.
     *
     * @param now 현재 시간
     */
    public void releaseHold(Instant now) {
        this.held = false;
        this.holdInfo = null;
        this.updatedAt = now;
    }

    private void transitTo(RefundStatus target, Instant now) {
        if (!this.refundStatus.canTransitionTo(target)) {
            throw new InvalidRefundStatusTransitionException(this.refundStatus, target);
        }
        this.refundStatus = target;
        this.updatedAt = now;
    }

    private void validateNotHeld() {
        if (this.held) {
            throw new RefundOnHoldException();
        }
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return refundStatus.isFinal();
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return refundStatus.isActive();
    }

    /**
     * 보류 중인지 확인합니다.
     *
     * @return 보류 여부
     */
    public boolean isHeld() {
        return held;
    }

    /**
     * 수거 단계인지 확인합니다.
     *
     * @return 수거 단계 여부
     */
    public boolean isCollectionStep() {
        return refundStatus.isCollectionStep();
    }

    /**
     * 반품 아이템의 총 환불 예상 금액을 계산합니다.
     *
     * @return 총 환불 예상 금액
     */
    public Money totalRefundAmount() {
        return refundItems.stream()
                .map(item -> item.itemAmount().multiply(item.quantity()))
                .reduce(Money.zero(), Money::add);
    }

    // VO Accessors
    public RefundId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public LegacyOrderId orderId() {
        return orderId;
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public SellerId sellerId() {
        return sellerId;
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }

    public RefundStatus refundStatus() {
        return refundStatus;
    }

    public RefundReason reason() {
        return reason;
    }

    public List<RefundItem> refundItems() {
        return refundItems;
    }

    public RefundInfo refundInfo() {
        return refundInfo;
    }

    public ClaimShipmentId claimShipmentId() {
        return claimShipmentId;
    }

    public HoldInfo holdInfo() {
        return holdInfo;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
