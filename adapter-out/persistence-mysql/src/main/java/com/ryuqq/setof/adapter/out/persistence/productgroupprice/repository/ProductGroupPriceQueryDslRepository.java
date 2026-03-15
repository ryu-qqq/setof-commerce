package com.ryuqq.setof.adapter.out.persistence.productgroupprice.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity.productGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.QProductGroupPriceJpaEntity.productGroupPriceJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.ProductGroupPriceJpaEntity;
import com.ryuqq.setof.application.discount.port.out.query.LegacyProductGroupPriceQueryPort.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductGroupPriceQueryDslRepository - 상품 그룹 가격 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ProductGroupPriceQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProductGroupPriceQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상품그룹 ID로 가격 엔티티 단건 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 가격 엔티티 Optional
     */
    public Optional<ProductGroupPriceJpaEntity> findByProductGroupId(Long productGroupId) {
        ProductGroupPriceJpaEntity entity =
                queryFactory
                        .selectFrom(productGroupPriceJpaEntity)
                        .where(productGroupPriceJpaEntity.productGroupId.eq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 상품그룹 ID 목록으로 가격 엔티티 목록 조회.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 가격 엔티티 목록
     */
    public List<ProductGroupPriceJpaEntity> findByProductGroupIds(List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(productGroupPriceJpaEntity)
                .where(productGroupPriceJpaEntity.productGroupId.in(productGroupIds))
                .fetch();
    }

    /**
     * 여러 상품그룹의 가격 정보를 CASE WHEN으로 일괄 업데이트.
     *
     * @param updates 업데이트 대상 목록
     * @return 갱신된 행 수
     */
    public long updatePrices(List<ProductGroupPriceUpdateData> updates) {
        if (updates.isEmpty()) {
            return 0;
        }

        List<Long> ids = updates.stream().map(ProductGroupPriceUpdateData::productGroupId).toList();

        NumberExpression<Integer> salePriceCase =
                buildCaseExpression(updates, ProductGroupPriceUpdateData::salePrice);
        NumberExpression<Integer> discountRateCase =
                buildCaseExpression(updates, ProductGroupPriceUpdateData::discountRate);
        NumberExpression<Integer> directDiscountRateCase =
                buildCaseExpression(updates, ProductGroupPriceUpdateData::directDiscountRate);
        NumberExpression<Integer> directDiscountPriceCase =
                buildCaseExpression(updates, ProductGroupPriceUpdateData::directDiscountPrice);

        return queryFactory
                .update(productGroupPriceJpaEntity)
                .set(productGroupPriceJpaEntity.salePrice, salePriceCase)
                .set(productGroupPriceJpaEntity.discountRate, discountRateCase)
                .set(productGroupPriceJpaEntity.directDiscountRate, directDiscountRateCase)
                .set(productGroupPriceJpaEntity.directDiscountPrice, directDiscountPriceCase)
                .where(productGroupPriceJpaEntity.productGroupId.in(ids))
                .execute();
    }

    /**
     * 타겟 조건에 해당하는 상품그룹의 가격 정보 조회.
     *
     * <p>product_groups JOIN product_group_prices 로 정가, 현재가를 함께 반환합니다.
     *
     * @param targetType BRAND, SELLER, CATEGORY, PRODUCT
     * @param targetId 타겟 ID
     * @return ProductGroupPriceRow 목록
     */
    public List<ProductGroupPriceRow> findPriceRowsByTarget(String targetType, long targetId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ProductGroupPriceRow.class,
                                productGroupJpaEntity.id,
                                productGroupJpaEntity.regularPrice,
                                productGroupJpaEntity.currentPrice))
                .from(productGroupJpaEntity)
                .join(productGroupPriceJpaEntity)
                .on(productGroupPriceJpaEntity.productGroupId.eq(productGroupJpaEntity.id))
                .where(
                        targetCondition(targetType, targetId),
                        productGroupJpaEntity.deletedAt.isNull())
                .fetch();
    }

    private NumberExpression<Integer> buildCaseExpression(
            List<ProductGroupPriceUpdateData> updates,
            java.util.function.ToIntFunction<ProductGroupPriceUpdateData> valueExtractor) {
        CaseBuilder.Cases<Integer, NumberExpression<Integer>> caseBuilder = null;

        for (ProductGroupPriceUpdateData update : updates) {
            int value = valueExtractor.applyAsInt(update);
            if (caseBuilder == null) {
                caseBuilder =
                        new CaseBuilder()
                                .when(
                                        productGroupPriceJpaEntity.productGroupId.eq(
                                                update.productGroupId()))
                                .then(value);
            } else {
                caseBuilder =
                        caseBuilder
                                .when(
                                        productGroupPriceJpaEntity.productGroupId.eq(
                                                update.productGroupId()))
                                .then(value);
            }
        }

        return caseBuilder.otherwise(productGroupPriceJpaEntity.salePrice);
    }

    private BooleanExpression targetCondition(String targetType, long targetId) {
        return switch (targetType) {
            case "BRAND" -> productGroupJpaEntity.brandId.eq(targetId);
            case "SELLER" -> productGroupJpaEntity.sellerId.eq(targetId);
            case "CATEGORY" -> productGroupJpaEntity.categoryId.eq(targetId);
            case "PRODUCT" -> productGroupJpaEntity.id.eq(targetId);
            default -> throw new IllegalArgumentException("지원하지 않는 타겟 유형: " + targetType);
        };
    }
}
