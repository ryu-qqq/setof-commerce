package com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.QProductGroupImageJpaEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/**
 * ProductGroupImageQueryDslRepository - 상품그룹 이미지 QueryDSL Repository.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ProductGroupImageQueryDslRepository {

    private static final QProductGroupImageJpaEntity productGroupImage =
            QProductGroupImageJpaEntity.productGroupImageJpaEntity;

    private final JPAQueryFactory queryFactory;

    public ProductGroupImageQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 단일 상품그룹 ID로 이미지 목록 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 활성 이미지 목록 (sortOrder 오름차순)
     */
    public List<ProductGroupImageJpaEntity> findByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(productGroupImage)
                .where(
                        productGroupImage.productGroupId.eq(productGroupId),
                        productGroupImage.deletedAt.isNull())
                .orderBy(productGroupImage.sortOrder.asc())
                .fetch();
    }

    /**
     * 복수 상품그룹 ID로 이미지 목록 조회.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 활성 이미지 목록 (sortOrder 오름차순)
     */
    public List<ProductGroupImageJpaEntity> findByProductGroupIds(List<Long> productGroupIds) {
        return queryFactory
                .selectFrom(productGroupImage)
                .where(
                        productGroupImage.productGroupId.in(productGroupIds),
                        productGroupImage.deletedAt.isNull())
                .orderBy(productGroupImage.sortOrder.asc())
                .fetch();
    }

    /**
     * 복수 상품그룹 ID로 대표(THUMBNAIL) 이미지 ID 조회.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return productGroupId → thumbnailImageId 맵
     */
    public Map<Long, Long> findThumbnailImageIdsByProductGroupIds(List<Long> productGroupIds) {
        List<Tuple> results =
                queryFactory
                        .select(productGroupImage.productGroupId, productGroupImage.id)
                        .from(productGroupImage)
                        .where(
                                productGroupImage.productGroupId.in(productGroupIds),
                                productGroupImage.imageType.eq("THUMBNAIL"),
                                productGroupImage.deletedAt.isNull())
                        .fetch();

        return results.stream()
                .collect(
                        Collectors.toMap(
                                tuple -> tuple.get(productGroupImage.productGroupId),
                                tuple -> tuple.get(productGroupImage.id),
                                (existing, replacement) -> existing));
    }
}
