package com.ryuqq.setof.storage.legacy.composite.web.cart.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.cart.entity.QLegacyCartEntity.legacyCartEntity;
import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;
import static com.ryuqq.setof.storage.legacy.option.entity.QLegacyOptionDetailEntity.legacyOptionDetailEntity;
import static com.ryuqq.setof.storage.legacy.option.entity.QLegacyOptionGroupEntity.legacyOptionGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductEntity.legacyProductEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductOptionEntity.legacyProductOptionEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductStockEntity.legacyProductStockEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.cart.dto.query.LegacyCartSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.cart.condition.LegacyWebCartCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebCartCompositeQueryDslRepository - 레거시 장바구니 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * <p>11개 테이블 조인 (8 INNER + 3 LEFT) + GroupBy로 옵션 집합 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebCartCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebCartCompositeConditionBuilder conditionBuilder;

    public LegacyWebCartCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebCartCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 장바구니 목록 조회 (커서 페이징, 옵션 포함).
     *
     * <p>11개 테이블 조인:
     *
     * <ul>
     *   <li>INNER JOIN: product_group, product, seller, product_group_image, product_stock, brand,
     *       category
     *   <li>LEFT JOIN: product_option, option_group, option_detail
     * </ul>
     *
     * @param condition 검색 조건
     * @return 장바구니 목록
     */
    public List<LegacyWebCartQueryDto> fetchCarts(LegacyCartSearchCondition condition) {
        return queryFactory
                .from(legacyCartEntity)
                // INNER JOINs
                .innerJoin(legacyProductGroupEntity)
                .on(legacyProductGroupEntity.id.eq(legacyCartEntity.productGroupId))
                .innerJoin(legacyProductEntity)
                .on(legacyProductEntity.id.eq(legacyCartEntity.productId))
                .innerJoin(legacySellerEntity)
                .on(legacySellerEntity.id.eq(legacyProductGroupEntity.sellerId))
                .innerJoin(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity.productGroupId.eq(
                                legacyProductGroupEntity.id),
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .innerJoin(legacyProductStockEntity)
                .on(legacyProductStockEntity.productId.eq(legacyCartEntity.productId))
                .innerJoin(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .innerJoin(legacyCategoryEntity)
                .on(legacyCategoryEntity.id.eq(legacyProductGroupEntity.categoryId))
                // LEFT JOINs for options
                .leftJoin(legacyProductOptionEntity)
                .on(legacyProductOptionEntity.productId.eq(legacyCartEntity.productId))
                .leftJoin(legacyOptionGroupEntity)
                .on(legacyOptionGroupEntity.id.eq(legacyProductOptionEntity.optionGroupId))
                .leftJoin(legacyOptionDetailEntity)
                .on(legacyOptionDetailEntity.id.eq(legacyProductOptionEntity.optionDetailId))
                // WHERE
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.notDeleted(),
                        conditionBuilder.cursorLessThan(condition.lastCartId()))
                // ORDER & LIMIT
                .orderBy(legacyCartEntity.id.desc())
                .limit(condition.getPageSizeOrDefault() + 1L)
                // GROUP BY with Transform
                .transform(
                        GroupBy.groupBy(legacyCartEntity.id)
                                .list(
                                        Projections.constructor(
                                                LegacyWebCartQueryDto.class,
                                                legacyCartEntity.id,
                                                legacyBrandEntity.id,
                                                legacyBrandEntity.brandName,
                                                legacyProductGroupEntity.id,
                                                legacyProductGroupEntity.productGroupName,
                                                legacySellerEntity.id,
                                                legacySellerEntity.sellerName,
                                                legacyProductEntity.id,
                                                legacyProductGroupEntity.regularPrice,
                                                legacyProductGroupEntity.currentPrice,
                                                legacyProductGroupEntity.salePrice,
                                                legacyCartEntity.quantity,
                                                legacyProductStockEntity.stockQuantity,
                                                legacyProductGroupImageEntity.imageUrl,
                                                legacyProductEntity.soldOutYn.stringValue(),
                                                legacyProductEntity.displayYn.stringValue(),
                                                legacyCategoryEntity.path,
                                                GroupBy.set(
                                                        Projections.constructor(
                                                                LegacyWebCartOptionQueryDto.class,
                                                                legacyOptionGroupEntity.id,
                                                                legacyOptionDetailEntity.id,
                                                                legacyOptionGroupEntity.optionName
                                                                        .stringValue(),
                                                                legacyOptionDetailEntity
                                                                        .optionValue)))));
    }

    /**
     * 장바구니 개수 조회.
     *
     * @param userId 사용자 ID
     * @return 장바구니 개수
     */
    public long countCarts(Long userId) {
        Long count =
                queryFactory
                        .select(legacyCartEntity.id.countDistinct())
                        .from(legacyCartEntity)
                        .where(conditionBuilder.userIdEq(userId), conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 장바구니 총 개수 조회 (카운트 API용).
     *
     * @param condition 검색 조건
     * @return 장바구니 총 개수
     */
    public long countCartsForUser(LegacyCartSearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyCartEntity.id.countDistinct())
                        .from(legacyCartEntity)
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
