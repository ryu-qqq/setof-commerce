package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionGroupJpaEntity.sellerOptionGroupJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QSellerOptionValueJpaEntity.sellerOptionValueJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * SellerOptionGroupQueryDslRepository - 셀러 옵션 그룹 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class SellerOptionGroupQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public SellerOptionGroupQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상품 그룹 ID로 활성 옵션 그룹 목록 조회.
     *
     * @param productGroupId 상품 그룹 ID
     * @return 활성 옵션 그룹 목록
     */
    public List<SellerOptionGroupJpaEntity> findByProductGroupId(Long productGroupId) {
        return queryFactory
                .selectFrom(sellerOptionGroupJpaEntity)
                .where(
                        sellerOptionGroupJpaEntity.productGroupId.eq(productGroupId),
                        sellerOptionGroupJpaEntity.deleted.isFalse())
                .orderBy(sellerOptionGroupJpaEntity.sortOrder.asc())
                .fetch();
    }

    /**
     * 옵션 그룹 ID 목록으로 활성 옵션 값 목록 조회.
     *
     * @param groupIds 옵션 그룹 ID 목록
     * @return 활성 옵션 값 목록
     */
    public List<SellerOptionValueJpaEntity> findValuesByGroupIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(sellerOptionValueJpaEntity)
                .where(
                        sellerOptionValueJpaEntity.sellerOptionGroupId.in(groupIds),
                        sellerOptionValueJpaEntity.deleted.isFalse())
                .orderBy(
                        sellerOptionValueJpaEntity.sellerOptionGroupId.asc(),
                        sellerOptionValueJpaEntity.sortOrder.asc())
                .fetch();
    }
}
