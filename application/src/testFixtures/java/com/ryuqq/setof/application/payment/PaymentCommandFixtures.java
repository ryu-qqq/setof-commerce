package com.ryuqq.setof.application.payment;

import com.ryuqq.setof.application.payment.dto.bundle.CartPaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.dto.command.CartOrderItemCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentCommand;
import com.ryuqq.setof.application.payment.dto.command.CreatePaymentInCartCommand;
import com.ryuqq.setof.application.payment.dto.command.PaymentOrderItemCommand;
import com.ryuqq.setof.application.payment.dto.command.RefundAccountInfoCommand;
import com.ryuqq.setof.application.payment.dto.command.ShippingInfoCommand;
import com.ryuqq.setof.domain.cart.vo.CartCheckoutItem;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.vo.OrderItemPrice;
import com.ryuqq.setof.domain.order.vo.OrderItemQuantity;
import com.ryuqq.setof.domain.order.vo.ReceiverInfo;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.BuyerInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentMethodType;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
import com.ryuqq.setof.domain.payment.vo.UsedMileage;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.stock.vo.StockDeductionItem;
import java.time.Instant;
import java.util.List;

/**
 * Payment Application Command 테스트 Fixtures.
 *
 * <p>결제 생성 Command / Bundle 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class PaymentCommandFixtures {

    private PaymentCommandFixtures() {}

    // ===== 공통 상수 =====

    public static final long DEFAULT_USER_ID = 1L;
    public static final long DEFAULT_PAY_AMOUNT = 50000L;
    public static final String DEFAULT_PAY_METHOD = "CARD";
    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final long DEFAULT_PRODUCT_ID = 200L;
    public static final long DEFAULT_SELLER_ID = 10L;
    public static final int DEFAULT_QUANTITY = 2;
    public static final long DEFAULT_ORDER_AMOUNT = 50000L;
    public static final long DEFAULT_CART_ID = 1L;

    // ===== ShippingInfoCommand =====

    public static ShippingInfoCommand shippingInfoCommand() {
        return new ShippingInfoCommand(
                "홍길동", "010-1234-5678", "12345", "서울시 강남구 테헤란로 1", "101호", "문 앞에 놓아주세요");
    }

    // ===== RefundAccountInfoCommand =====

    public static RefundAccountInfoCommand refundAccountInfoCommand() {
        return new RefundAccountInfoCommand("004", "123-456-789012", "홍길동");
    }

    // ===== PaymentOrderItemCommand =====

    public static PaymentOrderItemCommand paymentOrderItemCommand() {
        return new PaymentOrderItemCommand(
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_PRODUCT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_QUANTITY,
                DEFAULT_ORDER_AMOUNT,
                "PENDING");
    }

    public static PaymentOrderItemCommand paymentOrderItemCommand(
            long productGroupId, long productId, int quantity, long orderAmount) {
        return new PaymentOrderItemCommand(
                productGroupId, productId, DEFAULT_SELLER_ID, quantity, orderAmount, "PENDING");
    }

    // ===== CartOrderItemCommand =====

    public static CartOrderItemCommand cartOrderItemCommand() {
        return new CartOrderItemCommand(
                DEFAULT_CART_ID,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_PRODUCT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_QUANTITY,
                DEFAULT_ORDER_AMOUNT,
                "PENDING");
    }

    public static CartOrderItemCommand cartOrderItemCommand(long cartId) {
        return new CartOrderItemCommand(
                cartId,
                DEFAULT_PRODUCT_GROUP_ID,
                DEFAULT_PRODUCT_ID,
                DEFAULT_SELLER_ID,
                DEFAULT_QUANTITY,
                DEFAULT_ORDER_AMOUNT,
                "PENDING");
    }

    // ===== CreatePaymentCommand =====

    public static CreatePaymentCommand createPaymentCommand() {
        return new CreatePaymentCommand(
                DEFAULT_USER_ID,
                DEFAULT_PAY_AMOUNT,
                0L,
                DEFAULT_PAY_METHOD,
                shippingInfoCommand(),
                null,
                List.of(paymentOrderItemCommand()));
    }

    public static CreatePaymentCommand createPaymentCommandWithRefundAccount() {
        return new CreatePaymentCommand(
                DEFAULT_USER_ID,
                DEFAULT_PAY_AMOUNT,
                0L,
                DEFAULT_PAY_METHOD,
                shippingInfoCommand(),
                refundAccountInfoCommand(),
                List.of(paymentOrderItemCommand()));
    }

    // ===== CreatePaymentInCartCommand =====

    public static CreatePaymentInCartCommand createPaymentInCartCommand() {
        return new CreatePaymentInCartCommand(
                DEFAULT_USER_ID,
                DEFAULT_PAY_AMOUNT,
                0L,
                DEFAULT_PAY_METHOD,
                shippingInfoCommand(),
                null,
                List.of(cartOrderItemCommand()));
    }

    public static CreatePaymentInCartCommand createPaymentInCartCommandWithRefundAccount() {
        return new CreatePaymentInCartCommand(
                DEFAULT_USER_ID,
                DEFAULT_PAY_AMOUNT,
                0L,
                DEFAULT_PAY_METHOD,
                shippingInfoCommand(),
                refundAccountInfoCommand(),
                List.of(cartOrderItemCommand()));
    }

    // ===== Domain Fixtures =====

    public static Payment payment() {
        return Payment.forNew(
                LegacyOrderId.forNew(),
                null,
                LegacyUserId.of(DEFAULT_USER_ID),
                Money.of((int) DEFAULT_PAY_AMOUNT),
                PaymentMethodType.CARD,
                BuyerInfo.of("홍길동", null, "010-1234-5678"),
                UsedMileage.zero(),
                Instant.parse("2024-01-01T00:00:00Z"));
    }

    public static Order order() {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");
        List<OrderItem> items = List.of(orderItem(now));
        ReceiverInfo receiverInfo = receiverInfo();
        return Order.forNew(null, LegacyUserId.of(DEFAULT_USER_ID), receiverInfo, items, now);
    }

    public static Order orderWithItems(List<OrderItem> items) {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");
        return Order.forNew(null, LegacyUserId.of(DEFAULT_USER_ID), receiverInfo(), items, now);
    }

    public static OrderItem orderItem(Instant now) {
        OrderItemPrice price =
                new OrderItemPrice(25000, 25000, (int) DEFAULT_ORDER_AMOUNT, 0, List.of());
        return OrderItem.forNew(
                LegacyOrderId.forNew(),
                SellerId.of(DEFAULT_SELLER_ID),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                ProductId.of(DEFAULT_PRODUCT_ID),
                OrderItemQuantity.of(DEFAULT_QUANTITY),
                price,
                null,
                now);
    }

    public static ReceiverInfo receiverInfo() {
        return ReceiverInfo.of(
                "홍길동", "010-1234-5678", "서울시 강남구 테헤란로 1", "101호", "12345", "KR", "문 앞에 놓아주세요");
    }

    public static RefundAccountSnapshot refundAccountSnapshot() {
        return RefundAccountSnapshot.of(DEFAULT_USER_ID, "004", "123-456-789012", "홍길동");
    }

    public static List<StockDeductionItem> stockDeductionItems() {
        return List.of(new StockDeductionItem(DEFAULT_PRODUCT_ID, DEFAULT_QUANTITY));
    }

    // ===== Bundle Fixtures =====

    public static PaymentCreationBundle paymentCreationBundle() {
        return new PaymentCreationBundle(
                payment(), order(), stockDeductionItems(), receiverInfo(), null);
    }

    public static PaymentCreationBundle paymentCreationBundleWithRefundAccount() {
        return new PaymentCreationBundle(
                payment(), order(), stockDeductionItems(), receiverInfo(), refundAccountSnapshot());
    }

    public static CartPaymentCreationBundle cartPaymentCreationBundle() {
        return new CartPaymentCreationBundle(
                paymentCreationBundle(),
                List.of(new CartCheckoutItem(DEFAULT_CART_ID, DEFAULT_USER_ID)));
    }

    public static CartPaymentCreationBundle cartPaymentCreationBundleWithMultipleItems() {
        return new CartPaymentCreationBundle(
                paymentCreationBundle(),
                List.of(
                        new CartCheckoutItem(1L, DEFAULT_USER_ID),
                        new CartCheckoutItem(2L, DEFAULT_USER_ID)));
    }
}
