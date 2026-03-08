package com.ryuqq.setof.storage.legacy.cart.adapter;

import com.ryuqq.setof.application.cart.port.out.command.CartCommandPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.storage.legacy.cart.entity.LegacyCartEntity;
import com.ryuqq.setof.storage.legacy.cart.mapper.LegacyCartEntityMapper;
import com.ryuqq.setof.storage.legacy.cart.repository.LegacyCartJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyCartCommandAdapter - 레거시 장바구니 Command Adapter.
 *
 * <p>persist/persistAll만 제공합니다. CartItem의 ID 유무에 따라 Hibernate가 INSERT 또는 UPDATE(merge)를 자동 결정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyCartCommandAdapter implements CartCommandPort {

    private final LegacyCartJpaRepository jpaRepository;
    private final LegacyCartEntityMapper mapper;

    public LegacyCartCommandAdapter(
            LegacyCartJpaRepository jpaRepository, LegacyCartEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public CartItem persist(CartItem cartItem) {
        LegacyCartEntity entity = mapper.toEntity(cartItem);
        LegacyCartEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<CartItem> persistAll(List<CartItem> cartItems) {
        return cartItems.stream().map(this::persist).toList();
    }
}
