package com.ryuqq.setof.application.checkout.assembler;

import com.ryuqq.setof.application.checkout.dto.response.CheckoutItemResponse;
import com.ryuqq.setof.application.checkout.dto.response.CheckoutResponse;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Checkout Assembler
 *
 * <p>Domain Aggregate를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutAssembler {

    /**
     * Checkout과 Payment를 CheckoutResponse로 변환
     *
     * @param checkout Checkout Domain Aggregate
     * @param payment Payment Domain Aggregate
     * @return Response DTO
     */
    public CheckoutResponse toResponse(Checkout checkout, Payment payment) {
        ShippingAddressSnapshot shipping = checkout.shippingAddress();

        return new CheckoutResponse(
                checkout.id().value().toString(),
                payment.id().value().toString(),
                checkout.memberId(),
                checkout.status().name(),
                toItemResponses(checkout.items()),
                shipping.receiverName(),
                shipping.receiverPhone(),
                shipping.zipCode(),
                shipping.address(),
                shipping.addressDetail(),
                checkout.totalAmount().value(),
                checkout.createdAt(),
                checkout.expiredAt());
    }

    private List<CheckoutItemResponse> toItemResponses(List<CheckoutItem> items) {
        return items.stream().map(this::toItemResponse).toList();
    }

    private CheckoutItemResponse toItemResponse(CheckoutItem item) {
        return new CheckoutItemResponse(
                item.productStockId(),
                item.productId(),
                item.sellerId(),
                item.quantity(),
                item.unitPrice().value(),
                item.totalPrice().value());
    }
}
