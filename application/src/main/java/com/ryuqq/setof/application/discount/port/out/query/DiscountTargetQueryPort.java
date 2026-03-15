package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.util.List;

/**
 * 할인 적용 대상 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountTargetQueryPort {

    /**
     * 정책 ID로 타겟 목록 조회.
     *
     * @param discountPolicyId 정책 ID
     * @return 타겟 목록
     */
    List<DiscountTarget> findByPolicyId(long discountPolicyId);

    /**
     * 여러 정책의 타겟 목록 일괄 조회.
     *
     * @param policyIds 정책 ID 목록
     * @return 타겟 목록
     */
    List<DiscountTarget> findByPolicyIds(List<Long> policyIds);

    /**
     * 정책별 활성 타겟 수 조회.
     *
     * @param discountPolicyId 정책 ID
     * @return 활성 타겟 수
     */
    long countByPolicyId(long discountPolicyId);
}
