package com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition;

import static com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.QDiscountPolicyJpaEntity.discountPolicyJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyConditionBuilder - 할인 정책 QueryDSL 조건 빌더.
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
public class DiscountPolicyConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? discountPolicyJpaEntity.id.eq(id) : null;
    }

    /** 셀러 ID 일치 조건 */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? discountPolicyJpaEntity.sellerId.eq(sellerId) : null;
    }

    /** 적용 방식 일치 조건 */
    public BooleanExpression applicationTypeEq(
            DiscountPolicyJpaEntity.ApplicationType applicationType) {
        return applicationType != null
                ? discountPolicyJpaEntity.applicationType.eq(applicationType)
                : null;
    }

    /** 발행 주체 일치 조건 */
    public BooleanExpression publisherTypeEq(DiscountPolicyJpaEntity.PublisherType publisherType) {
        return publisherType != null
                ? discountPolicyJpaEntity.publisherType.eq(publisherType)
                : null;
    }

    /** 스태킹 그룹 일치 조건 */
    public BooleanExpression stackingGroupEq(DiscountPolicyJpaEntity.StackingGroup stackingGroup) {
        return stackingGroup != null
                ? discountPolicyJpaEntity.stackingGroup.eq(stackingGroup)
                : null;
    }

    /** 활성 상태 일치 조건 */
    public BooleanExpression activeEq(Boolean active) {
        return active != null ? discountPolicyJpaEntity.active.eq(active) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return discountPolicyJpaEntity.deletedAt.isNull();
    }
}
