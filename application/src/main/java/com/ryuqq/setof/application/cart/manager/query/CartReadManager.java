package com.ryuqq.setof.application.cart.manager.query;

import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Cart Read Manager
 *
 * <p>Cart 조회 관련 작업을 관리하는 Manager
 *
 * <p>장바구니 지연 생성 패턴을 지원합니다: 회원이 장바구니를 처음 접근할 때 자동 생성됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartReadManager {

    private final CartQueryPort cartQueryPort;
    private final CartPersistenceManager cartPersistenceManager;
    private final ClockHolder clockHolder;

    public CartReadManager(
            CartQueryPort cartQueryPort,
            CartPersistenceManager cartPersistenceManager,
            ClockHolder clockHolder) {
        this.cartQueryPort = cartQueryPort;
        this.cartPersistenceManager = cartPersistenceManager;
        this.clockHolder = clockHolder;
    }

    /**
     * CartId로 Cart 조회
     *
     * @param cartId 장바구니 ID
     * @return 장바구니 (Optional)
     */
    public Optional<Cart> findById(CartId cartId) {
        return cartQueryPort.findById(cartId);
    }

    /**
     * 회원 ID로 Cart 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return 장바구니 (Optional)
     */
    public Optional<Cart> findByMemberId(UUID memberId) {
        return cartQueryPort.findByMemberId(memberId);
    }

    /**
     * 회원 ID로 Cart 조회 또는 생성
     *
     * <p>장바구니가 없으면 신규 생성합니다 (지연 생성 패턴).
     *
     * <p>이 메서드는 CUD 로직을 포함하지만, 장바구니의 지연 생성 비즈니스 로직을 캡슐화하기 위해 ReadManager에 위치합니다.
     *
     * @param memberId 회원 ID (UUID)
     * @return 장바구니 (없으면 신규 생성)
     */
    public Cart findOrCreateByMemberId(UUID memberId) {
        return cartQueryPort.findByMemberId(memberId).orElseGet(() -> createNewCart(memberId));
    }

    /**
     * 새 장바구니 생성
     *
     * @param memberId 회원 ID
     * @return 생성된 Cart
     */
    private Cart createNewCart(UUID memberId) {
        Instant now = Instant.now(clockHolder.getClock());
        Cart newCart = Cart.forNew(memberId, now);
        return cartPersistenceManager.persist(newCart);
    }
}
