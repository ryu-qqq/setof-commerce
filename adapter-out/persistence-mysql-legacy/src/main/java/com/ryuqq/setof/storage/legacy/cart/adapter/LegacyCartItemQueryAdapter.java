package com.ryuqq.setof.storage.legacy.cart.adapter;

import com.ryuqq.setof.application.cart.port.out.query.CartItemQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.storage.legacy.cart.mapper.LegacyCartEntityMapper;
import com.ryuqq.setof.storage.legacy.cart.repository.LegacyCartCommandQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyCartItemQueryAdapter - 레거시 장바구니 아이템 도메인 레벨 조회 Adapter.
 *
 * <p>Command 흐름에서 기존 CartItem 도메인 객체 조회에 사용합니다.
 *
 * <p>활성화 조건: persistence.legacy.cart.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.cart.enabled", havingValue = "true")
public class LegacyCartItemQueryAdapter implements CartItemQueryPort {

    private final LegacyCartCommandQueryDslRepository queryDslRepository;
    private final LegacyCartEntityMapper mapper;

    public LegacyCartItemQueryAdapter(
            LegacyCartCommandQueryDslRepository queryDslRepository, LegacyCartEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CartItem> findByCartIdAndUserId(Long cartId, Long userId) {
        return queryDslRepository.findByCartIdAndUserId(cartId, userId).map(mapper::toDomain);
    }

    @Override
    public List<CartItem> findByCartIdsAndUserId(List<Long> cartIds, Long userId) {
        return queryDslRepository.findByCartIdsAndUserId(cartIds, userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CartItem> findExistingByProductIds(List<Long> productIds, Long userId) {
        return queryDslRepository.findExistingByProductIds(productIds, userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
