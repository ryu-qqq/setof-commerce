package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.order.exception.InvalidOrderItemStatusTransitionException;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.order.vo.OrderItemPrice;
import com.ryuqq.setof.domain.order.vo.OrderItemQuantity;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.ryuqq.setof.domain.order.vo.OrderProductSnapshot;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;

/**
 * 주문 아이템 Aggregate Root.
 *
 * <p>가격 정보는 {@link OrderItemPrice}로, 주문 시점 상품 정보는 {@link OrderProductSnapshot}으로 관리합니다.
 */
public class OrderItem {

    private final OrderItemId id;
    private final LegacyOrderId orderId;
    private final SellerId sellerId;
    private final ProductGroupId productGroupId;
    private final ProductId productId;
    private final OrderItemQuantity quantity;
    private final OrderItemPrice price;
    private final OrderProductSnapshot snapshot;
    private OrderItemStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private OrderItem(
            OrderItemId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            ProductGroupId productGroupId,
            ProductId productId,
            OrderItemQuantity quantity,
            OrderItemPrice price,
            OrderProductSnapshot snapshot,
            OrderItemStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.snapshot = snapshot;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 주문 아이템 생성.
     *
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param productGroupId 상품 그룹 ID
     * @param productId 상품 ID
     * @param quantity 주문 수량
     * @param price 가격 분해 정보
     * @param snapshot 주문 시점 상품 스냅샷
     * @param now 현재 시간
     * @return 대기 상태의 새 주문 아이템
     */
    public static OrderItem forNew(
            LegacyOrderId orderId,
            SellerId sellerId,
            ProductGroupId productGroupId,
            ProductId productId,
            OrderItemQuantity quantity,
            OrderItemPrice price,
            OrderProductSnapshot snapshot,
            Instant now) {
        return new OrderItem(
                OrderItemId.forNew(),
                orderId,
                sellerId,
                productGroupId,
                productId,
                quantity,
                price,
                snapshot,
                OrderItemStatus.PENDING,
                now,
                now);
    }

    /**
     * DB 복원용 팩토리 메서드.
     *
     * @param id 주문 아이템 ID
     * @param orderId 주문 ID
     * @param sellerId 셀러 ID
     * @param productGroupId 상품 그룹 ID
     * @param productId 상품 ID
     * @param quantity 주문 수량
     * @param price 가격 분해 정보
     * @param snapshot 주문 시점 상품 스냅샷
     * @param status 아이템 상태
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return 복원된 주문 아이템
     */
    public static OrderItem reconstitute(
            OrderItemId id,
            LegacyOrderId orderId,
            SellerId sellerId,
            ProductGroupId productGroupId,
            ProductId productId,
            OrderItemQuantity quantity,
            OrderItemPrice price,
            OrderProductSnapshot snapshot,
            OrderItemStatus status,
            Instant createdAt,
            Instant updatedAt) {
        return new OrderItem(
                id,
                orderId,
                sellerId,
                productGroupId,
                productId,
                quantity,
                price,
                snapshot,
                status,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 주문 아이템 확인.
     *
     * @param now 현재 시간
     */
    public void confirm(Instant now) {
        transitTo(OrderItemStatus.CONFIRMED, now);
    }

    /**
     * 배송 준비.
     *
     * @param now 현재 시간
     */
    public void readyToShip(Instant now) {
        transitTo(OrderItemStatus.SHIPPING_READY, now);
    }

    /**
     * 배송 시작.
     *
     * @param now 현재 시간
     */
    public void ship(Instant now) {
        transitTo(OrderItemStatus.SHIPPED, now);
    }

    /**
     * 배송 완료.
     *
     * @param now 현재 시간
     */
    public void deliver(Instant now) {
        transitTo(OrderItemStatus.DELIVERED, now);
    }

    /**
     * 구매 확정.
     *
     * @param now 현재 시간
     */
    public void confirmPurchase(Instant now) {
        transitTo(OrderItemStatus.PURCHASE_CONFIRMED, now);
    }

    /**
     * 정산 대기.
     *
     * @param now 현재 시간
     */
    public void startSettlement(Instant now) {
        transitTo(OrderItemStatus.SETTLEMENT_PENDING, now);
    }

    /**
     * 정산 완료.
     *
     * @param now 현재 시간
     */
    public void settle(Instant now) {
        transitTo(OrderItemStatus.SETTLED, now);
    }

    private void transitTo(OrderItemStatus target, Instant now) {
        if (!this.status.canTransitionTo(target)) {
            throw new InvalidOrderItemStatusTransitionException(this.status, target);
        }
        this.status = target;
        this.updatedAt = now;
    }

    /** 환불 가능 여부. */
    public boolean isRefundable() {
        return status.isRefundable();
    }

    /** 배송 관련 단계 여부. */
    public boolean isDeliveryStep() {
        return status.isDeliveryStep();
    }

    /** 정산 완료 여부. */
    public boolean isSettled() {
        return status.isSettled();
    }

    // VO Getters
    public OrderItemId id() {
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

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public ProductId productId() {
        return productId;
    }

    public Long productIdValue() {
        return productId.value();
    }

    public OrderItemQuantity quantity() {
        return quantity;
    }

    public int quantityValue() {
        return quantity.value();
    }

    public OrderItemPrice price() {
        return price;
    }

    public int orderAmount() {
        return price.orderAmount();
    }

    public OrderProductSnapshot snapshot() {
        return snapshot;
    }

    public OrderItemStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
