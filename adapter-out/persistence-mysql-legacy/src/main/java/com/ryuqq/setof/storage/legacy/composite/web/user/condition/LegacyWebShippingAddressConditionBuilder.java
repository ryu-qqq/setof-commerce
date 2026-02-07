package com.ryuqq.setof.storage.legacy.composite.web.user.condition;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyShippingAddressEntity.legacyShippingAddressEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyShippingAddressEntity;
import org.springframework.stereotype.Component;

/**
 * 레거시 배송지 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebShippingAddressConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyShippingAddressEntity.userId.eq(userId) : null;
    }

    public BooleanExpression shippingAddressIdEq(Long shippingAddressId) {
        return shippingAddressId != null
                ? legacyShippingAddressEntity.id.eq(shippingAddressId)
                : null;
    }

    public BooleanExpression notDeleted() {
        return legacyShippingAddressEntity.deleteYn.eq(LegacyShippingAddressEntity.Yn.N);
    }
}
