package com.ryuqq.setof.storage.legacy.wishlist.adapter;

import com.ryuqq.setof.application.wishlist.port.out.command.WishlistCommandPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.storage.legacy.wishlist.entity.LegacyWishlistItemEntity;
import com.ryuqq.setof.storage.legacy.wishlist.mapper.LegacyWishlistItemEntityMapper;
import com.ryuqq.setof.storage.legacy.wishlist.repository.LegacyWishlistItemJpaRepository;
import com.ryuqq.setof.storage.legacy.wishlist.repository.LegacyWishlistItemQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyWishlistItemCommandAdapter - 레거시 찜 항목 명령 Adapter.
 *
 * <p>Application Layer의 WishlistCommandPort를 구현합니다. 레거시 user_favorite 테이블은 delete_yn 컬럼이 없으므로, 도메인
 * 객체가 삭제 상태(isDeleted)이면 물리 삭제를 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.member.enabled", havingValue = "true")
public class LegacyWishlistItemCommandAdapter implements WishlistCommandPort {

    private final LegacyWishlistItemJpaRepository jpaRepository;
    private final LegacyWishlistItemQueryDslRepository queryDslRepository;
    private final LegacyWishlistItemEntityMapper mapper;

    public LegacyWishlistItemCommandAdapter(
            LegacyWishlistItemJpaRepository jpaRepository,
            LegacyWishlistItemQueryDslRepository queryDslRepository,
            LegacyWishlistItemEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(WishlistItem wishlistItem) {
        if (wishlistItem.isDeleted()) {
            hardDelete(wishlistItem);
            return wishlistItem.idValue();
        }

        LegacyWishlistItemEntity entity = mapper.toEntity(wishlistItem);
        LegacyWishlistItemEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    private void hardDelete(WishlistItem wishlistItem) {
        long userId = wishlistItem.legacyMemberIdValue();
        long productGroupId = wishlistItem.productGroupIdValue();

        Optional<LegacyWishlistItemEntity> target =
                queryDslRepository.findByUserIdAndProductGroupId(userId, productGroupId);

        target.ifPresent(entity -> jpaRepository.deleteAll(List.of(entity)));
    }
}
