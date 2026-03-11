package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity.productGroupJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroup.condition.ProductGroupConditionBuilder;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * ProductGroupPriceQueryDslRepository - 상품그룹 가격 조회/갱신 QueryDSL 리포지토리.
 *
 * <p>새 스키마(setof)의 product_groups 테이블에서 가격 정보를 조회하고, 할인 계산 결과를 배치 UPDATE합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ProductGroupPriceQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ProductGroupConditionBuilder conditionBuilder;

    public ProductGroupPriceQueryDslRepository(
            JPAQueryFactory queryFactory, ProductGroupConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 타겟에 해당하는 상품그룹의 가격 정보 조회.
     *
     * @param targetType 대상 유형 (PRODUCT, CATEGORY, BRAND, SELLER)
     * @param targetId 대상 ID
     * @return 가격 정보 목록
     */
    public List<ProductGroupPriceRow> findPriceRowsByTarget(String targetType, long targetId) {
        BooleanExpression targetCondition = buildTargetCondition(targetType, targetId);
        if (targetCondition == null) {
            return List.of();
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                ProductGroupPriceRow.class,
                                productGroupJpaEntity.id,
                                productGroupJpaEntity.regularPrice,
                                productGroupJpaEntity.currentPrice))
                .from(productGroupJpaEntity)
                .where(targetCondition, conditionBuilder.statusNotDeleted())
                .fetch();
    }

    /**
     * 상품그룹 가격 정보 배치 UPDATE.
     *
     * <p>sale_price, discount_rate, direct_discount_rate, direct_discount_price를 갱신합니다.
     */
    public void updatePrice(
            long productGroupId,
            int salePrice,
            int discountRate,
            int directDiscountRate,
            int directDiscountPrice) {
        queryFactory
                .update(productGroupJpaEntity)
                .set(productGroupJpaEntity.salePrice, salePrice)
                .set(productGroupJpaEntity.discountRate, discountRate)
                .set(productGroupJpaEntity.directDiscountRate, directDiscountRate)
                .set(productGroupJpaEntity.directDiscountPrice, directDiscountPrice)
                .where(productGroupJpaEntity.id.eq(productGroupId))
                .execute();
    }

    private BooleanExpression buildTargetCondition(String targetType, long targetId) {
        return switch (targetType) {
            case "PRODUCT" -> productGroupJpaEntity.id.eq(targetId);
            case "CATEGORY" -> productGroupJpaEntity.categoryId.eq(targetId);
            case "BRAND" -> productGroupJpaEntity.brandId.eq(targetId);
            case "SELLER" -> productGroupJpaEntity.sellerId.eq(targetId);
            default -> null;
        };
    }
}
