package com.ryuqq.setof.storage.legacy.composite.wishlist.condition;

import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.wishlist.entity.QLegacyWishlistItemEntity.legacyWishlistItemEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupEntity;
import org.springframework.stereotype.Component;

/**
 * LegacyWebWishlistCompositeConditionBuilder - 찜 목록 복합 조회 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebWishlistCompositeConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyWishlistItemEntity.userId.eq(userId) : null;
    }

    public BooleanExpression cursorLessThan(Long lastFavoriteId) {
        return lastFavoriteId != null ? legacyWishlistItemEntity.id.lt(lastFavoriteId) : null;
    }

    public BooleanExpression onStock() {
        return legacyProductGroupEntity.soldOutYn.eq(LegacyProductGroupEntity.Yn.N);
    }
}
