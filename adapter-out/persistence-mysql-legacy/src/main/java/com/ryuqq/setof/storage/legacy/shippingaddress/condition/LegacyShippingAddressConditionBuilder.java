package com.ryuqq.setof.storage.legacy.shippingaddress.condition;

import static com.ryuqq.setof.storage.legacy.shippingaddress.entity.QLegacyShippingAddressEntity.legacyShippingAddressEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * LegacyShippingAddressConditionBuilder - 레거시 배송지 QueryDSL 조건 빌더.
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
public class LegacyShippingAddressConditionBuilder {

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyShippingAddressEntity.userId.eq(userId) : null;
    }

    /**
     * 배송지 ID 일치 조건.
     *
     * @param shippingAddressId 배송지 ID
     * @return BooleanExpression
     */
    public BooleanExpression idEq(Long shippingAddressId) {
        return shippingAddressId != null
                ? legacyShippingAddressEntity.id.eq(shippingAddressId)
                : null;
    }
}
