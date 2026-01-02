package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.entity.QProductJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductSkuQueryDslRepository - Product(SKU) QueryDSL Repository
 *
 * <p>QueryDSL 기반 Product(SKU) 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByProductGroupId(Long productGroupId): 상품그룹 ID로 SKU 목록 조회
 *   <li>findByProductGroupIds(List ids): 여러 상품그룹 ID로 SKU 목록 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductSkuQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QProductJpaEntity qProduct = QProductJpaEntity.productJpaEntity;

    public ProductSkuQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Product(SKU) 단건 조회 (삭제되지 않은 것만)
     *
     * @param id Product ID
     * @return ProductJpaEntity (Optional)
     */
    public Optional<ProductJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qProduct)
                        .where(qProduct.id.eq(id), qProduct.deletedAt.isNull())
                        .fetchOne());
    }

    /**
     * 상품그룹 ID로 Product(SKU) 목록 조회 (삭제되지 않은 것만)
     *
     * @param productGroupId 상품그룹 ID
     * @return ProductJpaEntity 목록
     */
    public List<ProductJpaEntity> findByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(qProduct)
                .where(qProduct.productGroupId.eq(productGroupId), qProduct.deletedAt.isNull())
                .fetch();
    }

    /**
     * 여러 상품그룹 ID로 Product(SKU) 목록 조회 (삭제되지 않은 것만)
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return ProductJpaEntity 목록
     */
    public List<ProductJpaEntity> findByProductGroupIds(List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .selectFrom(qProduct)
                .where(qProduct.productGroupId.in(productGroupIds), qProduct.deletedAt.isNull())
                .fetch();
    }

    /**
     * 상품그룹 ID로 Product(SKU) 존재 여부 확인 (삭제되지 않은 것만)
     *
     * @param productGroupId 상품그룹 ID
     * @return 존재 여부
     */
    public boolean existsByProductGroupId(Long productGroupId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qProduct)
                        .where(
                                qProduct.productGroupId.eq(productGroupId),
                                qProduct.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }
}
