package com.ryuqq.setof.domain.cancel.aggregate;

import com.ryuqq.setof.domain.cancel.exception.InvalidCancelStatusTransitionException;
import com.ryuqq.setof.domain.cancel.id.CancelId;
import com.ryuqq.setof.domain.cancel.vo.CancelReason;
import com.ryuqq.setof.domain.cancel.vo.CancelRefundInfo;
import com.ryuqq.setof.domain.cancel.vo.CancelStatus;
import com.ryuqq.setof.domain.cancel.vo.CancelType;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;

/**
 * 취소 Aggregate Root.
 *
 * <p>주문 취소의 생명주기를 관리합니다. 구매자 취소는 REQUESTED 상태로 시작하고, 판매자 취소는 자동 승인되어 APPROVED 상태로 시작합니다.
 */
public class Cancel {

    private final CancelId id;
    private final LegacyOrderId orderId;
    private final SellerId sellerId;
    private final CancelType cancelType;
    private CancelStatus cancelStatus;
    private final CancelReason reason;
    private final List<CancelItem> cancelItems;
    private final Money totalCancelAmount;
    private CancelRefundInfo refundInfo;
    private final Instant createdAt;
    private Instant updatedAt;

    private Cancel(
            CancelId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            CancelType cancelType,
            CancelStatus cancelStatus,
            CancelReason reason,
            List<CancelItem> cancelItems,
            Money totalCancelAmount,
            CancelRefundInfo refundInfo,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.cancelType = cancelType;
        this.cancelStatus = cancelStatus;
        this.reason = reason;
        this.cancelItems = cancelItems;
        this.totalCancelAmount = totalCancelAmount;
        this.refundInfo = refundInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 취소 생성.
     *
     * <p>구매자 취소(BUYER_CANCEL)는 REQUESTED 상태로 시작합니다. 판매자 취소(SELLER_CANCEL)는 자동 승인되어 APPROVED 상태로
     * 시작합니다.
     *
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param cancelType 취소 유형
     * @param reason 취소 사유
     * @param cancelItems 취소 아이템 목록
     * @param totalCancelAmount 총 취소 금액
     * @param now 현재 시간
     * @return 새 취소
     */
    public static Cancel forNew(
            LegacyOrderId orderId,
            SellerId sellerId,
            CancelType cancelType,
            CancelReason reason,
            List<CancelItem> cancelItems,
            Money totalCancelAmount,
            Instant now) {
        CancelStatus initialStatus =
                cancelType.isSellerCancel() ? CancelStatus.APPROVED : CancelStatus.REQUESTED;
        return new Cancel(
                CancelId.forNew(),
                orderId,
                sellerId,
                cancelType,
                initialStatus,
                reason,
                List.copyOf(cancelItems),
                totalCancelAmount,
                null,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 취소 ID
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param cancelType 취소 유형
     * @param cancelStatus 취소 상태
     * @param reason 취소 사유
     * @param cancelItems 취소 아이템 목록
     * @param totalCancelAmount 총 취소 금액
     * @param refundInfo 환불 정보 (nullable)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 취소
     */
    public static Cancel reconstitute(
            CancelId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            CancelType cancelType,
            CancelStatus cancelStatus,
            CancelReason reason,
            List<CancelItem> cancelItems,
            Money totalCancelAmount,
            CancelRefundInfo refundInfo,
            Instant createdAt,
            Instant updatedAt) {
        return new Cancel(
                id,
                orderId,
                sellerId,
                cancelType,
                cancelStatus,
                reason,
                List.copyOf(cancelItems),
                totalCancelAmount,
                refundInfo,
                createdAt,
                updatedAt);
    }

    /**
     * 새로 생성된 취소인지 확인합니다.
     *
     * @return 새 취소 여부
     */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 취소 승인. REQUESTED -> APPROVED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void approve(Instant now) {
        transitTo(CancelStatus.APPROVED, now);
    }

    /**
     * 취소 거부. REQUESTED -> REJECTED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void reject(Instant now) {
        transitTo(CancelStatus.REJECTED, now);
    }

    /**
     * 취소 철회. REQUESTED -> CANCELLED 상태로 전이합니다.
     *
     * @param now 현재 시간
     */
    public void cancel(Instant now) {
        transitTo(CancelStatus.CANCELLED, now);
    }

    /**
     * 취소 완료. APPROVED -> COMPLETED 상태로 전이하며 환불 정보를 설정합니다.
     *
     * @param refundInfo 환불 정보
     * @param now 현재 시간
     */
    public void complete(CancelRefundInfo refundInfo, Instant now) {
        transitTo(CancelStatus.COMPLETED, now);
        this.refundInfo = refundInfo;
    }

    private void transitTo(CancelStatus target, Instant now) {
        if (!this.cancelStatus.canTransitionTo(target)) {
            throw new InvalidCancelStatusTransitionException(this.cancelStatus, target);
        }
        this.cancelStatus = target;
        this.updatedAt = now;
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return cancelStatus.isFinal();
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return cancelStatus.isActive();
    }

    /**
     * 구매자 취소인지 확인합니다.
     *
     * @return 구매자 취소 여부
     */
    public boolean isBuyerCancel() {
        return cancelType.isBuyerCancel();
    }

    /**
     * 판매자 취소인지 확인합니다.
     *
     * @return 판매자 취소 여부
     */
    public boolean isSellerCancel() {
        return cancelType.isSellerCancel();
    }

    // Accessors
    public CancelId id() {
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

    public CancelType cancelType() {
        return cancelType;
    }

    public CancelStatus cancelStatus() {
        return cancelStatus;
    }

    public CancelReason reason() {
        return reason;
    }

    public List<CancelItem> cancelItems() {
        return cancelItems;
    }

    public Money totalCancelAmount() {
        return totalCancelAmount;
    }

    public int totalCancelAmountValue() {
        return totalCancelAmount.value();
    }

    public CancelRefundInfo refundInfo() {
        return refundInfo;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
