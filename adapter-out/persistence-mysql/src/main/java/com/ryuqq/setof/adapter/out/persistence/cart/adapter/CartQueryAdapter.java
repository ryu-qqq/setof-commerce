package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.mapper.CartJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartQueryDslRepository;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.exception.CartNotFoundException;
import com.ryuqq.setof.domain.cart.vo.CartId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * CartQueryAdapter - Cart 조회 Adapter
 *
 * <p>Application Layer의 CartQueryPort 구현체입니다.
 *
 * <p><strong>규칙:</strong> Query Adapter는 순수 조회만 수행합니다. CUD 로직은 Application Layer에서 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartQueryAdapter implements CartQueryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartQueryDslRepository cartQueryDslRepository;
    private final CartItemQueryDslRepository cartItemQueryDslRepository;
    private final CartJpaEntityMapper mapper;

    public CartQueryAdapter(
            CartJpaRepository cartJpaRepository,
            CartQueryDslRepository cartQueryDslRepository,
            CartItemQueryDslRepository cartItemQueryDslRepository,
            CartJpaEntityMapper mapper) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartQueryDslRepository = cartQueryDslRepository;
        this.cartItemQueryDslRepository = cartItemQueryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Cart> findById(CartId cartId) {
        return cartJpaRepository.findById(cartId.value()).map(this::toDomainWithItems);
    }

    @Override
    public Cart getById(CartId cartId) {
        return findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
    }

    @Override
    public Optional<Cart> findByMemberId(UUID memberId) {
        return cartQueryDslRepository
                .findByMemberId(memberId.toString())
                .map(this::toDomainWithItems);
    }

    /** Entity -> Domain 변환 (아이템 포함) */
    private Cart toDomainWithItems(CartJpaEntity cartEntity) {
        List<CartItemJpaEntity> itemEntities =
                cartItemQueryDslRepository.findByCartId(cartEntity.getId());
        return mapper.toDomain(cartEntity, itemEntities);
    }
}
