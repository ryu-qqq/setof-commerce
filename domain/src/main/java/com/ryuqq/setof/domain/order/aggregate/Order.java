package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.util.UuidValidator;
import com.ryuqq.setof.domain.order.exception.OrderStatusException;
import com.ryuqq.setof.domain.order.vo.OrderDiscount;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderItemId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.ryuqq.setof.domain.order.vo.ShippingInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Order Aggregate Root
 *
 * <p>주문을 나타내는 Aggregate Root입니다. 판매자별로 분리되어 생성됩니다. (1 Checkout → N Orders)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드, 상태 변경 시 새 인스턴스 반환
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 안함
 * </ul>
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → CONFIRMED → PREPARING → SHIPPED → DELIVERED → COMPLETED
 *    │          │
 *    └→ CANCELLED └→ CANCELLED
 * </pre>
 */
public class Order {

    private final OrderId id;
    private final OrderNumber orderNumber;
    private final CheckoutId checkoutId;
    private final PaymentId paymentId;
    private final Long sellerId;
    private final String memberId;
    private final OrderStatus status;
    private final List<OrderItem> items;
    private final ShippingInfo shippingInfo;
    private final OrderMoney totalItemAmount;
    private final OrderMoney discountAmount;
    private final List<OrderDiscount> discounts;
    private final OrderMoney shippingFee;
    private final OrderMoney totalAmount;
    private final Instant orderedAt;
    private final Instant confirmedAt;
    private final Instant shippedAt;
    private final Instant deliveredAt;
    private final Instant completedAt;
    private final Instant cancelledAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Order(
            OrderId id,
            OrderNumber orderNumber,
            CheckoutId checkoutId,
            PaymentId paymentId,
            Long sellerId,
            String memberId,
            OrderStatus status,
            List<OrderItem> items,
            ShippingInfo shippingInfo,
            OrderMoney totalItemAmount,
            OrderMoney discountAmount,
            List<OrderDiscount> discounts,
            OrderMoney shippingFee,
            OrderMoney totalAmount,
            Instant orderedAt,
            Instant confirmedAt,
            Instant shippedAt,
            Instant deliveredAt,
            Instant completedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.checkoutId = checkoutId;
        this.paymentId = paymentId;
        this.sellerId = sellerId;
        this.memberId = memberId;
        this.status = status;
        this.items = items != null ? List.copyOf(items) : Collections.emptyList();
        this.shippingInfo = shippingInfo;
        this.totalItemAmount = totalItemAmount;
        this.discountAmount = discountAmount != null ? discountAmount : OrderMoney.zero();
        this.discounts = discounts != null ? List.copyOf(discounts) : Collections.emptyList();
        this.shippingFee = shippingFee;
        this.totalAmount = totalAmount;
        this.orderedAt = orderedAt;
        this.confirmedAt = confirmedAt;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 주문 생성 (할인 없음)
     *
     * @param checkoutId 결제 세션 ID
     * @param paymentId 결제 ID
     * @param sellerId 판매자 ID
     * @param memberId 회원 ID (UUIDv7 String)
     * @param items 주문 상품 목록
     * @param shippingInfo 배송 정보
     * @param shippingFee 배송비
     * @param now 현재 시각
     * @return Order 인스턴스
     */
    public static Order forNew(
            CheckoutId checkoutId,
            PaymentId paymentId,
            Long sellerId,
            String memberId,
            List<OrderItem> items,
            ShippingInfo shippingInfo,
            OrderMoney shippingFee,
            Instant now) {
        return forNew(
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                items,
                shippingInfo,
                OrderMoney.zero(),
                Collections.emptyList(),
                shippingFee,
                now);
    }

    /**
     * 신규 주문 생성 (할인 포함)
     *
     * @param checkoutId 결제 세션 ID
     * @param paymentId 결제 ID
     * @param sellerId 판매자 ID
     * @param memberId 회원 ID (UUIDv7 String)
     * @param items 주문 상품 목록
     * @param shippingInfo 배송 정보
     * @param discountAmount 할인 금액
     * @param discounts 적용된 할인 목록
     * @param shippingFee 배송비
     * @param now 현재 시각
     * @return Order 인스턴스
     */
    public static Order forNew(
            CheckoutId checkoutId,
            PaymentId paymentId,
            Long sellerId,
            String memberId,
            List<OrderItem> items,
            ShippingInfo shippingInfo,
            OrderMoney discountAmount,
            List<OrderDiscount> discounts,
            OrderMoney shippingFee,
            Instant now) {

        validateCheckoutId(checkoutId);
        validatePaymentId(paymentId);
        validateSellerId(sellerId);
        validateMemberId(memberId);
        validateItems(items);

        OrderMoney totalItemAmount = calculateTotalItemAmount(items);
        OrderMoney safeDiscountAmount = discountAmount != null ? discountAmount : OrderMoney.zero();
        OrderMoney safeShippingFee = shippingFee != null ? shippingFee : OrderMoney.zero();
        OrderMoney totalAmount = totalItemAmount.subtract(safeDiscountAmount).add(safeShippingFee);

        return new Order(
                OrderId.forNew(),
                OrderNumber.generate(now),
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.defaultStatus(),
                items,
                shippingInfo,
                totalItemAmount,
                safeDiscountAmount,
                discounts,
                safeShippingFee,
                totalAmount,
                now,
                null,
                null,
                null,
                null,
                null,
                now,
                now);
    }

    /** 영속화된 데이터로부터 복원 (Persistence Layer용) */
    public static Order restore(
            OrderId id,
            OrderNumber orderNumber,
            CheckoutId checkoutId,
            PaymentId paymentId,
            Long sellerId,
            String memberId,
            OrderStatus status,
            List<OrderItem> items,
            ShippingInfo shippingInfo,
            OrderMoney totalItemAmount,
            OrderMoney discountAmount,
            List<OrderDiscount> discounts,
            OrderMoney shippingFee,
            OrderMoney totalAmount,
            Instant orderedAt,
            Instant confirmedAt,
            Instant shippedAt,
            Instant deliveredAt,
            Instant completedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                status,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                shippedAt,
                deliveredAt,
                completedAt,
                cancelledAt,
                createdAt,
                updatedAt);
    }

