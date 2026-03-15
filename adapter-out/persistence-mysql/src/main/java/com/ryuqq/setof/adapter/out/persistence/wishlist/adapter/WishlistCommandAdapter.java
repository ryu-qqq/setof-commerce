package com.ryuqq.setof.adapter.out.persistence.wishlist.adapter;

import com.ryuqq.setof.adapter.out.persistence.wishlist.entity.WishlistItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.wishlist.mapper.WishlistItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.wishlist.repository.WishlistItemJpaRepository;
import com.ryuqq.setof.application.wishlist.port.out.command.WishlistCommandPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * WishlistCommandAdapter - 찜 항목 Command 어댑터.
 *
 * <p>WishlistCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>활성화 조건: persistence.wishlist.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.wishlist.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class WishlistCommandAdapter implements WishlistCommandPort {

    private final WishlistItemJpaRepository jpaRepository;
    private final WishlistItemJpaEntityMapper mapper;

    public WishlistCommandAdapter(
            WishlistItemJpaRepository jpaRepository, WishlistItemJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 찜 항목 저장.
     *
     * @param wishlistItem 찜 항목 도메인 객체
     * @return 저장된 찜 항목 ID
     */
    @Override
    public Long persist(WishlistItem wishlistItem) {
        WishlistItemJpaEntity entity = mapper.toEntity(wishlistItem);
        WishlistItemJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
