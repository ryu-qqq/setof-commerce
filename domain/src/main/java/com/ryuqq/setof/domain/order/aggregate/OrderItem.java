package com.ryuqq.setof.domain.order.aggregate;

import com.ryuqq.setof.domain.order.exception.OrderItemQuantityException;
import com.ryuqq.setof.domain.order.vo.OrderItemId;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.ProductSnapshot;

/**
 * OrderItem Entity (Order Aggregate 내부)
 *
 * <p>주문 내 개별 상품 항목입니다. Order Aggregate 내부의 Entity로, Order를 통해서만 접근/수정됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드, 상태 변경 시 새 인스턴스 반환
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 안함
 *   <li>부분 취소/환불 지원 - orderedQuantity, cancelledQuantity, refundedQuantity
 * </ul>
 */
public class OrderItem {

    private final OrderItemId id;
    private final Long productId;
    private final Long productStockId;
    private final int orderedQuantity;
    private final int cancelledQuantity;
    private final int refundedQuantity;
    private final OrderMoney unitPrice;
    private final OrderMoney totalPrice;
    private final OrderItemStatus status;
    private final ProductSnapshot snapshot;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private OrderItem(
            OrderItemId id,
            Long productId,
            Long productStockId,
            int orderedQuantity,
            int cancelledQuantity,
            int refundedQuantity,
            OrderMoney unitPrice,
            OrderMoney totalPrice,
            OrderItemStatus status,
            ProductSnapshot snapshot) {
        this.id = id;
        this.productId = productId;
        this.productStockId = productStockId;
        this.orderedQuantity = orderedQuantity;
        this.cancelledQuantity = cancelledQuantity;
        this.refundedQuantity = refundedQuantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
        this.snapshot = snapshot;
    }

    /**
     * 신규 주문 상품 생성
     *
     * @param productId 상품 ID
     * @param productStockId 상품 재고 ID
     * @param orderedQuantity 주문 수량
     * @param unitPrice 개당 가격
     * @param snapshot 상품 스냅샷
     * @return OrderItem 인스턴스
     */
    public static OrderItem forNew(
            Long productId,
            Long productStockId,
            int orderedQuantity,
            OrderMoney unitPrice,
            ProductSnapshot snapshot) {

        validatePositive(productId, "productId");
        validatePositive(productStockId, "productStockId");
        validateQuantity(orderedQuantity);
        validateNotNull(unitPrice, "unitPrice");
        validateNotNull(snapshot, "snapshot");

        OrderMoney totalPrice = unitPrice.multiply(orderedQuantity);

        return new OrderItem(
                OrderItemId.forNew(),
                productId,
                productStockId,
                orderedQuantity,
                0,
                0,
                unitPrice,
                totalPrice,
                OrderItemStatus.defaultStatus(),
                snapshot);
    }

    /** 영속화된 데이터로부터 복원 (Persistence Layer용) */
    public static OrderItem restore(
            OrderItemId id,
            Long productId,
            Long productStockId,
            int orderedQuantity,
            int cancelledQuantity,
            int refundedQuantity,
            OrderMoney unitPrice,
            OrderMoney totalPrice,
            OrderItemStatus status,
            ProductSnapshot snapshot) {
        return new OrderItem(
                id,
                productId,
                productStockId,
                orderedQuantity,
                cancelledQuantity,
                refundedQuantity,
                unitPrice,
                totalPrice,
                status,
                snapshot);
    }

    // ===== 상태 변경 메서드 =====

    /**
     * 상태 변경
     *
     * @param newStatus 새 상태
     * @return 상태가 변경된 새 OrderItem 인스턴스
     */
    public OrderItem changeStatus(OrderItemStatus newStatus) {
        return new OrderItem(
                id,
                productId,
                productStockId,
                orderedQuantity,
                cancelledQuantity,
                refundedQuantity,
                unitPrice,
                totalPrice,
                newStatus,
                snapshot);
    }

    /**
     * 취소 처리 (수량 기반)
     *
     * @param cancelQuantity 취소할 수량
     * @return 취소 처리된 새 OrderItem 인스턴스
     */
    public OrderItem cancel(int cancelQuantity) {
        int available = effectiveQuantity();
        if (cancelQuantity > available) {
            throw OrderItemQuantityException.cancelExceedsAvailable(cancelQuantity, available);
        }

        int newCancelledQuantity = cancelledQuantity + cancelQuantity;
        OrderMoney newTotalPrice =
                unitPrice.multiply(orderedQuantity - newCancelledQuantity - refundedQuantity);
        OrderItemStatus newStatus =
                (newCancelledQuantity + refundedQuantity >= orderedQuantity)
                        ? OrderItemStatus.CANCELLED
                        : status;

        return new OrderItem(
                id,
                productId,
                productStockId,
                orderedQuantity,
                newCancelledQuantity,
                refundedQuantity,
                unitPrice,
                newTotalPrice,
                newStatus,
                snapshot);
    }

    /**
     * 환불 처리 (수량 기반)
     *
     * @param refundQuantity 환불할 수량
     * @return 환불 처리된 새 OrderItem 인스턴스
     */
    public OrderItem refund(int refundQuantity) {
        int available = effectiveQuantity();
        if (refundQuantity > available) {
            throw OrderItemQuantityException.refundExceedsAvailable(refundQuantity, available);
        }

        int newRefundedQuantity = refundedQuantity + refundQuantity;
        OrderMoney newTotalPrice =
                unitPrice.multiply(orderedQuantity - cancelledQuantity - newRefundedQuantity);

        return new OrderItem(
                id,
                productId,
                productStockId,
                orderedQuantity,
                cancelledQuantity,
                newRefundedQuantity,
                unitPrice,
                newTotalPrice,
                status,
                snapshot);
    }

    // ===== 도메인 로직 =====

    /**
     * 유효 수량 (취소/환불 제외)
     *
     * @return 주문 수량 - 취소 수량 - 환불 수량
     */
    public int effectiveQuantity() {
        return orderedQuantity - cancelledQuantity - refundedQuantity;
    }

    /**
     * 유효 금액 계산
     *
     * @return 개당 가격 * 유효 수량
     */
    public OrderMoney effectiveAmount() {
        return unitPrice.multiply(effectiveQuantity());
    }

    /**
     * 환불 가능 금액 계산
     *
     * @return 환불 가능한 금액
     */
    public OrderMoney refundableAmount() {
        return effectiveAmount();
    }

    /**
     * 완전 취소/환불 여부
     *
     * @return 유효 수량이 0이면 true
     */
    public boolean isFullyCancelledOrRefunded() {
        return effectiveQuantity() == 0;
    }

    // ===== Getter 메서드 =====

    public OrderItemId id() {
        return id;
    }

    public Long productId() {
        return productId;
    }

    public Long productStockId() {
        return productStockId;
    }

    public int orderedQuantity() {
        return orderedQuantity;
    }

    public int cancelledQuantity() {
        return cancelledQuantity;
    }

    public int refundedQuantity() {
        return refundedQuantity;
    }

    public OrderMoney unitPrice() {
        return unitPrice;
    }

    public OrderMoney totalPrice() {
        return totalPrice;
    }

    public OrderItemStatus status() {
        return status;
    }

    public ProductSnapshot snapshot() {
        return snapshot;
    }

    // ===== Private Helper =====

    private static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + "은(는) 양수여야 합니다");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다");
        }
    }

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + "은(는) 필수입니다");
        }
    }
}
