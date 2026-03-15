package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.mapper.CartItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartItemQueryDslRepository;
import com.ryuqq.setof.application.cart.port.out.query.CartItemQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CartItemQueryAdapter - 장바구니 아이템 Query 어댑터.
 *
 * <p>CartItemQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
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
public class CartItemQueryAdapter implements CartItemQueryPort {

    private final CartItemQueryDslRepository queryDslRepository;
    private final CartItemJpaEntityMapper mapper;

    public CartItemQueryAdapter(
            CartItemQueryDslRepository queryDslRepository, CartItemJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 장바구니 아이템 ID와 레거시 사용자 ID로 단건 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_user_id 컬럼에 매핑됩니다.
     *
     * @param cartId 장바구니 아이템 ID
     * @param userId 레거시 사용자 ID
     * @return CartItem Optional
     */
    @Override
    public Optional<CartItem> findByCartIdAndUserId(Long cartId, Long userId) {
        return queryDslRepository.findByIdAndLegacyUserId(cartId, userId).map(mapper::toDomain);
    }

    /**
     * 장바구니 아이템 ID 목록과 레거시 사용자 ID로 목록 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_user_id 컬럼에 매핑됩니다.
     *
     * @param cartIds 장바구니 아이템 ID 목록
     * @param userId 레거시 사용자 ID
     * @return CartItem 목록
     */
    @Override
    public List<CartItem> findByCartIdsAndUserId(List<Long> cartIds, Long userId) {
        List<CartItemJpaEntity> entities =
                queryDslRepository.findByIdsAndLegacyUserId(cartIds, userId);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 상품 ID 목록과 레거시 사용자 ID로 기존 장바구니 아이템 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_user_id 컬럼에 매핑됩니다.
     *
     * @param productIds 상품 ID 목록
     * @param userId 레거시 사용자 ID
     * @return CartItem 목록
     */
    @Override
    public List<CartItem> findExistingByProductIds(List<Long> productIds, Long userId) {
        List<CartItemJpaEntity> entities =
                queryDslRepository.findByProductIdsAndLegacyUserId(productIds, userId);
        return entities.stream().map(mapper::toDomain).toList();
    }
}
