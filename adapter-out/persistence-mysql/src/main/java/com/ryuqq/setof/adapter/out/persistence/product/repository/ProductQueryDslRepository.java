package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.QProductGroupJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * ProductQueryDslRepository - Product QueryDSL Repository
 *
 * <p>복잡한 조건 조회를 위한 QueryDSL Repository
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 조건으로 ProductGroup 목록 조회
     *
     * @param sellerId 셀러 ID (nullable)
     * @param categoryId 카테고리 ID (nullable)
     * @param brandId 브랜드 ID (nullable)
     * @param name 상품그룹명 (nullable, like 검색)
     * @param status 상태 (nullable)
     * @param offset 오프셋
     * @param limit 제한
     * @return ProductGroup Entity 목록
     */
    public List<ProductGroupJpaEntity> findByConditions(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int offset,
            int limit) {
        QProductGroupJpaEntity productGroup = QProductGroupJpaEntity.productGroupJpaEntity;

        BooleanBuilder builder = buildConditions(sellerId, categoryId, brandId, name, status);

        return queryFactory
                .selectFrom(productGroup)
                .where(builder)
                .orderBy(productGroup.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 조건에 맞는 ProductGroup 총 개수 조회
     *
     * @param sellerId 셀러 ID (nullable)
     * @param categoryId 카테고리 ID (nullable)
     * @param brandId 브랜드 ID (nullable)
     * @param name 상품그룹명 (nullable)
     * @param status 상태 (nullable)
     * @return 총 개수
     */
    public long countByConditions(
            Long sellerId, Long categoryId, Long brandId, String name, String status) {
        QProductGroupJpaEntity productGroup = QProductGroupJpaEntity.productGroupJpaEntity;

        BooleanBuilder builder = buildConditions(sellerId, categoryId, brandId, name, status);

        Long count =
                queryFactory
                        .select(productGroup.count())
                        .from(productGroup)
                        .where(builder)
                        .fetchOne();

        return count != null ? count : 0L;
    }

    private BooleanBuilder buildConditions(
            Long sellerId, Long categoryId, Long brandId, String name, String status) {
        QProductGroupJpaEntity productGroup = QProductGroupJpaEntity.productGroupJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        // 삭제되지 않은 것만
        builder.and(productGroup.deletedAt.isNull());

        if (sellerId != null) {
            builder.and(productGroup.sellerId.eq(sellerId));
        }
        if (categoryId != null) {
            builder.and(productGroup.categoryId.eq(categoryId));
        }
        if (brandId != null) {
            builder.and(productGroup.brandId.eq(brandId));
        }
        if (name != null && !name.isBlank()) {
            builder.and(productGroup.name.containsIgnoreCase(name));
        }
        if (status != null && !status.isBlank()) {
            builder.and(productGroup.status.eq(status));
        }

        return builder;
    }
}
