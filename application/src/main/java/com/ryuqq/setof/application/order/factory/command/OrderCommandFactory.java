package com.ryuqq.setof.application.order.factory.command;

import com.ryuqq.setof.application.order.dto.command.CreateOrderCommand;
import com.ryuqq.setof.application.order.dto.command.CreateOrderDiscountCommand;
import com.ryuqq.setof.application.order.dto.command.CreateOrderItemCommand;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.order.vo.OrderDiscount;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.ProductSnapshot;
import com.ryuqq.setof.domain.order.vo.ShippingInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 주문 Command Factory
 *
 * <p>Command DTO를 Domain Aggregate로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderCommandFactory {

    private final ClockHolder clockHolder;

    public OrderCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 주문 생성
     *
     * @param command 주문 생성 Command
     * @return Order 도메인 객체
     */
    public Order createOrder(CreateOrderCommand command) {
        List<OrderItem> items = toOrderItems(command.items());
        ShippingInfo shippingInfo = toShippingInfo(command);
        OrderMoney shippingFee = toOrderMoney(command.shippingFee());
        Instant now = Instant.now(clockHolder.getClock());

        if (command.hasDiscount()) {
            OrderMoney discountAmount = toOrderMoney(command.discountAmount());
            List<OrderDiscount> discounts = toOrderDiscounts(command.discounts());

            return Order.forNew(
                    CheckoutId.fromString(command.checkoutId()),
                    PaymentId.fromString(command.paymentId()),
                    command.sellerId(),
                    command.memberId(),
                    items,
                    shippingInfo,
                    discountAmount,
                    discounts,
                    shippingFee,
                    now);
        }

        return Order.forNew(
                CheckoutId.fromString(command.checkoutId()),
                PaymentId.fromString(command.paymentId()),
                command.sellerId(),
                command.memberId(),
                items,
                shippingInfo,
                shippingFee,
                now);
    }

    private List<OrderItem> toOrderItems(List<CreateOrderItemCommand> commands) {
        return commands.stream().map(this::toOrderItem).toList();
    }

    private OrderItem toOrderItem(CreateOrderItemCommand command) {
        ProductSnapshot snapshot =
                ProductSnapshot.of(
                        command.productName(),
                        command.productImage(),
                        command.optionName(),
                        command.brandName(),
                        command.sellerName(),
                        OrderMoney.of(command.unitPrice()));

        return OrderItem.forNew(
                command.productId(),
                command.productStockId(),
                command.quantity(),
                OrderMoney.of(command.unitPrice()),
                snapshot);
    }

    private ShippingInfo toShippingInfo(CreateOrderCommand command) {
        return ShippingInfo.of(
                command.receiverName(),
                command.receiverPhone(),
                command.address(),
                command.addressDetail(),
                command.zipCode(),
                command.memo());
    }

    private OrderMoney toOrderMoney(BigDecimal value) {
        return value != null ? OrderMoney.of(value) : OrderMoney.zero();
    }

    private List<OrderDiscount> toOrderDiscounts(List<CreateOrderDiscountCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return Collections.emptyList();
        }
        return commands.stream().map(this::toOrderDiscount).toList();
    }

    private OrderDiscount toOrderDiscount(CreateOrderDiscountCommand command) {
        return OrderDiscount.of(
                DiscountPolicyId.of(command.discountPolicyId()),
                DiscountGroup.valueOf(command.discountGroup()),
                DiscountAmount.of(command.amount()),
                command.policyName());
    }
}
