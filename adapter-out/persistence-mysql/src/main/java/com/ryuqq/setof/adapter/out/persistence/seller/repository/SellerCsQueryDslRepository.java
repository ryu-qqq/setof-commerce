package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerCsJpaEntity.sellerCsJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.condition.SellerCsConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerCsQueryDslRepository - 셀러 CS 정보 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class SellerCsQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerCsConditionBuilder conditionBuilder;

    public SellerCsQueryDslRepository(
            JPAQueryFactory queryFactory, SellerCsConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 셀러 CS 정보 조회.
     *
     * @param id CS 정보 ID
     * @return CS 정보 Optional
     */
    public Optional<SellerCsJpaEntity> findById(Long id) {
        SellerCsJpaEntity entity =
                queryFactory
                        .selectFrom(sellerCsJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID로 CS 정보 조회.
     *
     * @param sellerId 셀러 ID
     * @return CS 정보 Optional
     */
    public Optional<SellerCsJpaEntity> findBySellerId(Long sellerId) {
        SellerCsJpaEntity entity =
                queryFactory
                        .selectFrom(sellerCsJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID 존재 여부 확인.
     *
     * @param sellerId 셀러 ID
     * @return 존재 여부
     */
    public boolean existsBySellerId(Long sellerId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerCsJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }
}
