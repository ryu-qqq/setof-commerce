package com.ryuqq.setof.domain.discount.service;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 할인 적용 가능성 검증 Domain Service
 *
 * <p>할인 정책의 적용 가능 여부를 다양한 조건으로 검증합니다.
 *
 * <p>검증 조건:
 *
 * <ul>
 *   <li>정책 활성화 상태
 *   <li>유효 기간
 *   <li>최소 주문 금액
 *   <li>사용 횟수 제한
 *   <li>적용 대상 (상품, 카테고리, 셀러, 브랜드)
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>외부 의존성 없는 순수 도메인 로직
 * </ul>
 */
public class DiscountEligibilityChecker {

    /**
     * 할인 적용 가능한 정책만 필터링
     *
     * @param policies 검증할 정책 목록
     * @param orderAmount 주문 금액
     * @param targetId 적용 대상 ID
     * @param customerUsageCount 고객별 사용 횟수
     * @param totalUsageCount 전체 사용 횟수
     * @return 적용 가능한 정책 목록
     */
    public List<DiscountPolicy> filterEligiblePolicies(
            List<DiscountPolicy> policies,
            long orderAmount,
            Long targetId,
            int customerUsageCount,
            int totalUsageCount) {

        List<DiscountPolicy> eligiblePolicies = new ArrayList<>();

        for (DiscountPolicy policy : policies) {
            if (isEligible(policy, orderAmount, targetId, customerUsageCount, totalUsageCount)) {
                eligiblePolicies.add(policy);
            }
        }

        return eligiblePolicies;
    }

    /**
     * 단일 정책의 적용 가능 여부 확인
     *
     * @param policy 검증할 정책
     * @param orderAmount 주문 금액
     * @param targetId 적용 대상 ID
     * @param customerUsageCount 고객별 사용 횟수
     * @param totalUsageCount 전체 사용 횟수
     * @return 적용 가능하면 true
     */
    public boolean isEligible(
            DiscountPolicy policy,
            long orderAmount,
            Long targetId,
            int customerUsageCount,
            int totalUsageCount) {

        if (!policy.canApply(orderAmount)) {
            return false;
        }
        if (!policy.canUse(customerUsageCount, totalUsageCount)) {
            return false;
        }
        if (targetId != null && !policy.isApplicableToTarget(targetId)) {
            return false;
        }
        return true;
    }

    /**
     * 적용 가능 여부와 상세 이유 반환
     *
     * @param policy 검증할 정책
     * @param orderAmount 주문 금액
     * @param targetId 적용 대상 ID
     * @param customerUsageCount 고객별 사용 횟수
     * @param totalUsageCount 전체 사용 횟수
     * @return 검증 결과
     */
    public EligibilityResult checkEligibility(
            DiscountPolicy policy,
            long orderAmount,
            Long targetId,
            int customerUsageCount,
            int totalUsageCount) {

        if (!policy.isActive()) {
            return EligibilityResult.failure(IneligibleReason.INACTIVE);
        }
        if (policy.isDeleted()) {
            return EligibilityResult.failure(IneligibleReason.DELETED);
        }
        if (!policy.isCurrentlyValid()) {
            if (policy.isExpired()) {
                return EligibilityResult.failure(IneligibleReason.EXPIRED);
            }
            return EligibilityResult.failure(IneligibleReason.NOT_STARTED);
        }
        if (!policy.getMinimumOrderAmount().isSatisfiedBy(orderAmount)) {
            return EligibilityResult.failure(IneligibleReason.MINIMUM_ORDER_NOT_MET);
        }
        if (!policy.getUsageLimit().canCustomerUse(customerUsageCount)) {
            return EligibilityResult.failure(IneligibleReason.CUSTOMER_USAGE_EXCEEDED);
        }
        if (!policy.getUsageLimit().hasTotalCapacity(totalUsageCount)) {
            return EligibilityResult.failure(IneligibleReason.TOTAL_USAGE_EXCEEDED);
        }
        if (targetId != null && !policy.isApplicableToTarget(targetId)) {
            return EligibilityResult.failure(IneligibleReason.TARGET_NOT_APPLICABLE);
        }

        return EligibilityResult.success();
    }

    /** 적용 불가 사유 Enum */
    public enum IneligibleReason {
        INACTIVE("비활성화된 정책입니다."),
        DELETED("삭제된 정책입니다."),
        EXPIRED("유효 기간이 만료되었습니다."),
        NOT_STARTED("유효 기간이 시작되지 않았습니다."),
        MINIMUM_ORDER_NOT_MET("최소 주문 금액을 충족하지 않습니다."),
        CUSTOMER_USAGE_EXCEEDED("고객별 사용 횟수를 초과했습니다."),
        TOTAL_USAGE_EXCEEDED("전체 사용 횟수를 초과했습니다."),
        TARGET_NOT_APPLICABLE("적용 대상이 아닙니다.");

        private final String message;

        IneligibleReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /** 적용 가능성 검증 결과 Record */
    public record EligibilityResult(boolean isEligible, IneligibleReason reason) {

        /**
         * 적용 가능 결과 생성
         *
         * @return 적용 가능 결과
         */
        public static EligibilityResult success() {
            return new EligibilityResult(true, null);
        }

        /**
         * 적용 불가 결과 생성
         *
         * @param reason 적용 불가 사유
         * @return 적용 불가 결과
         */
        public static EligibilityResult failure(IneligibleReason reason) {
            return new EligibilityResult(false, reason);
        }

        /**
         * 적용 불가 사유 메시지 반환
         *
         * @return 사유 메시지 (적용 가능 시 null)
         */
        public String getReasonMessage() {
            return reason != null ? reason.getMessage() : null;
        }
    }
}
