package com.ryuqq.setof.adapter.out.persistence.shippingaddress.condition;

import static com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.QShippingAddressJpaEntity.shippingAddressJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressConditionBuilder - 배송지 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressConditionBuilder {

    /** 레거시 회원 ID 일치 조건 */
    public BooleanExpression legacyMemberIdEq(Long legacyMemberId) {
        return legacyMemberId != null
                ? shippingAddressJpaEntity.legacyMemberId.eq(legacyMemberId)
                : null;
    }

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? shippingAddressJpaEntity.id.eq(id) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return shippingAddressJpaEntity.deletedAt.isNull();
    }
}
