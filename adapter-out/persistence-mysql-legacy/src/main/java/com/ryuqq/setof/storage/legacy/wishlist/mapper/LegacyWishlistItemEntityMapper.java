package com.ryuqq.setof.storage.legacy.wishlist.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import com.ryuqq.setof.storage.legacy.wishlist.entity.LegacyWishlistItemEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyWishlistItemEntityMapper - 레거시 찜 항목 Entity &lt;-&gt; Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWishlistItemEntityMapper {

    public WishlistItem toDomain(LegacyWishlistItemEntity entity) {
        return WishlistItem.reconstitute(
                WishlistItemId.of(entity.getId()),
                LegacyMemberId.of(entity.getUserId()),
                null,
                ProductGroupId.of(entity.getProductGroupId()),
                DeletionStatus.active(),
                toInstant(entity.getInsertDate()));
    }

    public LegacyWishlistItemEntity toEntity(WishlistItem domain) {
        long userId = domain.legacyMemberIdValue();
        long productGroupId = domain.productGroupIdValue();

        if (domain.id().isNew()) {
            return LegacyWishlistItemEntity.create(userId, productGroupId);
        }

        LocalDateTime insertDate =
                domain.createdAt() != null
                        ? LocalDateTime.ofInstant(domain.createdAt(), ZoneId.systemDefault())
                        : LocalDateTime.now();

        String deleteYn = domain.isDeleted() ? "Y" : "N";
        return LegacyWishlistItemEntity.reconstitute(
                domain.idValue(),
                userId,
                productGroupId,
                deleteYn,
                insertDate,
                LocalDateTime.now());
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
