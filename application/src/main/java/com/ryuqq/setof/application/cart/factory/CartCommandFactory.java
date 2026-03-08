package com.ryuqq.setof.application.cart.factory;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CartCommandFactory - 장바구니 도메인 객체 생성 Factory.
 *
 * <p>Command DTO에서 도메인 객체 및 Context를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartCommandFactory {

    private final TimeProvider timeProvider;

    public CartCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public Cart create(AddCartItemCommand command) {
        Instant now = timeProvider.now();
        List<CartItem> items =
                command.items().stream()
                        .map(
                                item ->
                                        CartItem.forNew(
                                                MemberId.of(command.memberId()),
                                                LegacyUserId.of(command.userId()),
                                                ProductGroupId.of(item.productGroupId()),
                                                ProductId.of(item.productId()),
                                                SellerId.of(item.sellerId()),
                                                CartQuantity.of(item.quantity()),
                                                now))
                        .toList();
        return Cart.of(command.memberId(), command.userId(), now, items);
    }

    public UpdateContext<CartItemId, CartItemUpdateData> createUpdateContext(
            ModifyCartItemCommand command) {
        Instant now = timeProvider.now();
        CartItemId cartItemId = CartItemId.of(command.cartId());
        CartItemUpdateData updateData =
                CartItemUpdateData.of(CartQuantity.of(command.newQuantity()), now);
        return new UpdateContext<>(cartItemId, updateData, now);
    }

    public StatusChangeContext<List<Long>> createDeleteContext(DeleteCartItemsCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(command.cartIds(), now);
    }
}
