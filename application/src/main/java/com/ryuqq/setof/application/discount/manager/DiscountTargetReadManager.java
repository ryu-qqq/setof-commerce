package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.query.DiscountTargetQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 할인 적용 대상 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountTargetReadManager {

    private final DiscountTargetQueryPort targetQueryPort;

    public DiscountTargetReadManager(DiscountTargetQueryPort targetQueryPort) {
        this.targetQueryPort = targetQueryPort;
    }

    /**
     * 정책 ID로 타겟 목록 조회.
     *
     * @param policyId 정책 ID
     * @return 타겟 목록
     */
    public List<DiscountTarget> findByPolicyId(long policyId) {
        return targetQueryPort.findByPolicyId(policyId);
    }

    /**
     * 여러 정책의 타겟 목록 일괄 조회.
     *
     * @param policyIds 정책 ID 목록
     * @return 타겟 목록
     */
    public List<DiscountTarget> findByPolicyIds(List<Long> policyIds) {
        return targetQueryPort.findByPolicyIds(policyIds);
    }

    /**
     * 정책별 활성 타겟 수 조회.
     *
     * @param policyId 정책 ID
     * @return 활성 타겟 수
     */
    public long countByPolicyId(long policyId) {
        return targetQueryPort.countByPolicyId(policyId);
    }
}
