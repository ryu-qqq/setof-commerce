package com.ryuqq.setof.domain.discount.service;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 할인 계산 Domain Service
 *
 * <p>복수의 할인 정책을 적용하여 최종 할인 금액을 계산합니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>같은 DiscountGroup 내에서는 가장 우선순위가 높은 정책 하나만 적용
 *   <li>서로 다른 DiscountGroup 간에는 할인 중첩 가능
 *   <li>Priority 값이 낮을수록 높은 우선순위
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>외부 의존성 없는 순수 도메인 로직
 * </ul>
 */
public class DiscountCalculator {

    /**
     * 복수의 할인 정책을 적용하여 총 할인 금액 계산
     *
     * <p>각 DiscountGroup에서 가장 우선순위가 높은 정책만 적용됩니다.
     *
     * @param policies 적용 가능한 할인 정책 목록
     * @param originalAmount 원 금액
     * @return 총 할인 금액
     */
    public long calculateTotalDiscount(List<DiscountPolicy> policies, long originalAmount) {
        Map<DiscountGroup, DiscountPolicy> bestPolicies = selectBestPoliciesPerGroup(policies);

        long totalDiscount = 0L;
        long remainingAmount = originalAmount;

        for (DiscountPolicy policy : bestPolicies.values()) {
            long discountAmount = policy.calculateDiscountAmount(remainingAmount);
            totalDiscount += discountAmount;
            remainingAmount -= discountAmount;

            if (remainingAmount <= 0) {
                break;
            }
        }

        return Math.min(totalDiscount, originalAmount);
    }

    /**
     * 할인 적용 결과 상세 정보 계산
     *
     * @param policies 적용 가능한 할인 정책 목록
     * @param originalAmount 원 금액
     * @return 그룹별 할인 금액 맵
     */
    public Map<DiscountGroup, Long> calculateDiscountsByGroup(
            List<DiscountPolicy> policies, long originalAmount) {
        Map<DiscountGroup, DiscountPolicy> bestPolicies = selectBestPoliciesPerGroup(policies);
        Map<DiscountGroup, Long> discountsByGroup = new EnumMap<>(DiscountGroup.class);

        long remainingAmount = originalAmount;

        for (Map.Entry<DiscountGroup, DiscountPolicy> entry : bestPolicies.entrySet()) {
            DiscountGroup group = entry.getKey();
            DiscountPolicy policy = entry.getValue();

            long discountAmount = policy.calculateDiscountAmount(remainingAmount);
            discountsByGroup.put(group, discountAmount);
            remainingAmount -= discountAmount;

            if (remainingAmount <= 0) {
                break;
            }
        }

        return discountsByGroup;
    }

    /**
     * 각 DiscountGroup에서 가장 우선순위가 높은 정책 선택
     *
     * @param policies 할인 정책 목록
     * @return 그룹별 최우선 정책 맵
     */
    private Map<DiscountGroup, DiscountPolicy> selectBestPoliciesPerGroup(
            List<DiscountPolicy> policies) {
        Map<DiscountGroup, DiscountPolicy> bestPolicies = new EnumMap<>(DiscountGroup.class);

        for (DiscountPolicy policy : policies) {
            DiscountGroup group = policy.getDiscountGroup();
            DiscountPolicy existing = bestPolicies.get(group);

            if (existing == null || hasHigherPriority(policy, existing)) {
                bestPolicies.put(group, policy);
            }
        }

        return bestPolicies;
    }

    /**
     * 우선순위 비교 (낮은 숫자가 높은 우선순위)
     *
     * @param candidate 비교 대상
     * @param existing 기존 정책
     * @return candidate가 더 높은 우선순위면 true
     */
    private boolean hasHigherPriority(DiscountPolicy candidate, DiscountPolicy existing) {
        return candidate.getPriorityValue() < existing.getPriorityValue();
    }

    /**
     * 정책을 우선순위순으로 정렬
     *
     * @param policies 정렬할 정책 목록
     * @return 우선순위순 정렬된 정책 목록
     */
    public List<DiscountPolicy> sortByPriority(List<DiscountPolicy> policies) {
        return policies.stream()
                .sorted(Comparator.comparingInt(DiscountPolicy::getPriorityValue))
                .toList();
    }
}
