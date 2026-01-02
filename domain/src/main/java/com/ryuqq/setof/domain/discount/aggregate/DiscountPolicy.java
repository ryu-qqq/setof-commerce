package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import com.ryuqq.setof.domain.discount.vo.ValidPeriod;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * DiscountPolicy Aggregate Root
 *
 * <p>할인 정책을 나타내는 도메인 엔티티입니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>같은 DiscountGroup 내에서는 하나의 정책만 적용 (중복 할인 방지)
 *   <li>서로 다른 DiscountGroup 간에는 할인 중첩 가능
 *   <li>Priority가 낮은 숫자가 높은 우선순위
 *   <li>유효 기간 내에만 적용 가능
 *   <li>사용 횟수 제한 초과 시 적용 불가
 *   <li>최소 주문 금액 미달 시 적용 불가
 *   <li>정률 할인 시 최대 할인 금액 상한 적용 가능
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class DiscountPolicy {

    private final DiscountPolicyId id;
    private final Long sellerId;
    private final PolicyName policyName;
    private final DiscountGroup discountGroup;
    private final DiscountType discountType;
    private final DiscountTargetType targetType;
    private final List<Long> targetIds;
    private final DiscountRate discountRate;
    private final DiscountAmount discountAmount;
    private final MaximumDiscountAmount maximumDiscountAmount;
    private final MinimumOrderAmount minimumOrderAmount;
    private final ValidPeriod validPeriod;
    private final UsageLimit usageLimit;
    private final CostShare costShare;
    private final Priority priority;
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private DiscountPolicy(
            DiscountPolicyId id,
            Long sellerId,
            PolicyName policyName,
            DiscountGroup discountGroup,
            DiscountType discountType,
            DiscountTargetType targetType,
            List<Long> targetIds,
            DiscountRate discountRate,
            DiscountAmount discountAmount,
            MaximumDiscountAmount maximumDiscountAmount,
            MinimumOrderAmount minimumOrderAmount,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            boolean isActive,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.discountGroup = discountGroup;
        this.discountType = discountType;
        this.targetType = targetType;
        this.targetIds = targetIds != null ? List.copyOf(targetIds) : List.of();
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
        this.validPeriod = validPeriod;
        this.usageLimit = usageLimit;
        this.costShare = costShare;
        this.priority = priority;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 정률 할인 정책 생성용 Static Factory Method
     *
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param discountGroup 할인 그룹
     * @param targetType 적용 대상 타입
     * @param targetIds 적용 대상 ID 목록
     * @param discountRate 할인율
     * @param maximumDiscountAmount 최대 할인 금액
     * @param minimumOrderAmount 최소 주문 금액
     * @param validPeriod 유효 기간
     * @param usageLimit 사용 제한
     * @param costShare 비용 분담
     * @param priority 우선순위
     * @param createdAt 생성일시
     * @return DiscountPolicy 인스턴스
     */
    public static DiscountPolicy forNewRateDiscount(
            Long sellerId,
            PolicyName policyName,
            DiscountGroup discountGroup,
            DiscountTargetType targetType,
            List<Long> targetIds,
            DiscountRate discountRate,
            MaximumDiscountAmount maximumDiscountAmount,
            MinimumOrderAmount minimumOrderAmount,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            Instant createdAt) {
        return new DiscountPolicy(
                null,
                sellerId,
                policyName,
                discountGroup,
                DiscountType.RATE,
                targetType,
                targetIds,
                discountRate,
                null,
                maximumDiscountAmount,
                minimumOrderAmount,
                validPeriod,
                usageLimit,
                costShare,
                priority,
                true,
                createdAt,
                createdAt,
                null);
    }

    /**
     * 신규 정액 할인 정책 생성용 Static Factory Method
     *
     * @param sellerId 셀러 ID
     * @param policyName 정책명
     * @param discountGroup 할인 그룹
     * @param targetType 적용 대상 타입
     * @param targetIds 적용 대상 ID 목록
     * @param discountAmount 할인 금액
     * @param minimumOrderAmount 최소 주문 금액
     * @param validPeriod 유효 기간
     * @param usageLimit 사용 제한
     * @param costShare 비용 분담
     * @param priority 우선순위
     * @param createdAt 생성일시
     * @return DiscountPolicy 인스턴스
     */
    public static DiscountPolicy forNewFixedDiscount(
            Long sellerId,
            PolicyName policyName,
            DiscountGroup discountGroup,
            DiscountTargetType targetType,
            List<Long> targetIds,
            DiscountAmount discountAmount,
            MinimumOrderAmount minimumOrderAmount,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            Instant createdAt) {
        return new DiscountPolicy(
                null,
                sellerId,
                policyName,
                discountGroup,
                DiscountType.FIXED_PRICE,
                targetType,
                targetIds,
                null,
                discountAmount,
                MaximumDiscountAmount.unlimited(),
                minimumOrderAmount,
                validPeriod,
                usageLimit,
                costShare,
                priority,
                true,
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     */
    public static DiscountPolicy reconstitute(
            DiscountPolicyId id,
            Long sellerId,
            PolicyName policyName,
            DiscountGroup discountGroup,
            DiscountType discountType,
            DiscountTargetType targetType,
            List<Long> targetIds,
            DiscountRate discountRate,
            DiscountAmount discountAmount,
            MaximumDiscountAmount maximumDiscountAmount,
            MinimumOrderAmount minimumOrderAmount,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            boolean isActive,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new DiscountPolicy(
                id,
                sellerId,
                policyName,
                discountGroup,
                discountType,
                targetType,
                targetIds,
                discountRate,
                discountAmount,
                maximumDiscountAmount,
                minimumOrderAmount,
                validPeriod,
                usageLimit,
                costShare,
                priority,
                isActive,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 할인 금액 계산
     *
     * @param originalAmount 원 금액
     * @return 계산된 할인 금액
     */
    public long calculateDiscountAmount(long originalAmount) {
        if (!canApply(originalAmount)) {
            return 0L;
        }

        long calculated;
        if (discountType.isRateType()) {
            calculated = discountRate.calculateDiscountAmount(originalAmount);
            calculated = maximumDiscountAmount.apply(calculated);
        } else {
            calculated = discountAmount.applyTo(originalAmount);
        }

        return calculated;
    }

    /**
     * 할인 적용 가능 여부 확인
     *
     * @param orderAmount 주문 금액
     * @return 적용 가능하면 true
     */
    public boolean canApply(long orderAmount) {
        if (!isActive) {
            return false;
        }
        if (isDeleted()) {
            return false;
        }
        if (!validPeriod.isCurrentlyValid()) {
            return false;
        }
        if (!minimumOrderAmount.isSatisfiedBy(orderAmount)) {
            return false;
        }
        return true;
    }

    /**
     * 고객의 사용 횟수 기준 적용 가능 여부 확인
     *
     * @param customerUsageCount 고객의 현재 사용 횟수
     * @param totalUsageCount 전체 사용 횟수
     * @return 사용 가능하면 true
     */
    public boolean canUse(int customerUsageCount, int totalUsageCount) {
        return usageLimit.canCustomerUse(customerUsageCount)
                && usageLimit.hasTotalCapacity(totalUsageCount);
    }

    /**
     * 대상에 적용 가능한지 확인
     *
     * @param targetId 대상 ID (상품, 카테고리, 셀러, 브랜드)
     * @return 적용 가능하면 true
     */
    public boolean isApplicableToTarget(Long targetId) {
        if (targetType == DiscountTargetType.ALL) {
            return true;
        }
        return targetIds.contains(targetId);
    }

    /**
     * 활성화
     *
     * @param updatedAt 수정일시
     * @return 활성화된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy activate(Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                true,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 비활성화
     *
     * @param updatedAt 수정일시
     * @return 비활성화된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy deactivate(Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                false,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 삭제 처리 (Soft Delete)
     *
     * @param deletedAt 삭제일시
     * @return 삭제된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy delete(Instant deletedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                false,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    /**
     * 우선순위 변경
     *
     * @param newPriority 새 우선순위
     * @param updatedAt 수정일시
     * @return 우선순위가 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changePriority(Priority newPriority, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                newPriority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 유효 기간 연장
     *
     * @param newValidPeriod 새 유효 기간
     * @param updatedAt 수정일시
     * @return 유효 기간이 연장된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy extendValidPeriod(ValidPeriod newValidPeriod, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                newValidPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 유효 기간 종료일만 연장 (편의 메서드)
     *
     * @param newEndAt 새 종료일시
     * @param updatedAt 수정일시
     * @return 유효 기간이 연장된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy extendValidPeriod(Instant newEndAt, Instant updatedAt) {
        ValidPeriod newValidPeriod = ValidPeriod.of(this.validPeriod.startAt(), newEndAt);
        return extendValidPeriod(newValidPeriod, updatedAt);
    }

    /**
     * 정책명 변경
     *
     * @param newPolicyName 새 정책명
     * @param updatedAt 수정일시
     * @return 정책명이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changePolicyName(PolicyName newPolicyName, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                newPolicyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 최대 할인 금액 변경
     *
     * @param newMaximumDiscountAmount 새 최대 할인 금액
     * @param updatedAt 수정일시
     * @return 최대 할인 금액이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changeMaximumDiscountAmount(
            MaximumDiscountAmount newMaximumDiscountAmount, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                newMaximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 최소 주문 금액 변경
     *
     * @param newMinimumOrderAmount 새 최소 주문 금액
     * @param updatedAt 수정일시
     * @return 최소 주문 금액이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changeMinimumOrderAmount(
            MinimumOrderAmount newMinimumOrderAmount, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                newMinimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 사용 제한 변경
     *
     * @param newUsageLimit 새 사용 제한
     * @param updatedAt 수정일시
     * @return 사용 제한이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changeUsageLimit(UsageLimit newUsageLimit, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                newUsageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 비용 분담 변경
     *
     * @param newCostShare 새 비용 분담
     * @param updatedAt 수정일시
     * @return 비용 분담이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy changeCostShare(CostShare newCostShare, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                this.targetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                newCostShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 적용 대상 ID 목록 변경
     *
     * @param newTargetIds 새 적용 대상 ID 목록
     * @param updatedAt 수정일시
     * @return 적용 대상이 변경된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy updateTargetIds(List<Long> newTargetIds, Instant updatedAt) {
        return new DiscountPolicy(
                this.id,
                this.sellerId,
                this.policyName,
                this.discountGroup,
                this.discountType,
                this.targetType,
                newTargetIds,
                this.discountRate,
                this.discountAmount,
                this.maximumDiscountAmount,
                this.minimumOrderAmount,
                this.validPeriod,
                this.usageLimit,
                this.costShare,
                this.priority,
                this.isActive,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 적용 대상 ID 추가
     *
     * @param targetIdToAdd 추가할 대상 ID
     * @param updatedAt 수정일시
     * @return 대상이 추가된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy addTargetId(Long targetIdToAdd, Instant updatedAt) {
        if (targetIds.contains(targetIdToAdd)) {
            return this;
        }
        List<Long> newTargetIds = new java.util.ArrayList<>(targetIds);
        newTargetIds.add(targetIdToAdd);
        return updateTargetIds(newTargetIds, updatedAt);
    }

    /**
     * 적용 대상 ID 제거
     *
     * @param targetIdToRemove 제거할 대상 ID
     * @param updatedAt 수정일시
     * @return 대상이 제거된 DiscountPolicy 인스턴스
     */
    public DiscountPolicy removeTargetId(Long targetIdToRemove, Instant updatedAt) {
        if (!targetIds.contains(targetIdToRemove)) {
            return this;
        }
        List<Long> newTargetIds = new java.util.ArrayList<>(targetIds);
        newTargetIds.remove(targetIdToRemove);
        return updateTargetIds(newTargetIds, updatedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null;
    }

    /**
     * 정률 할인인지 확인
     *
     * @return 정률 할인이면 true
     */
    public boolean isRateDiscount() {
        return discountType.isRateType();
    }

    /**
     * 정액 할인인지 확인
     *
     * @return 정액 할인이면 true
     */
    public boolean isFixedDiscount() {
        return discountType.isFixedType();
    }

    /**
     * 현재 유효한지 확인
     *
     * @return 유효 기간 내이면 true
     */
    public boolean isCurrentlyValid() {
        return validPeriod.isCurrentlyValid();
    }

    /**
     * 만료되었는지 확인
     *
     * @return 만료되었으면 true
     */
    public boolean isExpired() {
        return validPeriod.isExpired();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 할인 정책 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 할인 정책 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 정책명 값 반환 (Law of Demeter 준수)
     *
     * @return 정책명
     */
    public String getPolicyNameValue() {
        return policyName.value();
    }

    /**
     * 할인율 값 반환 (Law of Demeter 준수)
     *
     * @return 할인율 BigDecimal, 정률 할인이 아니면 null
     */
    public java.math.BigDecimal getDiscountRateValue() {
        return discountRate != null ? discountRate.value() : null;
    }

    /**
     * 할인 금액 값 반환 (Law of Demeter 준수)
     *
     * @return 할인 금액, 정액 할인이 아니면 null
     */
    public Long getDiscountAmountValue() {
        return discountAmount != null ? discountAmount.value() : null;
    }

    /**
     * 최대 할인 금액 값 반환 (Law of Demeter 준수)
     *
     * @return 최대 할인 금액, 상한 없으면 null
     */
    public Long getMaximumDiscountAmountValue() {
        return maximumDiscountAmount != null ? maximumDiscountAmount.value() : null;
    }

    /**
     * 최소 주문 금액 값 반환 (Law of Demeter 준수)
     *
     * @return 최소 주문 금액
     */
    public Long getMinimumOrderAmountValue() {
        return minimumOrderAmount.value();
    }

    /**
     * 우선순위 값 반환 (Law of Demeter 준수)
     *
     * @return 우선순위 int 값
     */
    public int getPriorityValue() {
        return priority.value();
    }

    /**
     * 유효 기간 시작일시 반환 (Law of Demeter 준수)
     *
     * @return 시작일시
     */
    public Instant getValidPeriodStartAt() {
        return validPeriod.startAt();
    }

    /**
     * 유효 기간 종료일시 반환 (Law of Demeter 준수)
     *
     * @return 종료일시
     */
    public Instant getValidPeriodEndAt() {
        return validPeriod.endAt();
    }

    /**
     * 플랫폼 비용 분담 비율 반환 (Law of Demeter 준수)
     *
     * @return 플랫폼 분담 비율
     */
    public java.math.BigDecimal getPlatformCostShareRatio() {
        return costShare.platformRatio();
    }

    /**
     * 셀러 비용 분담 비율 반환 (Law of Demeter 준수)
     *
     * @return 셀러 분담 비율
     */
    public java.math.BigDecimal getSellerCostShareRatio() {
        return costShare.sellerRatio();
    }

    /**
     * 플랫폼 부담 비용 계산 (Law of Demeter 준수)
     *
     * @param discountedAmount 할인된 금액
     * @return 플랫폼 부담 금액
     */
    public long calculatePlatformCost(long discountedAmount) {
        return costShare.calculatePlatformCost(discountedAmount);
    }

    /**
     * 셀러 부담 비용 계산 (Law of Demeter 준수)
     *
     * @param discountedAmount 할인된 금액
     * @return 셀러 부담 금액
     */
    public long calculateSellerCost(long discountedAmount) {
        return costShare.calculateSellerCost(discountedAmount);
    }

    /**
     * 유효 시작일시 반환 (편의 메서드)
     *
     * @return 시작일시
     */
    public Instant getValidStartAt() {
        return validPeriod.startAt();
    }

    /**
     * 유효 종료일시 반환 (편의 메서드)
     *
     * @return 종료일시
     */
    public Instant getValidEndAt() {
        return validPeriod.endAt();
    }

    /**
     * 고객당 최대 사용 횟수 반환 (Law of Demeter 준수)
     *
     * @return 고객당 최대 사용 횟수, 제한 없으면 null
     */
    public Integer getMaxUsagePerCustomer() {
        return usageLimit.perCustomerLimit();
    }

    /**
     * 전체 최대 사용 횟수 반환 (Law of Demeter 준수)
     *
     * @return 전체 최대 사용 횟수, 제한 없으면 null
     */
    public Integer getMaxTotalUsage() {
        return usageLimit.totalLimit();
    }

    /**
     * 첫 번째 타겟 ID 반환 (단일 타겟용 편의 메서드)
     *
     * @return 첫 번째 타겟 ID, 없으면 null
     */
    public Long getTargetId() {
        return targetIds.isEmpty() ? null : targetIds.get(0);
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public DiscountPolicyId getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public PolicyName getPolicyName() {
        return policyName;
    }

    public DiscountGroup getDiscountGroup() {
        return discountGroup;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public DiscountTargetType getTargetType() {
        return targetType;
    }

    public List<Long> getTargetIds() {
        return Collections.unmodifiableList(targetIds);
    }

    public DiscountRate getDiscountRate() {
        return discountRate;
    }

    public DiscountAmount getDiscountAmount() {
        return discountAmount;
    }

    public MaximumDiscountAmount getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public MinimumOrderAmount getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public ValidPeriod getValidPeriod() {
        return validPeriod;
    }

    public UsageLimit getUsageLimit() {
        return usageLimit;
    }

    public CostShare getCostShare() {
        return costShare;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
