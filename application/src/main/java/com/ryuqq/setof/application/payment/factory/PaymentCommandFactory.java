package com.ryuqq.setof.application.payment.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
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
import org.springframework.stereotype.Component;

/**
 * PaymentCommandFactory - 결제 커맨드 → 도메인 객체 변환 팩토리.
 *
 * <p>CreatePaymentCommand를 PaymentCreationBundle(Payment + Order + StockDeductionItems)로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentCommandFactory {

    private final TimeProvider timeProvider;

    public PaymentCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 결제 생성 번들을 만듭니다.
     *
     * <p>Payment + Order + StockDeductionItems를 하나의 번들로 묶어 반환합니다.
     *
     * @param command 결제 커맨드
     * @return PaymentCreationBundle
     */
    public PaymentCreationBundle create(CreatePaymentCommand command) {
        Instant now = timeProvider.now();

        Payment payment = createPayment(command, now);
        Order order = createOrder(command, now);
        List<StockDeductionItem> stockItems = toStockDeductionItems(command.orderItems());
        ReceiverInfo receiverInfo = toReceiverInfo(command.shippingInfo());
        RefundAccountSnapshot refundAccountSnapshot =
                toRefundAccountSnapshot(command.userId(), command.refundAccountInfo());

        return new PaymentCreationBundle(
                payment, order, stockItems, receiverInfo, refundAccountSnapshot);
    }

    /**
     * 장바구니 결제 전용 번들을 만듭니다.
     *
     * <p>PaymentCreationBundle + CartCheckoutItems를 포함합니다.
     *
     * @param command 장바구니 결제 커맨드
     * @return CartPaymentCreationBundle
     */
    public CartPaymentCreationBundle createForCart(CreatePaymentInCartCommand command) {
        List<CartOrderItemCommand> cartItems = command.orderItems();
        List<PaymentOrderItemCommand> orderItems =
                cartItems.stream().map(CartOrderItemCommand::toPaymentOrderItem).toList();

        CreatePaymentCommand paymentCommand =
                new CreatePaymentCommand(
                        command.userId(),
                        command.payAmount(),
                        command.mileageAmount(),
                        command.payMethod(),
                        command.shippingInfo(),
                        command.refundAccountInfo(),
                        orderItems);

        PaymentCreationBundle paymentBundle = create(paymentCommand);

        List<CartCheckoutItem> cartCheckoutItems =
                cartItems.stream()
                        .map(item -> new CartCheckoutItem(item.cartId(), command.userId()))
                        .toList();

        return new CartPaymentCreationBundle(paymentBundle, cartCheckoutItems);
    }

    private Order createOrder(CreatePaymentCommand command, Instant now) {
        List<OrderItem> orderItems =
                command.orderItems().stream().map(item -> toOrderItem(item, now)).toList();

        ReceiverInfo receiverInfo = toReceiverInfo(command.shippingInfo());

        return Order.forNew(null, LegacyUserId.of(command.userId()), receiverInfo, orderItems, now);
    }

    private Payment createPayment(CreatePaymentCommand command, Instant now) {
        BuyerInfo buyerInfo = toBuyerInfo(command.shippingInfo());
        return Payment.forNew(
                LegacyOrderId.forNew(),
                null,
                LegacyUserId.of(command.userId()),
                Money.of((int) command.payAmount()),
                PaymentMethodType.valueOf(command.payMethod()),
                buyerInfo,
                UsedMileage.of(command.mileageAmount()),
                now);
    }

    private List<StockDeductionItem> toStockDeductionItems(
            List<PaymentOrderItemCommand> orderItems) {
        return orderItems.stream()
                .map(item -> new StockDeductionItem(item.productId(), item.quantity()))
                .toList();
    }

    private OrderItem toOrderItem(PaymentOrderItemCommand item, Instant now) {
        int unitPrice = item.quantity() > 0 ? (int) (item.orderAmount() / item.quantity()) : 0;
        OrderItemPrice price =
                new OrderItemPrice(unitPrice, unitPrice, (int) item.orderAmount(), 0, List.of());

        return OrderItem.forNew(
                LegacyOrderId.forNew(),
                SellerId.of(item.sellerId()),
                ProductGroupId.of(item.productGroupId()),
                ProductId.of(item.productId()),
                OrderItemQuantity.of(item.quantity()),
                price,
                null,
                now);
    }

    private ReceiverInfo toReceiverInfo(ShippingInfoCommand shippingInfo) {
        if (shippingInfo == null) {
            return null;
        }
        return ReceiverInfo.of(
                shippingInfo.receiverName(),
                shippingInfo.receiverPhoneNumber(),
                shippingInfo.address(),
                shippingInfo.addressDetail(),
                shippingInfo.zipCode(),
                "KR",
                shippingInfo.deliveryMessage());
    }

    private BuyerInfo toBuyerInfo(ShippingInfoCommand shippingInfo) {
        if (shippingInfo == null) {
            return BuyerInfo.of("구매자", null, null);
        }
        return BuyerInfo.of(shippingInfo.receiverName(), null, shippingInfo.receiverPhoneNumber());
    }

    private RefundAccountSnapshot toRefundAccountSnapshot(
            long userId, RefundAccountInfoCommand refundAccountInfo) {
        if (refundAccountInfo == null) {
            return null;
        }
        return RefundAccountSnapshot.of(
                userId,
                refundAccountInfo.bankCode(),
                refundAccountInfo.accountNumber(),
                refundAccountInfo.accountHolderName());
    }
}
