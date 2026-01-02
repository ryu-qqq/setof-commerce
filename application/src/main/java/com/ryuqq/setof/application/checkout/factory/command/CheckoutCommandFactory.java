package com.ryuqq.setof.application.checkout.factory.command;

import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutItemCommand;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Checkout Command Factory
 *
 * <p>Command를 Domain Aggregate로 변환하는 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutCommandFactory {

    private static final int DEFAULT_EXPIRATION_MINUTES = 30;

    private final ClockHolder clockHolder;

    public CheckoutCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateCheckoutCommand를 Checkout Aggregate로 변환
     *
     * @param command 생성 Command
     * @return Checkout Aggregate
     */
    public Checkout createCheckout(CreateCheckoutCommand command) {
        List<CheckoutItem> items = toCheckoutItems(command.items());
        ShippingAddressSnapshot shippingAddress = toShippingAddressSnapshot(command);
        Instant now = Instant.now(clockHolder.getClock());

        return Checkout.forNew(
                command.memberId(),
                items,
                shippingAddress,
                CheckoutMoney.zero(),
                DEFAULT_EXPIRATION_MINUTES,
                now);
    }

    private List<CheckoutItem> toCheckoutItems(List<CreateCheckoutItemCommand> commands) {
        return commands.stream().map(this::toCheckoutItem).toList();
    }

    private CheckoutItem toCheckoutItem(CreateCheckoutItemCommand command) {
        return CheckoutItem.of(
                command.productStockId(),
                command.productId(),
                command.sellerId(),
                command.quantity(),
                CheckoutMoney.of(command.unitPrice()),
                command.productName(),
                command.productImage(),
                command.optionName(),
                command.brandName(),
                command.sellerName());
    }

    private ShippingAddressSnapshot toShippingAddressSnapshot(CreateCheckoutCommand command) {
        return ShippingAddressSnapshot.of(
                command.receiverName(),
                command.receiverPhone(),
                command.address(),
                command.addressDetail(),
                command.zipCode(),
                null);
    }
}
