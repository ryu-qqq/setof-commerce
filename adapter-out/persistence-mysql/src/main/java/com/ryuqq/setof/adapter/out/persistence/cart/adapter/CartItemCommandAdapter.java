package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.mapper.CartItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartItemJpaRepository;
import com.ryuqq.setof.application.cart.port.out.command.CartCommandPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CartItemCommandAdapter - 장바구니 아이템 Command 어댑터.
 *
 * <p>CartCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.cart.enabled=false (기본 활성)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.legacy.cart.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class CartItemCommandAdapter implements CartCommandPort {

    private final CartItemJpaRepository jpaRepository;
    private final CartItemJpaEntityMapper mapper;

    public CartItemCommandAdapter(
            CartItemJpaRepository jpaRepository, CartItemJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 장바구니 아이템 저장.
     *
     * @param cartItem CartItem 도메인 객체
     * @return 저장된 CartItem 도메인 객체
     */
    @Override
    public CartItem persist(CartItem cartItem) {
        CartItemJpaEntity entity = mapper.toEntity(cartItem);
        CartItemJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    /**
     * 장바구니 아이템 목록 저장.
     *
     * @param cartItems CartItem 도메인 객체 목록
     * @return 저장된 CartItem 도메인 객체 목록
     */
    @Override
    public List<CartItem> persistAll(List<CartItem> cartItems) {
        List<CartItemJpaEntity> entities = cartItems.stream().map(mapper::toEntity).toList();
        List<CartItemJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).toList();
    }
}