    // ===== 상태 전이 메서드 =====

    /**
     * 주문 확정
     *
     * @param now 현재 시각
     * @return CONFIRMED 상태의 새 Order 인스턴스
     * @throws OrderStatusException 상태 전이 불가 시
     */
    public Order confirm(Instant now) {
        if (!status.canConfirm()) {
            throw OrderStatusException.notConfirmable(id, status);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.CONFIRMED,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                now,
                null,
                null,
                null,
                null,
                createdAt,
                now);
    }

    /**
     * 상품 준비 시작
     *
     * @param now 현재 시각
     * @return PREPARING 상태의 새 Order 인스턴스
     */
    public Order startPreparing(Instant now) {
        if (!status.canStartPreparing()) {
            throw OrderStatusException.notShippable(id, status);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.PREPARING,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                null,
                null,
                null,
                null,
                createdAt,
                now);
    }

    /**
     * 배송 시작
     *
     * @param now 현재 시각
     * @return SHIPPED 상태의 새 Order 인스턴스
     */
    public Order ship(Instant now) {
        if (!status.canShip()) {
            throw OrderStatusException.notShippable(id, status);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.SHIPPED,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                now,
                null,
                null,
                null,
                createdAt,
                now);
    }

    /**
     * 배송 완료
     *
     * @param now 현재 시각
     * @return DELIVERED 상태의 새 Order 인스턴스
     */
    public Order deliver(Instant now) {
        if (!status.canDeliver()) {
            throw OrderStatusException.notShippable(id, status);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.DELIVERED,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                shippedAt,
                now,
                null,
                null,
                createdAt,
                now);
    }

    /**
     * 구매 확정
     *
     * @param now 현재 시각
     * @return COMPLETED 상태의 새 Order 인스턴스
     */
    public Order complete(Instant now) {
        if (!status.canComplete()) {
            throw OrderStatusException.alreadyCompleted(id);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.COMPLETED,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                shippedAt,
                deliveredAt,
                now,
                null,
                createdAt,
                now);
    }

    /**
     * 주문 취소
     *
     * @param now 현재 시각
     * @return CANCELLED 상태의 새 Order 인스턴스
     * @throws OrderStatusException 취소 불가 시
     */
    public Order cancel(Instant now) {
        if (!status.canCancel()) {
            throw OrderStatusException.notCancellable(id, status);
        }
        return new Order(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                OrderStatus.CANCELLED,
                items,
                shippingInfo,
                totalItemAmount,
                discountAmount,
                discounts,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                shippedAt,
                deliveredAt,
                null,
                now,
                createdAt,
                now);
    }

