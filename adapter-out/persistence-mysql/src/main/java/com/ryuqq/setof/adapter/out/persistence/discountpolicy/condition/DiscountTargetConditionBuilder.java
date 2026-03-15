package com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition;

import static com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.QDiscountTargetJpaEntity.discountTargetJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountTargetConditionBuilder - 할인 적용 대상 QueryDSL 조건 빌더.
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
public class DiscountTargetConditionBuilder {

    /** 정책 ID 일치 조건 */
    public BooleanExpression policyIdEq(Long policyId) {
        return policyId != null ? discountTargetJpaEntity.discountPolicyId.eq(policyId) : null;
    }

    /** 정책 ID 목록 포함 조건 */
    public BooleanExpression policyIdIn(List<Long> policyIds) {
        return policyIds != null && !policyIds.isEmpty()
                ? discountTargetJpaEntity.discountPolicyId.in(policyIds)
                : null;
    }

    /** 대상 유형 일치 조건 */
    public BooleanExpression targetTypeEq(DiscountTargetJpaEntity.TargetType targetType) {
        return targetType != null ? discountTargetJpaEntity.targetType.eq(targetType) : null;
    }

    /** 대상 ID 일치 조건 */
    public BooleanExpression targetIdEq(Long targetId) {
        return targetId != null ? discountTargetJpaEntity.targetId.eq(targetId) : null;
    }

    /** 활성 상태 일치 조건 */
    public BooleanExpression activeEq(Boolean active) {
        return active != null ? discountTargetJpaEntity.active.eq(active) : null;
    }
}
