package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.dto.command.RestoreCartItemsCommand;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.RestoreCartItemsUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 장바구니 아이템 복원 Service
 *
 * <p>결제 실패 시 소프트 딜리트된 장바구니 아이템을 복원합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RestoreCartItemsService implements RestoreCartItemsUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final ClockHolder clockHolder;

    public RestoreCartItemsService(
            CartReadManager cartReadManager,
            CartPersistenceManager cartPersistenceManager,
            ClockHolder clockHolder) {
        this.cartReadManager = cartReadManager;
        this.cartPersistenceManager = cartPersistenceManager;
        this.clockHolder = clockHolder;
    }

    /**
     * 소프트 딜리트된 장바구니 아이템 복원
     *
     * <p>productStockId를 기준으로 해당 회원의 삭제된 장바구니 아이템을 찾아 복원합니다.
     *
     * @param command 복원 Command
     */
    @Override
    @Transactional
    public void restoreItems(RestoreCartItemsCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());

        // 삭제된 아이템 중 productStockId가 일치하는 아이템의 CartItemId 수집
        Set<Long> targetProductStockIds = Set.copyOf(command.productStockIds());
        List<CartItemId> cartItemIdsToRestore =
                cart.deletedItems().stream()
                        .filter(item -> targetProductStockIds.contains(item.productStockId()))
                        .map(CartItem::id)
                        .filter(id -> id != null)
                        .toList();

        if (cartItemIdsToRestore.isEmpty()) {
            // 복원할 아이템이 없으면 early return
            return;
        }

        // 아이템 복원
        Instant now = Instant.now(clockHolder.getClock());
        Cart restoredCart = cart.restoreItems(cartItemIdsToRestore, now);

        cartPersistenceManager.persist(restoredCart);
    }
}
