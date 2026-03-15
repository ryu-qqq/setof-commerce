package com.ryuqq.setof.application.discount.dto.command;

import java.time.Instant;
import java.util.List;

/**
 * 할인 정책 생성 Command DTO.
 *
 * <p>할인 정책 신규 등록 요청에 필요한 모든 데이터를 담습니다.
 *
 * @param name 정책명 (필수)
 * @param description 정책 설명 (nullable)
 * @param discountMethod 할인 방식 (RATE, FIXED_AMOUNT)
 * @param discountRate 할인율 (RATE 방식 시 필수, nullable)
 * @param discountAmount 정액 할인금 (FIXED_AMOUNT 방식 시 필수, nullable)
 * @param maxDiscountAmount 최대 할인 한도액 (nullable)
 * @param discountCapped 할인 한도 적용 여부
 * @param minimumOrderAmount 최소 주문금액 조건 (nullable)
 * @param applicationType 적용 방식 (IMMEDIATE, COUPON)
 * @param publisherType 발행 주체 (ADMIN, SELLER)
 * @param sellerId 판매자 ID (셀러 할인 시, nullable)
 * @param stackingGroup 스태킹 그룹 (SELLER_INSTANT, PLATFORM_INSTANT, COUPON)
 * @param priority 적용 우선순위
 * @param startAt 할인 시작 시점
 * @param endAt 할인 종료 시점
 * @param totalBudget 총 예산
 * @param targets 할인 적용 대상 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CreateDiscountPolicyCommand(
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
        List<TargetItem> targets) {

    /**
     * 할인 적용 대상 항목.
     *
     * @param targetType 대상 유형 (예: PRODUCT_GROUP, CATEGORY)
     * @param targetId 대상 엔티티 ID
     */
    public record TargetItem(String targetType, long targetId) {}
}
