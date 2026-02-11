package com.ryuqq.setof.storage.legacy.composite.web.user.condition;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserFavoriteEntity.legacyUserFavoriteEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupEntity;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserFavoriteEntity;
import org.springframework.stereotype.Component;

/**
 * 레거시 찜 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserFavoriteConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyUserFavoriteEntity.userId.eq(userId) : null;
    }

    public BooleanExpression favoriteIdLt(Long lastFavoriteId) {
        return lastFavoriteId != null ? legacyUserFavoriteEntity.id.lt(lastFavoriteId) : null;
    }

    public BooleanExpression notDeleted() {
        return legacyUserFavoriteEntity.deleteYn.eq(LegacyUserFavoriteEntity.Yn.N);
    }

    public BooleanExpression productGroupJoinCondition() {
        return legacyProductGroupEntity.id.eq(legacyUserFavoriteEntity.productGroupId);
    }

    public BooleanExpression productGroupImageJoinCondition() {
        return legacyProductGroupImageEntity
                .productGroupId
                .eq(legacyProductGroupEntity.id)
                .and(
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN))
                .and(legacyProductGroupImageEntity.deleteYn.eq(LegacyProductGroupImageEntity.Yn.N));
    }

    public BooleanExpression brandJoinCondition() {
        return legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId);
    }

    public BooleanExpression notSoldOut() {
        return legacyProductGroupEntity.soldOutYn.eq(LegacyProductGroupEntity.Yn.N);
    }

    public BooleanExpression displayed() {
        return legacyProductGroupEntity.displayYn.eq(LegacyProductGroupEntity.Yn.Y);
    }
}
