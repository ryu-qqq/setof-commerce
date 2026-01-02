package com.ryuqq.setof.adapter.out.persistence.order.mapper;

import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderJpaEntity;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.order.vo.OrderDiscount;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderItemId;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.OrderNumber;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.ryuqq.setof.domain.order.vo.ProductSnapshot;
import com.ryuqq.setof.domain.order.vo.ShippingInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * OrderJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Order 간 변환을 담당합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>OrderJpaEntity와 OrderItemJpaEntity 분리 변환
 *   <li>Parent-Child 관계를 FK 필드로 연결
 * </ul>
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Order -> OrderJpaEntity (저장용)
 *   <li>OrderJpaEntity + Items -> Order (조회용)
 *   <li>OrderItem <-> OrderItemJpaEntity 변환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (저장용, Parent만)
     *
     * @param domain Order 도메인
     * @return OrderJpaEntity (items 미포함)
     */
    public OrderJpaEntity toEntity(Order domain) {
        return OrderJpaEntity.of(
                domain.id().value(),
                domain.orderNumber().value(),
                domain.checkoutId().value(),
                domain.paymentId().value(),
                domain.sellerId(),
                domain.memberId(),
                domain.status().name(),
                extractReceiverName(domain),
                extractReceiverPhone(domain),
                extractAddress(domain),
                extractAddressDetail(domain),
                extractZipCode(domain),
                extractMemo(domain),
                domain.totalItemAmount().value(),
                domain.shippingFee().value(),
                domain.totalAmount().value(),
                domain.orderedAt(),
                domain.confirmedAt(),
                domain.shippedAt(),
                domain.deliveredAt(),
                domain.completedAt(),
                domain.cancelledAt(),
                domain.createdAt(),
                domain.updatedAt());
    }

    /**
     * Entity + Items -> Domain 변환 (조회용)
     *
     * @param entity OrderJpaEntity
     * @param itemEntities OrderItemJpaEntity 목록
     * @return Order 도메인
     */
    public Order toDomain(OrderJpaEntity entity, List<OrderItemJpaEntity> itemEntities) {
        List<OrderItem> items = toItems(itemEntities);
        ShippingInfo shippingInfo = toShippingInfo(entity);

        // TODO: Entity에 discountAmount, discounts 필드 추가 필요
        OrderMoney discountAmount = calculateDiscountAmount(entity);
        List<OrderDiscount> discounts = Collections.emptyList();

        return Order.restore(
                OrderId.of(entity.getId()),
                OrderNumber.of(entity.getOrderNumber()),
                CheckoutId.of(entity.getCheckoutId()),
                PaymentId.of(entity.getPaymentId()),
                entity.getSellerId(),
                entity.getMemberId(),
                OrderStatus.valueOf(entity.getStatus()),
                items,
                shippingInfo,
                OrderMoney.of(entity.getTotalItemAmount()),
                discountAmount,
                discounts,
                OrderMoney.of(entity.getShippingFee()),
                OrderMoney.of(entity.getTotalAmount()),
                entity.getOrderedAt(),
                entity.getConfirmedAt(),
                entity.getShippedAt(),
                entity.getDeliveredAt(),
                entity.getCompletedAt(),
                entity.getCancelledAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Domain Items -> Item Entities 변환 (저장용)
     *
     * @param orderId Parent Order UUID
     * @param items OrderItem 도메인 목록
     * @return OrderItemJpaEntity 목록
     */
    public List<OrderItemJpaEntity> toItemEntities(UUID orderId, List<OrderItem> items) {
        return items.stream().map(item -> toItemEntity(orderId, item)).toList();
    }

    /**
     * Item Entities -> Domain Items 변환 (조회용)
     *
     * @param entities OrderItemJpaEntity 목록
     * @return OrderItem 도메인 목록
     */
    public List<OrderItem> toItems(List<OrderItemJpaEntity> entities) {
        return entities.stream().map(this::toItem).toList();
    }

    private OrderItemJpaEntity toItemEntity(UUID orderId, OrderItem item) {
        return OrderItemJpaEntity.of(
                item.id().value(),
                orderId,
                item.productId(),
                item.productStockId(),
                item.orderedQuantity(),
                item.cancelledQuantity(),
                item.refundedQuantity(),
                item.unitPrice().value(),
                item.totalPrice().value(),
                item.status().name(),
                item.snapshot().productName(),
                item.snapshot().productImage(),
                item.snapshot().optionName(),
                item.snapshot().brandName(),
                item.snapshot().sellerName(),
                item.snapshot().originalPrice().value());
    }

    private OrderItem toItem(OrderItemJpaEntity entity) {
        ProductSnapshot snapshot =
                ProductSnapshot.of(
                        entity.getProductName(),
                        entity.getProductImage(),
                        entity.getOptionName(),
                        entity.getBrandName(),
                        entity.getSellerName(),
                        OrderMoney.of(entity.getOriginalPrice()));

        return OrderItem.restore(
                OrderItemId.of(entity.getId()),
                entity.getProductId(),
                entity.getProductStockId(),
                entity.getOrderedQuantity(),
                entity.getCancelledQuantity(),
                entity.getRefundedQuantity(),
                OrderMoney.of(entity.getUnitPrice()),
                OrderMoney.of(entity.getTotalPrice()),
                OrderItemStatus.valueOf(entity.getStatus()),
                snapshot);
    }

    private ShippingInfo toShippingInfo(OrderJpaEntity entity) {
        if (entity.getReceiverName() == null) {
            return null;
        }
        return ShippingInfo.of(
                entity.getReceiverName(),
                entity.getReceiverPhone(),
                entity.getAddress(),
                entity.getAddressDetail(),
                entity.getZipCode(),
                entity.getMemo());
    }

    private String extractReceiverName(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().receiverName() : null;
    }

    private String extractReceiverPhone(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().receiverPhone() : null;
    }

    private String extractAddress(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().address() : null;
    }

    private String extractAddressDetail(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().addressDetail() : null;
    }

    private String extractZipCode(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().zipCode() : null;
    }

    private String extractMemo(Order domain) {
        return domain.shippingInfo() != null ? domain.shippingInfo().memo() : null;
    }

    /**
     * 할인 금액 계산 (Entity에 discountAmount 필드 추가 전 임시)
     *
     * <p>totalItemAmount + shippingFee - totalAmount로 역산
     *
     * @param entity OrderJpaEntity
     * @return 계산된 할인 금액
     */
    private OrderMoney calculateDiscountAmount(OrderJpaEntity entity) {
        var totalItemAmount = entity.getTotalItemAmount();
        var shippingFee = entity.getShippingFee();
        var totalAmount = entity.getTotalAmount();

        if (totalItemAmount == null || shippingFee == null || totalAmount == null) {
            return OrderMoney.zero();
        }

        var discountValue = totalItemAmount.add(shippingFee).subtract(totalAmount);
        if (discountValue.signum() < 0) {
            return OrderMoney.zero();
        }
        return OrderMoney.of(discountValue);
    }
}
