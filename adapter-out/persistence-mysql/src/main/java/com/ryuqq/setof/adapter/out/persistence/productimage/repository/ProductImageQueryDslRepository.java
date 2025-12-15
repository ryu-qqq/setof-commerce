package com.ryuqq.setof.adapter.out.persistence.productimage.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productimage.entity.ProductImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productimage.entity.QProductImageJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductImageQueryDslRepository - ProductImage QueryDSL Repository
 *
 * <p>복잡한 쿼리를 위한 QueryDSL 기반 Repository
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductImageQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QProductImageJpaEntity productImage =
            QProductImageJpaEntity.productImageJpaEntity;

    public ProductImageQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 이미지 조회
     *
     * @param productImageId 이미지 ID
     * @return 이미지 Entity (없으면 empty)
     */
    public Optional<ProductImageJpaEntity> findById(Long productImageId) {
        ProductImageJpaEntity result =
                queryFactory
                        .selectFrom(productImage)
                        .where(productImage.id.eq(productImageId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 상품그룹 ID로 이미지 목록 조회 (표시 순서 정렬)
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 Entity 목록
     */
    public List<ProductImageJpaEntity> findByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(productImage)
                .where(productImage.productGroupId.eq(productGroupId))
                .orderBy(productImage.displayOrder.asc())
                .fetch();
    }

    /**
     * 상품그룹 ID와 이미지 타입으로 이미지 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @param imageType 이미지 타입
     * @return 이미지 Entity 목록
     */
    public List<ProductImageJpaEntity> findByProductGroupIdAndImageType(
            Long productGroupId, String imageType) {
        return queryFactory
                .selectFrom(productImage)
                .where(
                        productImage.productGroupId.eq(productGroupId),
                        productImage.imageType.eq(imageType))
                .orderBy(productImage.displayOrder.asc())
                .fetch();
    }
}
