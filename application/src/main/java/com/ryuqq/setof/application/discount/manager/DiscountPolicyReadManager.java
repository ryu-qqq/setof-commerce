package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.exception.DiscountPolicyNotFoundException;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 할인 정책 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyReadManager {

    private final DiscountPolicyQueryPort policyQueryPort;

    public DiscountPolicyReadManager(DiscountPolicyQueryPort policyQueryPort) {
        this.policyQueryPort = policyQueryPort;
    }

    /**
     * 특정 타겟에 적용 가능한 활성 할인 정책 중, 현재 시점에서 유효한 목록만 반환.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @param now 현재 시각
     * @return 적용 가능한 할인 정책 목록
     */
    public List<DiscountPolicy> findApplicablePolicies(
            DiscountTargetType targetType, long targetId, Instant now) {
        List<DiscountPolicy> policies = policyQueryPort.findActiveByTarget(targetType, targetId);
        return policies.stream().filter(p -> p.isApplicableAt(now)).toList();
    }

    /**
     * ID로 할인 정책 단건 조회. 존재하지 않으면 예외 발생.
     *
     * @param id 할인 정책 ID
     * @return 할인 정책
     * @throws DiscountPolicyNotFoundException 해당 ID의 정책이 없을 때
     */
    public DiscountPolicy getById(long id) {
        return policyQueryPort
                .findById(id)
                .orElseThrow(() -> new DiscountPolicyNotFoundException(id));
    }

    /**
     * ID로 할인 정책 단건 조회 (Optional).
     *
     * @param id 할인 정책 ID
     * @return 할인 정책 Optional
     */
    public Optional<DiscountPolicy> findById(long id) {
        return policyQueryPort.findById(id);
    }

    /**
     * 검색 조건으로 할인 정책 목록 조회 (페이징).
     *
     * @param criteria 검색 조건
     * @return 할인 정책 목록
     */
    public List<DiscountPolicy> findByCriteria(DiscountPolicySearchCriteria criteria) {
        return policyQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건으로 할인 정책 총 건수 조회.
     *
     * @param criteria 검색 조건
     * @return 총 건수
     */
    public long countByCriteria(DiscountPolicySearchCriteria criteria) {
        return policyQueryPort.countByCriteria(criteria);
    }
}
