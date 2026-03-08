package com.ryuqq.setof.domain.exchange.aggregate;

import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.exchange.exception.InvalidExchangeStatusTransitionException;
import com.ryuqq.setof.domain.exchange.id.ExchangeId;
import com.ryuqq.setof.domain.exchange.vo.AmountAdjustment;
import com.ryuqq.setof.domain.exchange.vo.ExchangeReason;
import com.ryuqq.setof.domain.exchange.vo.ExchangeStatus;
import com.ryuqq.setof.domain.exchange.vo.ExchangeTarget;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;

/**
 * 교환 클레임 Aggregate Root.
 *
 * <p>교환 요청의 생명주기를 관리합니다. 기존 상품의 수거부터 교환 상품의 배송까지 전체 프로세스를 추적합니다.
 */
public class ExchangeClaim {

    private final ExchangeId id;
    private final LegacyOrderId orderId;
    private final SellerId sellerId;
    private ExchangeStatus exchangeStatus;
    private final ExchangeReason reason;
    private final List<ExchangeItem> exchangeItems;
    private final ExchangeTarget exchangeTarget;
    private final AmountAdjustment amountAdjustment;
    private ClaimShipmentId claimShipmentId;
    private LegacyOrderId linkedOrderId;
    private final Instant createdAt;
    private Instant updatedAt;

