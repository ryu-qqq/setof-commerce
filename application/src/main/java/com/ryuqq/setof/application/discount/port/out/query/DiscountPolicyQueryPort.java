package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;

/**
 * 할인 정책 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountPolicyQueryPort {

    /**
     * 특정 타겟에 적용 가능한 활성 할인 정책 목록 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 적용 가능한 할인 정책 목록
     */
    List<DiscountPolicy> findActiveByTarget(DiscountTargetType targetType, long targetId);
}
