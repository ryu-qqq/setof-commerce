package com.ryuqq.setof.storage.legacy.wishlist.condition;

import static com.ryuqq.setof.storage.legacy.wishlist.entity.QLegacyWishlistItemEntity.legacyWishlistItemEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * LegacyWishlistItemConditionBuilder - 레거시 찜 항목 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWishlistItemConditionBuilder {

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyWishlistItemEntity.userId.eq(userId) : null;
    }

    /**
     * 상품그룹 ID 일치 조건.
     *
     * @param productGroupId 상품그룹 ID
     * @return BooleanExpression
     */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null
                ? legacyWishlistItemEntity.productGroupId.eq(productGroupId)
                : null;
    }

    /**
     * 찜 ID 커서 조건 - 커서 페이징용 (id < lastFavoriteId).
     *
     * @param lastFavoriteId 마지막으로 조회된 찜 ID (커서)
     * @return BooleanExpression
     */
    public BooleanExpression idLt(Long lastFavoriteId) {
        return lastFavoriteId != null ? legacyWishlistItemEntity.id.lt(lastFavoriteId) : null;
    }
}
