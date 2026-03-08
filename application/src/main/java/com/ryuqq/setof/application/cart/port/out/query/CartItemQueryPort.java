package com.ryuqq.setof.application.cart.port.out.query;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import java.util.Optional;

/**
 * CartItemQueryPort - 장바구니 아이템 도메인 레벨 조회 Port.
 *
 * <p>Command 흐름에서 기존 CartItem 도메인 객체 조회에 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CartItemQueryPort {

    Optional<CartItem> findByCartIdAndUserId(Long cartId, Long userId);

    List<CartItem> findByCartIdsAndUserId(List<Long> cartIds, Long userId);

    List<CartItem> findExistingByProductIds(List<Long> productIds, Long userId);
}
