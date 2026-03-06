package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerBusinessInfoJpaEntity.sellerBusinessInfoJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.condition.SellerBusinessInfoConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerBusinessInfoJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerBusinessInfoQueryDslRepository - 셀러 사업자 정보 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class SellerBusinessInfoQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerBusinessInfoConditionBuilder conditionBuilder;

    public SellerBusinessInfoQueryDslRepository(
            JPAQueryFactory queryFactory, SellerBusinessInfoConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 셀러 사업자 정보 조회.
     *
     * @param id 사업자 정보 ID
     * @return 사업자 정보 Optional
     */
    public Optional<SellerBusinessInfoJpaEntity> findById(Long id) {
        SellerBusinessInfoJpaEntity entity =
                queryFactory
                        .selectFrom(sellerBusinessInfoJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 셀러 ID로 사업자 정보 조회.
     *
     * @param sellerId 셀러 ID
     * @return 사업자 정보 Optional
     */
    public Optional<SellerBusinessInfoJpaEntity> findBySellerId(Long sellerId) {
        SellerBusinessInfoJpaEntity entity =
                queryFactory
                        .selectFrom(sellerBusinessInfoJpaEntity)
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
                        .from(sellerBusinessInfoJpaEntity)
                        .where(conditionBuilder.sellerIdEq(sellerId), conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 사업자등록번호 존재 여부 확인.
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재 여부
     */
    public boolean existsByRegistrationNumber(String registrationNumber) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerBusinessInfoJpaEntity)
                        .where(
                                conditionBuilder.registrationNumberEq(registrationNumber),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 특정 셀러 ID를 제외한 사업자등록번호 존재 여부 확인.
     *
     * @param registrationNumber 사업자등록번호
     * @param excludeSellerId 제외할 셀러 ID
     * @return 존재 여부
     */
    public boolean existsByRegistrationNumberExcluding(
            String registrationNumber, Long excludeSellerId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(sellerBusinessInfoJpaEntity)
                        .where(
                                conditionBuilder.registrationNumberEq(registrationNumber),
                                sellerBusinessInfoJpaEntity.sellerId.ne(excludeSellerId),
                                conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }
}