    // ===== 도메인 로직 =====

    /**
     * 항목 개수
     *
     * @return 주문 상품 개수
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * 총 수량 (모든 항목의 수량 합계)
     *
     * @return 총 수량
     */
    public int totalQuantity() {
        return items.stream().mapToInt(OrderItem::orderedQuantity).sum();
    }

    /**
     * 유효 수량 (취소/환불 제외)
     *
     * @return 유효 수량 합계
     */
    public int effectiveQuantity() {
        return items.stream().mapToInt(OrderItem::effectiveQuantity).sum();
    }

    /**
     * 환불 가능 금액
     *
     * @return 환불 가능한 금액
     */
    public OrderMoney refundableAmount() {
        return items.stream()
                .map(OrderItem::refundableAmount)
                .reduce(OrderMoney.zero(), OrderMoney::add);
    }

    /**
     * 특정 주문 상품 조회
     *
     * @param orderItemId 주문 상품 ID
     * @return Optional<OrderItem>
     */
    public Optional<OrderItem> findItem(OrderItemId orderItemId) {
        return items.stream().filter(item -> item.id().equals(orderItemId)).findFirst();
    }

    /**
     * 취소 가능 여부
     *
     * @return 취소 가능하면 true
     */
    public boolean canBeCancelled() {
        return status.canCancel();
    }

    /**
     * 배송 전 여부
     *
     * @return 배송 전이면 true
     */
    public boolean isBeforeShipping() {
        return status.isBeforeShipping();
    }

    /**
     * 할인 적용 여부
     *
     * @return 할인이 적용되었으면 true
     */
    public boolean hasDiscount() {
        return !discounts.isEmpty() && !discountAmount.isZero();
    }

    /**
     * 할인 금액 값 (Law of Demeter)
     *
     * @return 할인 금액 (BigDecimal)
     */
    public java.math.BigDecimal discountAmountValue() {
        return discountAmount.value();
    }

    /**
     * 적용된 할인 개수
     *
     * @return 할인 정책 개수
     */
    public int discountCount() {
        return discounts.size();
    }

    // ===== Getter 메서드 =====

    public OrderId id() {
        return id;
    }

    public OrderNumber orderNumber() {
        return orderNumber;
    }

    public CheckoutId checkoutId() {
        return checkoutId;
    }

    public PaymentId paymentId() {
        return paymentId;
    }

    public Long sellerId() {
        return sellerId;
    }

    public String memberId() {
        return memberId;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderItem> items() {
        return java.util.Collections.unmodifiableList(items);
    }

    public ShippingInfo shippingInfo() {
        return shippingInfo;
    }

    public OrderMoney totalItemAmount() {
        return totalItemAmount;
    }

    public OrderMoney discountAmount() {
        return discountAmount;
    }

    public List<OrderDiscount> discounts() {
        return java.util.Collections.unmodifiableList(discounts);
    }

    public OrderMoney shippingFee() {
        return shippingFee;
    }

    public OrderMoney totalAmount() {
        return totalAmount;
    }

    public Instant orderedAt() {
        return orderedAt;
    }

    public Instant confirmedAt() {
        return confirmedAt;
    }

    public Instant shippedAt() {
        return shippedAt;
    }

    public Instant deliveredAt() {
        return deliveredAt;
    }

    public Instant completedAt() {
        return completedAt;
    }

    public Instant cancelledAt() {
        return cancelledAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    // ===== Private Helper =====

    private static OrderMoney calculateTotalItemAmount(List<OrderItem> items) {
        return items.stream().map(OrderItem::totalPrice).reduce(OrderMoney.zero(), OrderMoney::add);
    }

    private static void validateCheckoutId(CheckoutId checkoutId) {
        if (checkoutId == null) {
            throw new IllegalArgumentException("CheckoutId는 필수입니다");
        }
    }

    private static void validatePaymentId(PaymentId paymentId) {
        if (paymentId == null) {
            throw new IllegalArgumentException("PaymentId는 필수입니다");
        }
    }

    private static void validateSellerId(Long sellerId) {
        if (sellerId == null || sellerId <= 0) {
            throw new IllegalArgumentException("판매자 ID는 필수입니다");
        }
    }

    private static void validateMemberId(String memberId) {
        UuidValidator.requireValid(memberId, "회원 ID");
    }

    private static void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어있습니다");
        }
    }
}
