package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerAddressJpaEntity.sellerAddressJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.condition.SellerAddressConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerAddressQueryDslRepository - 셀러 주소 QueryDSL 레포지토리.
 *
 * <p>복잡한 쿼리를 위한 QueryDSL 기반 조회를 제공합니다.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository.
 *
 * <p>PER-CND-001: BooleanExpression은 ConditionBuilder로 분리.
 */
@Repository
public class SellerAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerAddressConditionBuilder conditionBuilder;

    public SellerAddressQueryDslRepository(
            JPAQueryFactory queryFactory, SellerAddressConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 주소 조회.
     *
     * @param id 주소 ID
     * @return 주소 Optional
     */
    public Optional<SellerAddressJpaEntity> findById(Long id) {
        SellerAddressJpaEntity entity =
                queryFactory
                        .selectFrom(sellerAddressJpaEntity)
                        .where(conditionBuilder.idEq(id), notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID로 주소 조회.
     *
     * @param sellerId 셀러 ID
     * @return 주소 Optional
     */
    public Optional<SellerAddressJpaEntity> findBySellerId(Long sellerId) {
        SellerAddressJpaEntity entity =
                queryFactory
                        .selectFrom(sellerAddressJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID 존재 여부 확인.
     *
     * @param sellerId 셀러 ID
     * @return 존재하면 true
     */
    public boolean existsBySellerId(Long sellerId) {
        Integer fetchOne =
                queryFactory
                        .selectOne()
                        .from(sellerAddressJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), notDeleted())
                        .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return sellerAddressJpaEntity.deletedAt.isNull();
    }
}
