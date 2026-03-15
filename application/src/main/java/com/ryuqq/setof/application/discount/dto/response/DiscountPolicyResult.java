package com.ryuqq.setof.application.discount.dto.response;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.time.Instant;
import java.util.List;

/**
 * 할인 정책 상세 결과 DTO.
 *
 * <p>도메인 {@link DiscountPolicy}를 Application 레이어에서 외부로 노출하기 위한 읽기 전용 표현입니다.
 *
 * @param id 할인 정책 ID
 * @param name 정책명
 * @param description 정책 설명
 * @param discountMethod 할인 방식
 * @param discountRate 할인율 (RATE 방식 시 non-null)
 * @param discountAmount 정액 할인금 (FIXED_AMOUNT 방식 시 non-null)
 * @param maxDiscountAmount 최대 할인 한도액
 * @param discountCapped 할인 한도 적용 여부
 * @param minimumOrderAmount 최소 주문금액 조건
 * @param applicationType 적용 방식
 * @param publisherType 발행 주체
 * @param sellerId 판매자 ID (nullable)
 * @param stackingGroup 스태킹 그룹
 * @param priority 적용 우선순위
 * @param startAt 할인 시작 시점
 * @param endAt 할인 종료 시점
 * @param totalBudget 총 예산
 * @param usedBudget 사용된 예산
 * @param active 활성 여부
 * @param targets 활성 적용 대상 목록
 * @param createdAt 생성 시점
 * @param updatedAt 최종 수정 시점
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountPolicyResult(
        long id,
        String name,
        String description,
        String discountMethod,
        Double discountRate,
        Integer discountAmount,
        Integer maxDiscountAmount,
        boolean discountCapped,
        Integer minimumOrderAmount,
        String applicationType,
        String publisherType,
        Long sellerId,
        String stackingGroup,
        int priority,
        Instant startAt,
        Instant endAt,
        int totalBudget,
        int usedBudget,
        boolean active,
        List<DiscountTargetResult> targets,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * 도메인 {@link DiscountPolicy}로부터 결과 DTO를 생성합니다.
     *
     * @param policy 할인 정책 Aggregate
     * @return 변환된 결과 DTO
     */
    public static DiscountPolicyResult from(DiscountPolicy policy) {
        List<DiscountTargetResult> targetResults =
                policy.activeTargets().stream()
                        .map(
                                t ->
                                        new DiscountTargetResult(
                                                t.idValue(), t.targetType().name(), t.targetId()))
                        .toList();

        return new DiscountPolicyResult(
                policy.idValue(),
                policy.nameValue(),
                policy.description(),
                policy.discountMethod().name(),
                policy.discountRateValue(),
                policy.discountAmountValue(),
                policy.maxDiscountAmountValue(),
                policy.isDiscountCapped(),
                policy.minimumOrderAmountValue(),
                policy.applicationType().name(),
                policy.publisherType().name(),
                policy.sellerIdValue(),
                policy.stackingGroup().name(),
                policy.priorityValue(),
                policy.period().startAt(),
                policy.period().endAt(),
                policy.budget().totalBudget().value(),
                policy.budget().usedBudget().value(),
                policy.isActive(),
                targetResults,
                policy.createdAt(),
                policy.updatedAt());
    }
}
