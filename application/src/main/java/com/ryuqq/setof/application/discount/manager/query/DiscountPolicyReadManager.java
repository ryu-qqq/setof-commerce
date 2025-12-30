package com.ryuqq.setof.application.discount.manager.query;

import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.exception.DiscountPolicyNotFoundException;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 할인 정책 Read Manager
 *
 * <p>Port-Out과 Domain 사이의 읽기 관련 로직을 조율
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyReadManager {

    private final DiscountPolicyQueryPort discountPolicyQueryPort;

    public DiscountPolicyReadManager(DiscountPolicyQueryPort discountPolicyQueryPort) {
        this.discountPolicyQueryPort = discountPolicyQueryPort;
    }

    /**
     * ID로 할인 정책 조회 (존재하지 않으면 예외)
     *
     * @param discountPolicyId 할인 정책 ID
     * @return 할인 정책 도메인
     * @throws DiscountPolicyNotFoundException 정책이 존재하지 않는 경우
     */
    public DiscountPolicy findById(Long discountPolicyId) {
        return discountPolicyQueryPort
                .findById(DiscountPolicyId.of(discountPolicyId))
                .orElseThrow(() -> new DiscountPolicyNotFoundException(discountPolicyId));
    }

    /**
     * 셀러 ID로 할인 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 할인 정책 목록
     */
    public List<DiscountPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return discountPolicyQueryPort.findBySellerId(sellerId, includeDeleted);
    }

    /**
     * 셀러 ID와 그룹으로 할인 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param discountGroup 할인 그룹
     * @param activeOnly 활성 정책만 조회 여부
     * @return 할인 정책 목록
     */
    public List<DiscountPolicy> findBySellerIdAndGroup(
            Long sellerId, DiscountGroup discountGroup, boolean activeOnly) {
        return discountPolicyQueryPort.findBySellerIdAndGroup(sellerId, discountGroup, activeOnly);
    }

    /**
     * 적용 대상 ID로 적용 가능한 할인 정책 조회
     *
     * @param targetType 적용 대상 타입
     * @param targetId 적용 대상 ID
     * @param activeOnly 활성 정책만 조회 여부
     * @return 할인 정책 목록
     */
    public List<DiscountPolicy> findByTargetTypeAndTargetId(
            DiscountTargetType targetType, Long targetId, boolean activeOnly) {
        return discountPolicyQueryPort.findByTargetTypeAndTargetId(
                targetType, targetId, activeOnly);
    }

    /**
     * 현재 유효한 할인 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @return 현재 유효한 할인 정책 목록
     */
    public List<DiscountPolicy> findValidPolicies(Long sellerId) {
        return discountPolicyQueryPort.findValidPolicies(sellerId);
    }
}
