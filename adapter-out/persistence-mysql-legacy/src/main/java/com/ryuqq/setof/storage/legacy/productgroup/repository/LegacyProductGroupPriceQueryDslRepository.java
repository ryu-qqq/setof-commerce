package com.ryuqq.setof.storage.legacy.productgroup.repository;

import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.productgroup.dto.LegacyProductGroupPriceRow;
import com.ryuqq.setof.storage.legacy.productgroup.dto.LegacyProductGroupPriceUpdateRow;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * LegacyProductGroupPriceQueryDslRepository - 레거시 상품그룹 가격 조회/갱신 QueryDSL 레포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyProductGroupPriceQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyProductGroupPriceQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 타겟 조건에 해당하는 상품그룹의 가격 정보 조회.
     *
     * @param targetType BRAND, SELLER, CATEGORY, PRODUCT
     * @param targetId 타겟 ID
     * @return PriceRow 목록
     */
    public List<LegacyProductGroupPriceRow> findPriceRowsByTarget(
            String targetType, long targetId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyProductGroupPriceRow.class,
                                legacyProductGroupEntity.id,
                                legacyProductGroupEntity.regularPrice,
                                legacyProductGroupEntity.currentPrice))
                .from(legacyProductGroupEntity)
                .where(
                        targetCondition(targetType, targetId),
                        legacyProductGroupEntity.deleteYn.eq(LegacyProductGroupEntity.Yn.N))
                .fetch();
    }

    /**
     * 여러 상품그룹의 가격 필드를 CASE WHEN으로 한 번에 일괄 업데이트.
     *
     * @param updates 업데이트 대상 목록
     * @return 갱신된 행 수
     */
    public long updatePrices(List<LegacyProductGroupPriceUpdateRow> updates) {
        if (updates.isEmpty()) {
            return 0;
        }

        List<Long> ids =
                updates.stream().map(LegacyProductGroupPriceUpdateRow::productGroupId).toList();

        NumberExpression<Integer> salePriceCase =
                buildCaseExpression(updates, LegacyProductGroupPriceUpdateRow::salePrice);
        NumberExpression<Integer> discountRateCase =
                buildCaseExpression(updates, LegacyProductGroupPriceUpdateRow::discountRate);
        NumberExpression<Integer> directDiscountRateCase =
                buildCaseExpression(updates, LegacyProductGroupPriceUpdateRow::directDiscountRate);
        NumberExpression<Integer> directDiscountPriceCase =
                buildCaseExpression(updates, LegacyProductGroupPriceUpdateRow::directDiscountPrice);

        return queryFactory
                .update(legacyProductGroupEntity)
                .set(legacyProductGroupEntity.salePrice, salePriceCase)
                .set(legacyProductGroupEntity.discountRate, discountRateCase)
                .set(legacyProductGroupEntity.directDiscountRate, directDiscountRateCase)
                .set(legacyProductGroupEntity.directDiscountPrice, directDiscountPriceCase)
                .where(legacyProductGroupEntity.id.in(ids))
                .execute();
    }

    private NumberExpression<Integer> buildCaseExpression(
            List<LegacyProductGroupPriceUpdateRow> updates,
            java.util.function.ToIntFunction<LegacyProductGroupPriceUpdateRow> valueExtractor) {
        CaseBuilder.Cases<Integer, NumberExpression<Integer>> caseBuilder = null;

        for (LegacyProductGroupPriceUpdateRow update : updates) {
            int value = valueExtractor.applyAsInt(update);
            if (caseBuilder == null) {
                caseBuilder =
                        new CaseBuilder()
                                .when(legacyProductGroupEntity.id.eq(update.productGroupId()))
                                .then(value);
            } else {
                caseBuilder =
                        caseBuilder
                                .when(legacyProductGroupEntity.id.eq(update.productGroupId()))
                                .then(value);
            }
        }

        return caseBuilder.otherwise(legacyProductGroupEntity.salePrice);
    }

    private BooleanExpression targetCondition(String targetType, long targetId) {
        return switch (targetType) {
            case "BRAND" -> legacyProductGroupEntity.brandId.eq(targetId);
            case "SELLER" -> legacyProductGroupEntity.sellerId.eq(targetId);
            case "CATEGORY" -> legacyProductGroupEntity.categoryId.eq(targetId);
            case "PRODUCT" -> legacyProductGroupEntity.id.eq(targetId);
            default -> throw new IllegalArgumentException("지원하지 않는 타겟 유형: " + targetType);
        };
    }
}