    private ExchangeClaim(
            ExchangeId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            ExchangeStatus exchangeStatus,
            ExchangeReason reason,
            List<ExchangeItem> exchangeItems,
            ExchangeTarget exchangeTarget,
            AmountAdjustment amountAdjustment,
            ClaimShipmentId claimShipmentId,
            LegacyOrderId linkedOrderId,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.exchangeStatus = exchangeStatus;
        this.reason = reason;
        this.exchangeItems = exchangeItems;
        this.exchangeTarget = exchangeTarget;
        this.amountAdjustment = amountAdjustment;
        this.claimShipmentId = claimShipmentId;
        this.linkedOrderId = linkedOrderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 교환 클레임 생성.
     *
     * @param orderId 주문 ID
     * @param sellerId 판매자 ID
     * @param reason 교환 사유
     * @param exchangeItems 교환 아이템 목록
     * @param exchangeTarget 교환 대상 상품 정보
     * @param amountAdjustment 금액 조정 정보
     * @param now 현재 시간
     * @return 요청됨 상태의 새 교환 클레임
     */
    public static ExchangeClaim forNew(
            LegacyOrderId orderId,
            SellerId sellerId,
            ExchangeReason reason,
            List<ExchangeItem> exchangeItems,
            ExchangeTarget exchangeTarget,
            AmountAdjustment amountAdjustment,
            Instant now) {
        return new ExchangeClaim(
                ExchangeId.forNew(),
                orderId,
                sellerId,
                ExchangeStatus.REQUESTED,
                reason,
                List.copyOf(exchangeItems),
                exchangeTarget,
                amountAdjustment,
                null,
                null,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 교환 ID
     * @param orderId 주문 ID
     * @param sellerId 판매자 ID
     * @param exchangeStatus 교환 상태
     * @param reason 교환 사유
     * @param exchangeItems 교환 아이템 목록
     * @param exchangeTarget 교환 대상 상품 정보
     * @param amountAdjustment 금액 조정 정보
     * @param claimShipmentId 클레임 배송 ID (nullable)
     * @param linkedOrderId 연결된 주문 ID (nullable)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 교환 클레임
     */
    public static ExchangeClaim reconstitute(
            ExchangeId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            ExchangeStatus exchangeStatus,
            ExchangeReason reason,
            List<ExchangeItem> exchangeItems,
            ExchangeTarget exchangeTarget,
            AmountAdjustment amountAdjustment,
            ClaimShipmentId claimShipmentId,
            LegacyOrderId linkedOrderId,
            Instant createdAt,
            Instant updatedAt) {
        return new ExchangeClaim(
                id,
                orderId,
                sellerId,
                exchangeStatus,
                reason,
                List.copyOf(exchangeItems),
                exchangeTarget,
                amountAdjustment,
                claimShipmentId,
                linkedOrderId,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 수거 시작.
     *
     * <p>REQUESTED -> COLLECTING 전이. 클레임 배송 ID를 설정합니다.
     *
     * @param claimShipmentId 클레임 배송 ID
     * @param now 현재 시간
     */
    public void startCollecting(ClaimShipmentId claimShipmentId, Instant now) {
        transitTo(ExchangeStatus.COLLECTING, now);
        this.claimShipmentId = claimShipmentId;
    }

    /**
     * 수거 완료.
     *
     * <p>COLLECTING -> COLLECTED 전이.
     *
     * @param now 현재 시간
     */
    public void completeCollection(Instant now) {
        transitTo(ExchangeStatus.COLLECTED, now);
    }

    /**
     * 교환 상품 준비 시작.
     *
     * <p>COLLECTED -> PREPARING 전이.
     *
     * @param now 현재 시간
     */
    public void startPreparing(Instant now) {
        transitTo(ExchangeStatus.PREPARING, now);
    }

    /**
     * 교환 상품 배송 시작.
     *
     * <p>PREPARING -> SHIPPING 전이. 연결된 주문 ID를 설정합니다.
     *
     * @param linkedOrderId 교환 상품 신규 주문 ID
     * @param now 현재 시간
     */
    public void startShipping(LegacyOrderId linkedOrderId, Instant now) {
        transitTo(ExchangeStatus.SHIPPING, now);
        this.linkedOrderId = linkedOrderId;
    }

    /**
     * 교환 완료.
     *
     * <p>SHIPPING -> COMPLETED 전이.
     *
     * @param now 현재 시간
     */
    public void complete(Instant now) {
        transitTo(ExchangeStatus.COMPLETED, now);
    }

    /**
     * 교환 거부.
     *
     * <p>REQUESTED, COLLECTING, COLLECTED -> REJECTED 전이.
     *
     * @param now 현재 시간
     */
    public void reject(Instant now) {
        transitTo(ExchangeStatus.REJECTED, now);
    }

    /**
     * 교환 철회.
     *
     * <p>REQUESTED -> CANCELLED 전이.
     *
     * @param now 현재 시간
     */
    public void cancel(Instant now) {
        transitTo(ExchangeStatus.CANCELLED, now);
    }

    private void transitTo(ExchangeStatus target, Instant now) {
        if (!this.exchangeStatus.canTransitionTo(target)) {
            throw new InvalidExchangeStatusTransitionException(this.exchangeStatus, target);
        }
        this.exchangeStatus = target;
        this.updatedAt = now;
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return 종료 상태 여부
     */
    public boolean isFinal() {
        return exchangeStatus.isFinal();
    }

    /**
     * 활성 상태인지 확인합니다.
     *
     * @return 활성 상태 여부
     */
    public boolean isActive() {
        return exchangeStatus.isActive();
    }

    /**
     * 수거 단계인지 확인합니다.
     *
     * @return 수거 단계 여부
     */
    public boolean isCollectionStep() {
        return exchangeStatus.isCollectionStep();
    }

    /**
     * 배송 단계인지 확인합니다.
     *
     * @return 배송 단계 여부
     */
    public boolean isShippingStep() {
        return exchangeStatus.isShippingStep();
    }

    /**
     * 연결된 주문이 있는지 확인합니다.
     *
     * @return 연결된 주문 존재 여부
     */
    public boolean hasLinkedOrder() {
        return linkedOrderId != null;
    }

    /**
     * 추가 결제가 필요한지 확인합니다.
     *
     * @return 추가 결제 필요 여부
     */
    public boolean requiresAdditionalPayment() {
        return amountAdjustment != null && amountAdjustment.requiresAdditionalPayment();
    }

    /**
     * 부분 환불이 필요한지 확인합니다.
     *
     * @return 부분 환불 필요 여부
     */
    public boolean requiresPartialRefund() {
        return amountAdjustment != null && amountAdjustment.requiresPartialRefund();
    }

    // VO Accessors
    public ExchangeId id() {
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

    public ExchangeStatus exchangeStatus() {
        return exchangeStatus;
    }

    public ExchangeReason reason() {
        return reason;
    }

    public List<ExchangeItem> exchangeItems() {
        return exchangeItems;
    }

    public ExchangeTarget exchangeTarget() {
        return exchangeTarget;
    }

    public AmountAdjustment amountAdjustment() {
        return amountAdjustment;
    }

    public ClaimShipmentId claimShipmentId() {
        return claimShipmentId;
    }

    public LegacyOrderId linkedOrderId() {
        return linkedOrderId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
