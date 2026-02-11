package com.ryuqq.setof.storage.legacy.composite.web.user.condition;

import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyRefundAccountEntity.legacyRefundAccountEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyRefundAccountEntity;
import org.springframework.stereotype.Component;

/**
 * 레거시 환불 계좌 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebRefundAccountConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyRefundAccountEntity.userId.eq(userId) : null;
    }

    public BooleanExpression notDeleted() {
        return legacyRefundAccountEntity.deleteYn.eq(LegacyRefundAccountEntity.Yn.N);
    }
}
