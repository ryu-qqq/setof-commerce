package com.ryuqq.setof.storage.legacy.cart.mapper;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.storage.legacy.cart.entity.LegacyCartEntity;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyCartEntityMapper - 레거시 장바구니 Entity &lt;-&gt; Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * <p>Domain CartItem &lt;-&gt; LegacyCartEntity 양방향 변환을 담당합니다. - toEntity: CartItem(신규/복원) →
 * LegacyCartEntity (INSERT용) - toDomain: LegacyCartEntity → CartItem (조회 후 도메인 복원용)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyCartEntityMapper {

    /**
     * CartItem 도메인 객체를 LegacyCartEntity로 변환합니다.
     *
     * <p>신규 항목(isNew)이면 id=null로 생성하여 JPA auto-increment INSERT를 유도합니다.
     *
     * @param domain CartItem 도메인 객체
     * @return LegacyCartEntity
     */
    public LegacyCartEntity toEntity(CartItem domain) {
        LocalDateTime now = LocalDateTime.now();
        Long id = domain.isNew() ? null : domain.idValue();
        Yn deleteYn = domain.isDeleted() ? Yn.Y : Yn.N;

        LocalDateTime insertDate =
                domain.createdAt() != null
                        ? LocalDateTime.ofInstant(domain.createdAt(), ZoneId.systemDefault())
                        : now;
        LocalDateTime updateDate =
                domain.updatedAt() != null
                        ? LocalDateTime.ofInstant(domain.updatedAt(), ZoneId.systemDefault())
                        : now;

        return LegacyCartEntity.create(
                id,
                domain.legacyUserIdValue(),
                domain.productGroupIdValue(),
                domain.productIdValue(),
                domain.quantityValue(),
                domain.sellerIdValue(),
                deleteYn,
                insertDate,
                updateDate);
    }

    /**
     * LegacyCartEntity를 CartItem 도메인 객체로 변환합니다.
     *
     * <p>Command 흐름에서 기존 엔티티를 도메인으로 복원하여 수량 변경·삭제 여부를 판단하는 데 사용합니다.
     *
     * @param entity LegacyCartEntity
     * @return CartItem 도메인 객체
     */
    public CartItem toDomain(LegacyCartEntity entity) {
        boolean deleted = entity.getDeleteYn() == Yn.Y;
        Instant deletedAt = deleted ? toInstant(entity.getUpdateDate()) : null;
        DeletionStatus deletionStatus =
                deleted ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();

        return CartItem.reconstitute(
                CartItemId.of(entity.getId()),
                null,
                LegacyUserId.of(entity.getUserId()),
                ProductGroupId.of(entity.getProductGroupId()),
                ProductId.of(entity.getProductId()),
                SellerId.of(entity.getSellerId()),
                CartQuantity.of(entity.getQuantity()),
                deletionStatus,
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
