package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.dto.command.SoftDeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.SoftDeleteCartItemsUseCase;
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
 * 장바구니 아이템 소프트 딜리트 Service
 *
 * <p>체크아웃 생성 시 장바구니 아이템을 소프트 딜리트합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SoftDeleteCartItemsService implements SoftDeleteCartItemsUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final ClockHolder clockHolder;

    public SoftDeleteCartItemsService(
            CartReadManager cartReadManager,
            CartPersistenceManager cartPersistenceManager,
            ClockHolder clockHolder) {
        this.cartReadManager = cartReadManager;
        this.cartPersistenceManager = cartPersistenceManager;
        this.clockHolder = clockHolder;
    }

    /**
     * 장바구니 아이템 소프트 딜리트
     *
     * <p>productStockId를 기준으로 해당 회원의 활성 장바구니 아이템을 찾아 소프트 딜리트합니다.
     *
     * @param command 소프트 딜리트 Command
     */
    @Override
    @Transactional
    public void softDeleteItems(SoftDeleteCartItemsCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());

        // 활성 아이템 중 productStockId가 일치하는 아이템의 CartItemId 수집
        Set<Long> targetProductStockIds = Set.copyOf(command.productStockIds());
        List<CartItemId> cartItemIdsToDelete =
                cart.activeItems().stream()
                        .filter(item -> targetProductStockIds.contains(item.productStockId()))
                        .map(CartItem::id)
                        .filter(id -> id != null)
                        .toList();

        if (cartItemIdsToDelete.isEmpty()) {
            // 소프트 딜리트할 아이템이 없으면 early return
            return;
        }

        // 아이템 소프트 딜리트
        Instant now = Instant.now(clockHolder.getClock());
        Cart deletedCart = cart.softDeleteItems(cartItemIdsToDelete, now);

        cartPersistenceManager.persist(deletedCart);
    }
}
