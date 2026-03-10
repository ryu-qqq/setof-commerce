package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionGroupJpaEntity.sellerOptionGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionValueJpaEntity.sellerOptionValueJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroup.condition.ProductGroupConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductGroupQueryDslRepository - 상품 그룹 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class ProductGroupQueryDslRepository {

    private static final QProductGroupJpaEntity productGroup =
            QProductGroupJpaEntity.productGroupJpaEntity;

    private final JPAQueryFactory queryFactory;
    private final ProductGroupConditionBuilder conditionBuilder;

    public ProductGroupQueryDslRepository(
            JPAQueryFactory queryFactory, ProductGroupConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 상품 그룹 조회 (삭제 상태 제외).
     *
     * @param id 상품 그룹 ID
     * @return 상품 그룹 Optional
     */
    public Optional<ProductGroupJpaEntity> findById(Long id) {
        ProductGroupJpaEntity entity =
                queryFactory
                        .selectFrom(productGroup)
                        .where(conditionBuilder.idEq(id), conditionBuilder.statusNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 상품 그룹 목록 조회.
     *
     * @param ids 상품 그룹 ID 목록
     * @return 상품 그룹 목록
     */
    public List<ProductGroupJpaEntity> findByIds(List<Long> ids) {
        return queryFactory.selectFrom(productGroup).where(conditionBuilder.idIn(ids)).fetch();
    }

    /**
     * 상품 그룹 ID로 옵션 그룹 목록 조회 (삭제 제외).
     *
     * @param productGroupId 상품 그룹 ID
     * @return 옵션 그룹 목록
     */
    public List<SellerOptionGroupJpaEntity> findOptionGroupsByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(sellerOptionGroupJpaEntity)
                .where(
                        sellerOptionGroupJpaEntity.productGroupId.eq(productGroupId),
                        sellerOptionGroupJpaEntity.deleted.isFalse())
                .orderBy(sellerOptionGroupJpaEntity.sortOrder.asc())
                .fetch();
    }

    /**
     * 상품 그룹 ID 목록으로 옵션 그룹 목록 조회 (삭제 제외).
     *
     * @param productGroupIds 상품 그룹 ID 목록
     * @return 옵션 그룹 목록
     */
    public List<SellerOptionGroupJpaEntity> findOptionGroupsByProductGroupIds(
            List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(sellerOptionGroupJpaEntity)
                .where(
                        sellerOptionGroupJpaEntity.productGroupId.in(productGroupIds),
                        sellerOptionGroupJpaEntity.deleted.isFalse())
                .orderBy(
                        sellerOptionGroupJpaEntity.productGroupId.asc(),
                        sellerOptionGroupJpaEntity.sortOrder.asc())
                .fetch();
    }

    /**
     * 옵션 그룹 ID 목록으로 옵션 값 목록 조회 (삭제 제외).
     *
     * @param optionGroupIds 옵션 그룹 ID 목록
     * @return 옵션 값 목록
     */
    public List<SellerOptionValueJpaEntity> findOptionValuesByOptionGroupIds(
            List<Long> optionGroupIds) {
        if (optionGroupIds == null || optionGroupIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(sellerOptionValueJpaEntity)
                .where(
                        sellerOptionValueJpaEntity.sellerOptionGroupId.in(optionGroupIds),
                        sellerOptionValueJpaEntity.deleted.isFalse())
                .orderBy(
                        sellerOptionValueJpaEntity.sellerOptionGroupId.asc(),
                        sellerOptionValueJpaEntity.sortOrder.asc())
                .fetch();
    }
}
