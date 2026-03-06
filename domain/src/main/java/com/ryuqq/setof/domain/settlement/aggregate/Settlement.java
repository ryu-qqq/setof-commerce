package com.ryuqq.setof.domain.settlement.aggregate;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.settlement.exception.SettlementErrorCode;
import com.ryuqq.setof.domain.settlement.exception.SettlementException;
import com.ryuqq.setof.domain.settlement.id.SettlementId;
import com.ryuqq.setof.domain.settlement.vo.CommissionRate;
import com.ryuqq.setof.domain.settlement.vo.DiscountBurdenInfo;
import com.ryuqq.setof.domain.settlement.vo.MileageBurdenInfo;
import com.ryuqq.setof.domain.settlement.vo.SettlementStatus;
import java.time.Instant;

/** 정산 Aggregate Root. */
public class Settlement {

    private final SettlementId id;
    private final OrderItemId orderItemId;
    private final SellerId sellerId;
    private final CommissionRate commissionRate;
    private final Money settlementAmount;
    private final DiscountBurdenInfo discountBurden;
    private final MileageBurdenInfo mileageBurden;
    private SettlementStatus status;
    private final Instant expectedDate;
    private Instant settledAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private Settlement(
            SettlementId id,
            OrderItemId orderItemId,
            SellerId sellerId,
            CommissionRate commissionRate,
            Money settlementAmount,
            DiscountBurdenInfo discountBurden,
            MileageBurdenInfo mileageBurden,
            SettlementStatus status,
            Instant expectedDate,
            Instant settledAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.sellerId = sellerId;
        this.commissionRate = commissionRate;
        this.settlementAmount = settlementAmount;
        this.discountBurden = discountBurden;
        this.mileageBurden = mileageBurden;
        this.status = status;
        this.expectedDate = expectedDate;
        this.settledAt = settledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 정산 생성.
     *
     * @param orderItemId 주문 아이템 ID
     * @param sellerId 셀러 ID
     * @param commissionRate 수수료율
     * @param settlementAmount 정산 금액
     * @param discountBurden 할인 부담 정보
     * @param mileageBurden 마일리지 부담 정보
     * @param expectedDate 예상 정산일
     * @param now 현재 시간
     * @return 대기 상태의 새 정산
     */
    public static Settlement forNew(
            OrderItemId orderItemId,
            SellerId sellerId,
            CommissionRate commissionRate,
            Money settlementAmount,
            DiscountBurdenInfo discountBurden,
            MileageBurdenInfo mileageBurden,
            Instant expectedDate,
            Instant now) {
        return new Settlement(
                SettlementId.forNew(),
                orderItemId,
                sellerId,
                commissionRate,
                settlementAmount,
                discountBurden,
                mileageBurden,
                SettlementStatus.PENDING,
                expectedDate,
                null,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 정산 ID
     * @param orderItemId 주문 아이템 ID
     * @param sellerId 셀러 ID
     * @param commissionRate 수수료율
     * @param settlementAmount 정산 금액
     * @param discountBurden 할인 부담 정보
     * @param mileageBurden 마일리지 부담 정보
     * @param status 정산 상태
     * @param expectedDate 예상 정산일
     * @param settledAt 정산 완료 시각
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 정산
     */
    public static Settlement reconstitute(
            SettlementId id,
            OrderItemId orderItemId,
            SellerId sellerId,
            CommissionRate commissionRate,
            Money settlementAmount,
            DiscountBurdenInfo discountBurden,
            MileageBurdenInfo mileageBurden,
            SettlementStatus status,
            Instant expectedDate,
            Instant settledAt,
            Instant createdAt,
            Instant updatedAt) {
        return new Settlement(
                id,
                orderItemId,
                sellerId,
                commissionRate,
                settlementAmount,
                discountBurden,
                mileageBurden,
                status,
                expectedDate,
                settledAt,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 정산 처리 시작.
     *
     * @param now 현재 시간
     */
    public void startProcessing(Instant now) {
        transitTo(SettlementStatus.PROCESSING, now);
    }

    /**
     * 정산 완료.
     *
     * @param settledAt 정산 완료 시각
     */
    public void complete(Instant settledAt) {
        transitTo(SettlementStatus.COMPLETED, settledAt);
        this.settledAt = settledAt;
    }

    private void transitTo(SettlementStatus target, Instant now) {
        if (!this.status.canTransitionTo(target)) {
            throw new SettlementException(
                    SettlementErrorCode.INVALID_SETTLEMENT_STATUS_TRANSITION,
                    String.format(
                            "%s 상태에서 %s 상태로 전이할 수 없습니다",
                            this.status.getDescription(), target.getDescription()));
        }
        this.status = target;
        this.updatedAt = now;
    }

    /**
     * 정산이 완료된 상태인지 확인합니다.
     *
     * @return 완료 여부
     */
    public boolean isCompleted() {
        return status.isCompleted();
    }

    // VO Getters
    public SettlementId id() {
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

    public SellerId sellerId() {
        return sellerId;
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }

    public CommissionRate commissionRate() {
        return commissionRate;
    }

    public double commissionRateValue() {
        return commissionRate.value();
    }

    public Money settlementAmount() {
        return settlementAmount;
    }

    public int settlementAmountValue() {
        return settlementAmount.value();
    }

    public DiscountBurdenInfo discountBurden() {
        return discountBurden;
    }

    public MileageBurdenInfo mileageBurden() {
        return mileageBurden;
    }

    public SettlementStatus status() {
        return status;
    }

    public Instant expectedDate() {
        return expectedDate;
    }

    public Instant settledAt() {
        return settledAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
