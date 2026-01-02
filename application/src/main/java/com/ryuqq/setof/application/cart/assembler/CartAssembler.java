package com.ryuqq.setof.application.cart.assembler;

import com.ryuqq.setof.application.cart.dto.response.CartItemResponse;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Cart Assembler
 *
 * <p>Domain Aggregate를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartAssembler {

    /**
     * Cart를 CartResponse로 변환
     *
     * @param cart Cart Domain Aggregate
     * @return Response DTO
     */
    public CartResponse toResponse(Cart cart) {
        return new CartResponse(
                cart.id() != null ? cart.id().value() : null,
                cart.memberId(),
                toItemResponses(cart.items()),
                cart.totalAmount(),
                cart.selectedTotalAmount(),
                cart.itemCount(),
                cart.selectedItemCount(),
                cart.totalQuantity(),
                cart.createdAt(),
                cart.updatedAt());
    }

    private List<CartItemResponse> toItemResponses(List<CartItem> items) {
        return items.stream().map(this::toItemResponse).toList();
    }

    private CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.id() != null ? item.id().value() : null,
                item.productStockId(),
                item.productId(),
                item.productGroupId(),
                item.sellerId(),
                item.quantity(),
                item.unitPrice(),
                item.totalPrice(),
                item.selected(),
                item.addedAt());
    }
}
