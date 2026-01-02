package com.ryuqq.setof.adapter.out.persistence.cart.mapper;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartJpaEntity;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * CartJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Cart 간 변환을 담당합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>CartJpaEntity와 CartItemJpaEntity 분리 변환
 *   <li>Parent-Child 관계를 FK 필드로 연결
 * </ul>
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Cart -> CartJpaEntity (저장용)
 *   <li>CartJpaEntity + Items -> Cart (조회용)
 *   <li>CartItem <-> CartItemJpaEntity 변환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartJpaEntityMapper {

    /**
     * Domain -> Entity 변환 (저장용, Parent만)
     *
     * @param domain Cart 도메인
     * @return CartJpaEntity (items 미포함)
     */
    public CartJpaEntity toEntity(Cart domain) {
        String memberIdString = domain.memberId().toString();
        if (domain.isNew()) {
            return CartJpaEntity.forNew(memberIdString, domain.createdAt(), domain.updatedAt());
        }
        return CartJpaEntity.of(
                domain.id().value(), memberIdString, domain.createdAt(), domain.updatedAt());
    }

    /**
     * Entity + Items -> Domain 변환 (조회용)
     *
     * @param entity CartJpaEntity
     * @param itemEntities CartItemJpaEntity 목록
     * @return Cart 도메인
     */
    public Cart toDomain(CartJpaEntity entity, List<CartItemJpaEntity> itemEntities) {
        List<CartItem> items = toItems(itemEntities);
        UUID memberId = UUID.fromString(entity.getMemberId());

        return Cart.restore(
                CartId.of(entity.getId()),
                memberId,
                items,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * Domain Items -> Item Entities 변환 (저장용)
     *
     * @param cartId Parent Cart ID
     * @param items CartItem 도메인 목록
     * @return CartItemJpaEntity 목록
     */
    public List<CartItemJpaEntity> toItemEntities(Long cartId, List<CartItem> items) {
        return items.stream().map(item -> toItemEntity(cartId, item)).toList();
    }

    /**
     * Item Entities -> Domain Items 변환 (조회용)
     *
     * @param entities CartItemJpaEntity 목록
     * @return CartItem 도메인 목록
     */
    public List<CartItem> toItems(List<CartItemJpaEntity> entities) {
        return entities.stream().map(this::toItem).toList();
    }

    private CartItemJpaEntity toItemEntity(Long cartId, CartItem item) {
        if (item.isNew()) {
            return CartItemJpaEntity.forNew(
                    cartId,
                    item.productStockId(),
                    item.productId(),
                    item.productGroupId(),
                    item.sellerId(),
                    item.quantity(),
                    item.unitPrice(),
                    item.selected(),
                    item.addedAt(),
                    item.deletedAt());
        }
        return CartItemJpaEntity.of(
                item.id().value(),
                cartId,
                item.productStockId(),
                item.productId(),
                item.productGroupId(),
                item.sellerId(),
                item.quantity(),
                item.unitPrice(),
                item.selected(),
                item.addedAt(),
                item.deletedAt());
    }

    private CartItem toItem(CartItemJpaEntity entity) {
        return CartItem.restore(
                CartItemId.of(entity.getId()),
                entity.getProductStockId(),
                entity.getProductId(),
                entity.getProductGroupId(),
                entity.getSellerId(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.isSelected(),
                entity.getAddedAt(),
                entity.getDeletedAt());
    }
}
