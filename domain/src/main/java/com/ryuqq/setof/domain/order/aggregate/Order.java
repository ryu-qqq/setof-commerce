package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.order.exception.InvalidOrderStatusTransitionException;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import java.time.Instant;
import java.util.List;

/**
 * 주문 Aggregate Root.
 *
 * <p>2단계 주문 패턴: PENDING(결제대기) → PLACED(결제완료) → CONFIRMED → COMPLETED
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>주문은 PENDING 상태로 생성됨 (재고 선점 후, 결제 전)
 *   <li>결제 성공 시 PLACED로 전이, 실패 시 FAILED로 전이
 *   <li>결제 타임아웃 시 EXPIRED로 전이
 *   <li>Order는 최소 1개 이상의 OrderItem을 포함
 * </ul>
 */
public class Order {

    private final LegacyOrderId id;
    private final MemberId memberId;
    private final LegacyUserId legacyUserId;
    private final ReceiverInfo receiverInfo;
    private final List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private final Instant createdAt;
    private Instant updatedAt;
    private long paymentId;

    private Order(
            LegacyOrderId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            ReceiverInfo receiverInfo,
            List<OrderItem> orderItems,
            OrderStatus orderStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
        this.receiverInfo = receiverInfo;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 주문 생성. 초기 상태는 PENDING (결제 대기).
     *
     * @param memberId 회원 ID (nullable, 마이그레이션 중)
     * @param legacyUserId 레거시 사용자 ID
     * @param receiverInfo 수령인/배송지 정보
     * @param orderItems 주문 아이템 목록
     * @param now 현재 시간
     * @return 결제대기 상태의 새 주문
     */
    public static Order forNew(
            MemberId memberId,
            LegacyUserId legacyUserId,
            ReceiverInfo receiverInfo,
            List<OrderItem> orderItems,
            Instant now) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 아이템은 최소 1개 이상이어야 합니다");
        }
        return new Order(
                LegacyOrderId.forNew(),
                memberId,
                legacyUserId,
                receiverInfo,
                List.copyOf(orderItems),
                OrderStatus.PENDING,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 주문 ID
     * @param memberId 회원 ID (nullable)
     * @param legacyUserId 레거시 사용자 ID
     * @param receiverInfo 수령인/배송지 정보 (nullable, 레거시 데이터)
     * @param orderItems 주문 아이템 목록
     * @param orderStatus 주문 상태
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 주문
     */
    public static Order reconstitute(
            LegacyOrderId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            ReceiverInfo receiverInfo,
            List<OrderItem> orderItems,
            OrderStatus orderStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new Order(
                id,
                memberId,
                legacyUserId,
                receiverInfo,
                orderItems != null ? List.copyOf(orderItems) : List.of(),
                orderStatus,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    // ========== Business Methods ==========

    /**
     * 결제 완료 → 주문 확정 (PENDING → PLACED).
     *
     * @param now 현재 시간
     */
    public void place(Instant now) {
        transitTo(OrderStatus.PLACED, now);
    }

    /**
     * 결제 실패 (PENDING → FAILED).
     *
     * @param now 현재 시간
     */
    public void fail(Instant now) {
        transitTo(OrderStatus.FAILED, now);
    }

    /**
     * 결제 타임아웃 만료 (PENDING → EXPIRED).
     *
     * @param now 현재 시간
     */
    public void expire(Instant now) {
        transitTo(OrderStatus.EXPIRED, now);
    }

    /**
     * 판매자 주문 확인 (PLACED → CONFIRMED).
     *
     * @param now 현재 시간
     */
    public void confirm(Instant now) {
        transitTo(OrderStatus.CONFIRMED, now);
    }

    /**
     * 주문 전체 완료 (CONFIRMED → COMPLETED).
     *
     * @param now 현재 시간
     */
    public void complete(Instant now) {
        transitTo(OrderStatus.COMPLETED, now);
    }

    /**
     * 주문 취소 (PLACED → CANCELLED).
     *
     * @param now 현재 시간
     */
    public void cancel(Instant now) {
        transitTo(OrderStatus.CANCELLED, now);
    }

    /**
     * 결제 대기 상태인지 확인합니다.
     *
     * @return PENDING이면 true
     */
    public boolean isPending() {
        return orderStatus.isPending();
    }

    /**
     * 주문이 확정된 상태인지 확인합니다.
     *
     * @return PLACED 이후 정상 상태이면 true
     */
    public boolean isConfirmed() {
        return orderStatus.isConfirmed();
    }

    /**
     * 종료 상태인지 확인합니다.
     *
     * @return FAILED, EXPIRED, COMPLETED, CANCELLED이면 true
     */
    public boolean isFinal() {
        return orderStatus.isFinal();
    }

    private void transitTo(OrderStatus target, Instant now) {
        if (!this.orderStatus.canTransitionTo(target)) {
            throw new InvalidOrderStatusTransitionException(this.orderStatus, target);
        }
        this.orderStatus = target;
        this.updatedAt = now;
    }

    // VO Getters
    public LegacyOrderId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public String memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public LegacyUserId legacyUserId() {
        return legacyUserId;
    }

    public long legacyUserIdValue() {
        return legacyUserId.value();
    }

    public ReceiverInfo receiverInfo() {
        return receiverInfo;
    }

    public List<OrderItem> orderItems() {
        return orderItems;
    }

    /** 총 주문 금액 (전체 아이템 합산). */
    public int totalOrderAmount() {
        return orderItems.stream().mapToInt(OrderItem::orderAmount).sum();
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * 결제 ID를 할당합니다. 결제 영속화 후 호출됩니다.
     *
     * @param paymentId 결제 ID
     */
    public void assignPaymentId(long paymentId) {
        if (paymentId <= 0) {
            throw new IllegalArgumentException("paymentId must be positive");
        }
        this.paymentId = paymentId;
    }

    public long paymentId() {
        return paymentId;
    }
}
