package com.ryuqq.setof.storage.legacy.composite.web.user.repository;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyShippingAddressEntity.legacyShippingAddressEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyShippingAddressSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.condition.LegacyWebShippingAddressConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebShippingAddressQueryDto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * 레거시 배송지 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebShippingAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebShippingAddressConditionBuilder conditionBuilder;

    public LegacyWebShippingAddressQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebShippingAddressConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 사용자의 배송지 목록 조회.
     *
     * @param condition 검색 조건
     * @return 배송지 목록
     */
    public List<LegacyWebShippingAddressQueryDto> fetchShippingAddresses(
            LegacyShippingAddressSearchCondition condition) {
        return queryFactory
                .select(createProjection())
                .from(legacyShippingAddressEntity)
                .where(conditionBuilder.userIdEq(condition.userId()), conditionBuilder.notDeleted())
                .orderBy(legacyShippingAddressEntity.id.desc())
                .fetch();
    }

    /**
     * 특정 배송지 단건 조회.
     *
     * @param condition 검색 조건
     * @return 배송지 Optional
     */
    public Optional<LegacyWebShippingAddressQueryDto> fetchShippingAddress(
            LegacyShippingAddressSearchCondition condition) {
        LegacyWebShippingAddressQueryDto dto =
                queryFactory
                        .select(createProjection())
                        .from(legacyShippingAddressEntity)
                        .where(
                                conditionBuilder.userIdEq(condition.userId()),
                                conditionBuilder.shippingAddressIdEq(condition.shippingAddressId()),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    private com.querydsl.core.types.ConstructorExpression<LegacyWebShippingAddressQueryDto>
            createProjection() {
        return Projections.constructor(
                LegacyWebShippingAddressQueryDto.class,
                legacyShippingAddressEntity.id,
                legacyShippingAddressEntity.receiverName,
                legacyShippingAddressEntity.shippingAddressName,
                legacyShippingAddressEntity.addressLine1,
                legacyShippingAddressEntity.addressLine2,
                legacyShippingAddressEntity.zipCode,
                legacyShippingAddressEntity.country.stringValue(),
                legacyShippingAddressEntity.deliveryRequest,
                legacyShippingAddressEntity.phoneNumber,
                legacyShippingAddressEntity.defaultYn.stringValue());
    }
}
