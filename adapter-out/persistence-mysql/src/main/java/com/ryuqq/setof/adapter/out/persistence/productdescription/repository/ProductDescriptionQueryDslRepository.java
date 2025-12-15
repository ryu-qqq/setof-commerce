package com.ryuqq.setof.adapter.out.persistence.productdescription.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.QProductDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.QProductDescriptionJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductDescriptionQueryDslRepository - ProductDescription QueryDSL Repository
 *
 * <p>QueryDSL 기반 동적 쿼리 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductDescriptionQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QProductDescriptionJpaEntity productDescription =
            QProductDescriptionJpaEntity.productDescriptionJpaEntity;
    private final QProductDescriptionImageJpaEntity productDescriptionImage =
            QProductDescriptionImageJpaEntity.productDescriptionImageJpaEntity;

    public ProductDescriptionQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상품설명 ID로 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 상품설명 Entity
     */
    public Optional<ProductDescriptionJpaEntity> findById(Long productDescriptionId) {
        ProductDescriptionJpaEntity result =
                queryFactory
                        .selectFrom(productDescription)
                        .where(productDescription.id.eq(productDescriptionId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 상품그룹 ID로 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 Entity
     */
    public Optional<ProductDescriptionJpaEntity> findByProductGroupId(Long productGroupId) {
        ProductDescriptionJpaEntity result =
                queryFactory
                        .selectFrom(productDescription)
                        .where(productDescription.productGroupId.eq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 상품설명 ID로 이미지 목록 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 이미지 Entity 목록
     */
    public List<ProductDescriptionImageJpaEntity> findImagesByProductDescriptionId(
            Long productDescriptionId) {
        return queryFactory
                .selectFrom(productDescriptionImage)
                .where(productDescriptionImage.productDescriptionId.eq(productDescriptionId))
                .orderBy(productDescriptionImage.displayOrder.asc())
                .fetch();
    }
}
