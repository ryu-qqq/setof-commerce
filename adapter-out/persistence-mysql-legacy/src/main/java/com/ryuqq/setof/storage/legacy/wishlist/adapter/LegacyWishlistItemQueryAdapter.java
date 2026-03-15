package com.ryuqq.setof.storage.legacy.wishlist.adapter;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.port.out.query.WishlistQueryPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.wishlist.dto.LegacyWebWishlistQueryDto;
import com.ryuqq.setof.storage.legacy.composite.wishlist.mapper.LegacyWebWishlistMapper;
import com.ryuqq.setof.storage.legacy.composite.wishlist.repository.LegacyWebWishlistCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.wishlist.mapper.LegacyWishlistItemEntityMapper;
import com.ryuqq.setof.storage.legacy.wishlist.repository.LegacyWishlistItemQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyWishlistItemQueryAdapter - 레거시 찜 항목 조회 Adapter.
 *
 * <p>Application Layer의 WishlistQueryPort를 구현합니다. 단순 조회는 기본 Repository, 복합 조회(상품 정보 JOIN)는
 * Composite Repository를 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.member.enabled", havingValue = "true")
public class LegacyWishlistItemQueryAdapter implements WishlistQueryPort {

    private final LegacyWishlistItemQueryDslRepository repository;
    private final LegacyWishlistItemEntityMapper mapper;
    private final LegacyWebWishlistCompositeQueryDslRepository compositeRepository;
    private final LegacyWebWishlistMapper compositeMapper;

    public LegacyWishlistItemQueryAdapter(
            LegacyWishlistItemQueryDslRepository repository,
            LegacyWishlistItemEntityMapper mapper,
            LegacyWebWishlistCompositeQueryDslRepository compositeRepository,
            LegacyWebWishlistMapper compositeMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.compositeRepository = compositeRepository;
        this.compositeMapper = compositeMapper;
    }

    @Override
    public Optional<WishlistItem> findByUserIdAndProductGroupId(Long userId, long productGroupId) {
        return repository
                .findByUserIdAndProductGroupId(userId, productGroupId)
                .map(mapper::toDomain);
    }

    @Override
    public List<WishlistItem> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<WishlistItemResult> fetchSlice(WishlistSearchCriteria criteria) {
        List<LegacyWebWishlistQueryDto> dtos = compositeRepository.fetchMyFavorites(criteria);
        return compositeMapper.toResults(dtos);
    }

    @Override
    public long countByUserId(Long userId) {
        return compositeRepository.countByUserId(userId);
    }
}
