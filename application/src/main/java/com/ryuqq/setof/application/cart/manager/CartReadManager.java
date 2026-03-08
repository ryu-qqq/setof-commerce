package com.ryuqq.setof.application.cart.manager;

import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.port.out.query.CartItemQueryPort;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.exception.CartItemNotFoundException;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CartReadManager - 장바구니 조회 Manager.
 *
 * <p>트랜잭션 경계를 관리합니다. QueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartReadManager {

    private final CartQueryPort queryPort;
    private final CartItemQueryPort itemQueryPort;

    public CartReadManager(CartQueryPort queryPort, CartItemQueryPort itemQueryPort) {
        this.queryPort = queryPort;
        this.itemQueryPort = itemQueryPort;
    }

    @Transactional(readOnly = true)
    public List<CartItemResult> fetchCarts(CartSearchCriteria criteria) {
        return queryPort.fetchCarts(criteria);
    }

    @Transactional(readOnly = true)
    public long countCarts(CartSearchCriteria criteria) {
        return queryPort.countCarts(criteria);
    }

    @Transactional(readOnly = true)
    public CartCountResult fetchCartCount(CartSearchCriteria criteria) {
        return queryPort.fetchCartCount(criteria);
    }

    @Transactional(readOnly = true)
    public CartItem getByCartIdAndUserId(Long cartId, Long userId) {
        return itemQueryPort
                .findByCartIdAndUserId(cartId, userId)
                .orElseThrow(() -> new CartItemNotFoundException(cartId));
    }

    @Transactional(readOnly = true)
    public List<CartItem> findByCartIdsAndUserId(List<Long> cartIds, Long userId) {
        return itemQueryPort.findByCartIdsAndUserId(cartIds, userId);
    }

    @Transactional(readOnly = true)
    public List<CartItem> findExistingByProductIds(List<Long> productIds, Long userId) {
        return itemQueryPort.findExistingByProductIds(productIds, userId);
    }
}
