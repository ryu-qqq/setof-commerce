package com.ryuqq.setof.adapter.out.persistence.cart.mapper;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;

/**
 * CartItemJpaEntityMapper - 장바구니 아이템 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartItemJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain CartItem 도메인 객체
     * @return CartItemJpaEntity
     */
    public CartItemJpaEntity toEntity(CartItem domain) {
        return CartItemJpaEntity.create(
                domain.idValue(),
                domain.memberIdValue(),
                domain.legacyUserIdValue(),
                domain.productGroupIdValue(),
                domain.productIdValue(),
                domain.sellerIdValue(),
                domain.quantityValue(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity CartItemJpaEntity
     * @return CartItem 도메인 객체
     */
    public CartItem toDomain(CartItemJpaEntity entity) {
        DeletionStatus deletionStatus =
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt());

        return CartItem.reconstitute(
                CartItemId.of(entity.getId()),
                MemberId.of(entity.getMemberId()),
                LegacyUserId.of(entity.getLegacyUserId()),
                ProductGroupId.of(entity.getProductGroupId()),
                ProductId.of(entity.getProductId()),
                entity.getSellerId() != null ? SellerId.of(entity.getSellerId()) : null,
                CartQuantity.of(entity.getQuantity()),
                deletionStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
