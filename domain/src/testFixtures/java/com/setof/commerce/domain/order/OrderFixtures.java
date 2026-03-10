package com.setof.commerce.domain.order;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import com.ryuqq.setof.domain.order.vo.AppliedDiscountSnapshot;
import com.ryuqq.setof.domain.order.vo.OptionSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderItemPrice;
import com.ryuqq.setof.domain.order.vo.OrderItemQuantity;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.ryuqq.setof.domain.order.vo.OrderProductSnapshot;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.List;

/**
 * Order 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Order 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class OrderFixtures {

    private OrderFixtures() {}

    // ===== ID Fixtures =====

    public static LegacyOrderId defaultOrderId() {
        return LegacyOrderId.of(100L);
    }

    public static LegacyOrderId orderId(Long value) {
        return LegacyOrderId.of(value);
    }

    public static OrderItemId defaultOrderItemId() {
        return OrderItemId.of(200L);
    }

    // ===== VO Fixtures =====

    public static MemberId defaultMemberId() {
        return MemberId.of("01914b6c-dead-beef-8000-aabbccddeeff");
    }

    public static LegacyUserId defaultLegacyUserId() {
        return LegacyUserId.of(999L);
    }

    public static ReceiverInfo defaultReceiverInfo() {
        return ReceiverInfo.of(
                "김수령", "010-9876-5432", "서울시 강남구 테헤란로 123", "5층 501호", "06141", "KR", "문 앞에 놔주세요");
    }

    public static ReceiverInfo receiverInfoWithoutRequest() {
        return ReceiverInfo.of(
                "이배송", "010-1111-2222", "부산시 해운대구 센텀로 456", null, "48058", "KR", null);
    }

    public static OptionSnapshot defaultOptionSnapshot() {
        return OptionSnapshot.of(10L, 101L, "사이즈", "270mm");
    }

    public static OptionSnapshot colorOptionSnapshot() {
        return OptionSnapshot.of(20L, 201L, "색상", "화이트");
    }

    public static OrderProductSnapshot defaultOrderProductSnapshot() {
        return new OrderProductSnapshot(
                1L,
                "테스트 상품 A",
                10L,
                1L,
                "테스트 셀러",
                100L,
                "테스트 브랜드",
                1000L,
                "https://example.com/image.jpg",
                List.of(defaultOptionSnapshot(), colorOptionSnapshot()));
    }

    public static OrderProductSnapshot snapshotWithoutOptions() {
        return new OrderProductSnapshot(
                2L,
                "옵션없는 상품 B",
                11L,
                1L,
                "테스트 셀러",
                100L,
                "테스트 브랜드",
                1000L,
                "https://example.com/image2.jpg",
                List.of());
    }

    public static AppliedDiscountSnapshot defaultDiscountSnapshot() {
        return AppliedDiscountSnapshot.of(1001L, "SELLER_INSTANT", 5000, "셀러 10% 할인");
    }

    public static OrderItemPrice defaultOrderItemPrice() {
        return new OrderItemPrice(50000, 45000, 48000, 3000, List.of(defaultDiscountSnapshot()));
    }

    public static OrderItemPrice orderItemPriceWithoutDiscount() {
        return new OrderItemPrice(30000, 30000, 33000, 3000, List.of());
    }

    // ===== OrderItem Fixtures =====

    public static OrderItem newOrderItem() {
        return OrderItem.forNew(
                defaultOrderId(),
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(2),
                defaultOrderItemPrice(),
                defaultOrderProductSnapshot(),
                CommonVoFixtures.now());
    }

    public static OrderItem newOrderItem(LegacyOrderId orderId) {
        return OrderItem.forNew(
                orderId,
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(1),
                orderItemPriceWithoutDiscount(),
                snapshotWithoutOptions(),
                CommonVoFixtures.now());
    }

    public static OrderItem pendingOrderItem() {
        return OrderItem.reconstitute(
                defaultOrderItemId(),
                defaultOrderId(),
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(2),
                defaultOrderItemPrice(),
                defaultOrderProductSnapshot(),
                OrderItemStatus.PENDING,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static OrderItem confirmedOrderItem() {
        return OrderItem.reconstitute(
                defaultOrderItemId(),
                defaultOrderId(),
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(2),
                defaultOrderItemPrice(),
                defaultOrderProductSnapshot(),
                OrderItemStatus.CONFIRMED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static OrderItem deliveredOrderItem() {
        return OrderItem.reconstitute(
                defaultOrderItemId(),
                defaultOrderId(),
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(1),
                defaultOrderItemPrice(),
                defaultOrderProductSnapshot(),
                OrderItemStatus.DELIVERED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static OrderItem settledOrderItem() {
        return OrderItem.reconstitute(
                defaultOrderItemId(),
                defaultOrderId(),
                SellerId.of(1L),
                ProductGroupId.of(1L),
                ProductId.of(10L),
                OrderItemQuantity.of(1),
                defaultOrderItemPrice(),
                defaultOrderProductSnapshot(),
                OrderItemStatus.SETTLED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== Order Aggregate Fixtures =====

    public static Order newOrder() {
        return Order.forNew(
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(newOrderItem()),
                CommonVoFixtures.now());
    }

    public static Order newOrderWithMultipleItems() {
        return Order.forNew(
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(newOrderItem(), newOrderItem()),
                CommonVoFixtures.now());
    }

    public static Order pendingOrder() {
        return Order.reconstitute(
                defaultOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(pendingOrderItem()),
                OrderStatus.PENDING,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Order placedOrder() {
        return Order.reconstitute(
                defaultOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(confirmedOrderItem()),
                OrderStatus.PLACED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Order confirmedOrder() {
        return Order.reconstitute(
                defaultOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(confirmedOrderItem()),
                OrderStatus.CONFIRMED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Order failedOrder() {
        return Order.reconstitute(
                defaultOrderId(),
                defaultMemberId(),
                defaultLegacyUserId(),
                defaultReceiverInfo(),
                List.of(pendingOrderItem()),
                OrderStatus.FAILED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }
}
