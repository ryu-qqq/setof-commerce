package com.ryuqq.setof.storage.legacy.shippingaddress.repository;

import static com.ryuqq.setof.storage.legacy.shippingaddress.entity.QLegacyShippingAddressEntity.legacyShippingAddressEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.shippingaddress.query.ShippingAddressSearchCondition;
import com.ryuqq.setof.storage.legacy.shippingaddress.condition.LegacyShippingAddressConditionBuilder;
import com.ryuqq.setof.storage.legacy.shippingaddress.entity.LegacyShippingAddressEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyShippingAddressQueryDslRepository - 레거시 배송지 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>단일 테이블 조회이므로 Projections.constructor 대신 엔티티 직접 조회를 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyShippingAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyShippingAddressConditionBuilder conditionBuilder;

    public LegacyShippingAddressQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyShippingAddressConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 검색 조건으로 배송지 목록 조회.
     *
     * <p>userId가 주어지면 해당 사용자의 전체 배송지 목록을 반환합니다.
     *
     * <p>shippingAddressId도 함께 주어지면 특정 배송지 단건을 반환합니다.
     *
     * @param condition 배송지 검색 조건
     * @return 배송지 엔티티 목록
     */
    public List<LegacyShippingAddressEntity> findByCondition(
            ShippingAddressSearchCondition condition) {
        return queryFactory
                .selectFrom(legacyShippingAddressEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.idEq(condition.shippingAddressId()))
                .fetch();
    }

    /**
     * 사용자별 배송지 개수 조회.
     *
     * @param userId 사용자 ID
     * @return 배송지 개수
     */
    public long countByUserId(Long userId) {
        Long count =
                queryFactory
                        .select(legacyShippingAddressEntity.count())
                        .from(legacyShippingAddressEntity)
                        .where(conditionBuilder.userIdEq(userId))
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
