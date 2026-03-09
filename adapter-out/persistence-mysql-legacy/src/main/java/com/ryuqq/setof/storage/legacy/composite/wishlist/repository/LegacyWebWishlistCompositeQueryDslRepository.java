package com.ryuqq.setof.storage.legacy.composite.wishlist.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.wishlist.entity.QLegacyWishlistItemEntity.legacyWishlistItemEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import com.ryuqq.setof.storage.legacy.composite.wishlist.condition.LegacyWebWishlistCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.wishlist.dto.LegacyWebWishlistQueryDto;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupImageEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebWishlistCompositeQueryDslRepository - 찜 목록 복합 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>4개 테이블 조인: user_favorite + product_group + product_group_image + brand.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebWishlistCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebWishlistCompositeConditionBuilder conditionBuilder;

    public LegacyWebWishlistCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebWishlistCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 찜 목록 커서 기반 복합 조회.
     *
     * <p>user_favorite INNER JOIN product_group, product_group_image, brand. 커서(lastFavoriteId) 기준
     * DESC 정렬, fetchSize(size+1)로 hasNext 판단.
     *
     * @param criteria 검색 조건
     * @return 찜 항목 목록 (fetchSize만큼)
     */
    public List<LegacyWebWishlistQueryDto> fetchMyFavorites(WishlistSearchCriteria criteria) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebWishlistQueryDto.class,
                                legacyWishlistItemEntity.id,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.sellerId,
                                legacyProductGroupEntity.productGroupName,
                                legacyBrandEntity.id,
                                legacyBrandEntity.brandName,
                                legacyProductGroupImageEntity.imageUrl,
                                legacyProductGroupEntity.regularPrice,
                                legacyProductGroupEntity.currentPrice,
                                legacyProductGroupEntity.soldOutYn.stringValue(),
                                legacyProductGroupEntity.displayYn.stringValue(),
                                legacyProductGroupEntity.insertDate))
                .from(legacyWishlistItemEntity)
                .innerJoin(legacyProductGroupEntity)
                .on(legacyProductGroupEntity.id.eq(legacyWishlistItemEntity.productGroupId))
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity.productGroupId.eq(
                                legacyProductGroupEntity.id),
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .where(
                        conditionBuilder.userIdEq(criteria.memberId().value()),
                        conditionBuilder.onStock(),
                        conditionBuilder.cursorLessThan(criteria.cursor()))
                .orderBy(legacyWishlistItemEntity.id.desc())
                .limit(criteria.fetchSize())
                .fetch();
    }

    /**
     * 찜 개수 조회 (재고 있는 상품만).
     *
     * @param userId 사용자 ID
     * @return 찜 개수
     */
    public long countByUserId(Long userId) {
        Long count =
                queryFactory
                        .select(legacyWishlistItemEntity.id.count())
                        .from(legacyWishlistItemEntity)
                        .innerJoin(legacyProductGroupEntity)
                        .on(legacyProductGroupEntity.id.eq(legacyWishlistItemEntity.productGroupId))
                        .where(conditionBuilder.userIdEq(userId), conditionBuilder.onStock())
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
