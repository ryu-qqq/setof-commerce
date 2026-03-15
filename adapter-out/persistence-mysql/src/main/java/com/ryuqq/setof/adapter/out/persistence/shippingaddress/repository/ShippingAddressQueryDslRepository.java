package com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository;

import static com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.QShippingAddressJpaEntity.shippingAddressJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.condition.ShippingAddressConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ShippingAddressQueryDslRepository - 배송지 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ShippingAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ShippingAddressConditionBuilder conditionBuilder;

    public ShippingAddressQueryDslRepository(
            JPAQueryFactory queryFactory, ShippingAddressConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 레거시 회원 ID + 배송지 ID로 배송지 조회.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @param shippingAddressId 배송지 ID
     * @return 배송지 Optional
     */
    public Optional<ShippingAddressJpaEntity> findById(
            Long legacyMemberId, Long shippingAddressId) {
        ShippingAddressJpaEntity entity =
                queryFactory
                        .selectFrom(shippingAddressJpaEntity)
                        .where(
                                conditionBuilder.legacyMemberIdEq(legacyMemberId),
                                conditionBuilder.idEq(shippingAddressId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * 레거시 회원 ID로 배송지 목록 조회.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @return 배송지 목록
     */
    public List<ShippingAddressJpaEntity> findAllByLegacyMemberId(Long legacyMemberId) {
        return queryFactory
                .selectFrom(shippingAddressJpaEntity)
                .where(
                        conditionBuilder.legacyMemberIdEq(legacyMemberId),
                        conditionBuilder.notDeleted())
                .fetch();
    }

    /**
     * 레거시 회원 ID로 배송지 개수 조회.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @return 배송지 개수
     */
    public int countByLegacyMemberId(Long legacyMemberId) {
        Long count =
                queryFactory
                        .select(shippingAddressJpaEntity.count())
                        .from(shippingAddressJpaEntity)
                        .where(
                                conditionBuilder.legacyMemberIdEq(legacyMemberId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count.intValue() : 0;
    }
}
