package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.mapper.CartJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartJpaRepository;
import com.ryuqq.setof.application.cart.port.out.command.CartPersistencePort;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * CartPersistenceAdapter - Cart 영속화 Adapter
 *
 * <p>Application Layer의 CartPersistencePort 구현체입니다.
 *
 * <p><strong>전략:</strong>
 *
 * <ul>
 *   <li>Cart는 upsert 방식으로 저장
 *   <li>CartItem은 delete-insert 방식으로 동기화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartPersistenceAdapter implements CartPersistencePort {

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CartItemQueryDslRepository cartItemQueryDslRepository;
    private final CartJpaEntityMapper mapper;

    public CartPersistenceAdapter(
            CartJpaRepository cartJpaRepository,
            CartItemJpaRepository cartItemJpaRepository,
            CartItemQueryDslRepository cartItemQueryDslRepository,
            CartJpaEntityMapper mapper) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
        this.cartItemQueryDslRepository = cartItemQueryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public CartId persist(Cart cart) {
        // Cart Entity 저장 (upsert)
        CartJpaEntity cartEntity = mapper.toEntity(cart);
        CartJpaEntity savedCart = cartJpaRepository.save(cartEntity);
        Long cartId = savedCart.getId();

        // 기존 아이템과 새 아이템 비교하여 동기화
        // 소프트 딜리트된 아이템도 items()에 포함되어 있으므로 정상 동기화됨
        syncCartItems(cartId, cart.items());

        return CartId.of(cartId);
    }

    /**
     * 장바구니 아이템 동기화
     *
     * <p>Domain의 items와 DB의 items를 비교하여: - 새로운 아이템: INSERT - 기존 아이템: UPDATE - 삭제된 아이템: DELETE
     */
    private void syncCartItems(Long cartId, List<CartItem> domainItems) {
        // 현재 DB의 아이템 조회 (QueryDslRepository 사용)
        List<CartItemJpaEntity> existingEntities = cartItemQueryDslRepository.findByCartId(cartId);

        // 삭제할 아이템 ID 수집
        Set<Long> domainItemIds = new HashSet<>();
        for (CartItem item : domainItems) {
            if (item.id() != null) {
                domainItemIds.add(item.id().value());
            }
        }

        List<Long> itemsToDelete =
                existingEntities.stream()
                        .map(CartItemJpaEntity::getId)
                        .filter(id -> !domainItemIds.contains(id))
                        .toList();

        // 삭제 (QueryDslRepository 사용)
        if (!itemsToDelete.isEmpty()) {
            cartItemQueryDslRepository.deleteByCartIdAndIdIn(cartId, itemsToDelete);
        }

        // 신규/업데이트 아이템 저장
        List<CartItemJpaEntity> itemEntities = mapper.toItemEntities(cartId, domainItems);
        cartItemJpaRepository.saveAll(itemEntities);
    }
}
