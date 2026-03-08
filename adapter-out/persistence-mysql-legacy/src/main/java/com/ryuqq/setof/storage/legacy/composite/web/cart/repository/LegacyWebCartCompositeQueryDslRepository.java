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

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.cart.dto.query.LegacyCartSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.cart.condition.LegacyWebCartCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartFlatQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.cart.dto.LegacyWebCartQueryDto;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * <p>11개 테이블 조인 (8 INNER + 3 LEFT) + flat 조회 후 Java groupBy로 옵션 집합 처리. Hibernate 6 호환성
 * 문제(ScrollableResults.get)로 QueryDSL transform 대신 Java groupBy 사용.
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
        List<LegacyWebCartFlatQueryDto> flatRows =
                queryFactory
                        .select(
                                Projections.constructor(
                                        LegacyWebCartFlatQueryDto.class,
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
                                        legacyProductGroupEntity.directDiscountRate,
                                        legacyProductGroupEntity.directDiscountPrice,
                                        legacyProductGroupEntity.discountRate,
                                        legacyCartEntity.quantity,
                                        legacyProductStockEntity.stockQuantity,
                                        legacyProductGroupImageEntity.imageUrl,
                                        legacyProductEntity.soldOutYn.stringValue(),
                                        legacyProductEntity.displayYn.stringValue(),
                                        legacyCategoryEntity.path,
                                        legacyOptionGroupEntity.id,
                                        legacyOptionDetailEntity.id,
                                        legacyOptionGroupEntity.optionName.stringValue(),
                                        legacyOptionDetailEntity.optionValue))
                        .from(legacyCartEntity)
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
                        .leftJoin(legacyProductOptionEntity)
                        .on(legacyProductOptionEntity.productId.eq(legacyCartEntity.productId))
                        .leftJoin(legacyOptionGroupEntity)
                        .on(legacyOptionGroupEntity.id.eq(legacyProductOptionEntity.optionGroupId))
                        .leftJoin(legacyOptionDetailEntity)
                        .on(
                                legacyOptionDetailEntity.id.eq(
                                        legacyProductOptionEntity.optionDetailId))
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.notDeleted(),
                                conditionBuilder.cursorLessThan(condition.lastCartId()))
                        .orderBy(legacyCartEntity.id.desc())
                        .fetch();

        return groupByCartId(flatRows, condition.getPageSizeOrDefault() + 1);
    }

    private List<LegacyWebCartQueryDto> groupByCartId(
            List<LegacyWebCartFlatQueryDto> flatRows, int limit) {
        Map<Long, LegacyWebCartQueryDto> grouped = new LinkedHashMap<>();

        for (LegacyWebCartFlatQueryDto row : flatRows) {
            LegacyWebCartQueryDto existing = grouped.get(row.cartId());
            if (existing == null) {
                if (grouped.size() >= limit) {
                    break;
                }
                Set<LegacyWebCartOptionQueryDto> options = new LinkedHashSet<>();
                addOptionIfPresent(options, row);
                existing =
                        new LegacyWebCartQueryDto(
                                row.cartId(),
                                row.brandId(),
                                row.brandName(),
                                row.productGroupId(),
                                row.productGroupName(),
                                row.sellerId(),
                                row.sellerName(),
                                row.productId(),
                                row.regularPrice(),
                                row.currentPrice(),
                                row.salePrice(),
                                row.directDiscountRate(),
                                row.directDiscountPrice(),
                                row.discountRate(),
                                row.quantity(),
                                row.stockQuantity(),
                                row.imageUrl(),
                                row.soldOutYn(),
                                row.displayYn(),
                                row.categoryPath(),
                                options);
                grouped.put(row.cartId(), existing);
            } else {
                addOptionIfPresent(existing.options(), row);
            }
        }

        return List.copyOf(grouped.values());
    }

    private void addOptionIfPresent(
            Set<LegacyWebCartOptionQueryDto> options, LegacyWebCartFlatQueryDto row) {
        if (row.optionGroupId() != null) {
            options.add(
                    new LegacyWebCartOptionQueryDto(
                            row.optionGroupId(),
                            row.optionDetailId(),
                            row.optionName(),
                            row.optionValue()));
        }
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
