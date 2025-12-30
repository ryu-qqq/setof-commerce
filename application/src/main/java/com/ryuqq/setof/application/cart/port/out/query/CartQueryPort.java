package com.ryuqq.setof.application.cart.port.out.query;

import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import java.util.Optional;
import java.util.UUID;

/**
 * Cart Query Port (Query)
 *
 * <p>Cart 조회 전용 Port
 *
 * <p><strong>규칙:</strong> Query Port는 순수 조회만 수행합니다. CUD 로직은 Application Layer에서 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CartQueryPort {

    /**
     * CartId로 Cart 조회
     *
     * @param cartId 장바구니 ID
     * @return 장바구니 (Optional)
     */
    Optional<Cart> findById(CartId cartId);

    /**
     * CartId로 Cart 조회 (필수)
     *
     * @param cartId 장바구니 ID
     * @return 장바구니
     * @throws com.ryuqq.setof.domain.cart.exception.CartNotFoundException 장바구니가 없는 경우
     */
    Cart getById(CartId cartId);

    /**
     * 회원 ID로 Cart 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return 장바구니 (Optional)
     */
    Optional<Cart> findByMemberId(UUID memberId);
}
