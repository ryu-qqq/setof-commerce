package com.ryuqq.setof.application.discount.dto.command;

import java.time.Instant;

/**
 * 할인 정책 수정 Command DTO.
 *
 * <p>기존 할인 정책의 수정 가능한 필드를 담습니다. applicationType, publisherType, sellerId, stackingGroup 은 변경 불가 필드로
 * 제외됩니다.
 *
 * @param discountPolicyId 수정할 할인 정책 ID
 * @param name 정책명
 * @param description 정책 설명 (nullable)
 * @param discountMethod 할인 방식 (RATE, FIXED_AMOUNT)
 * @param discountRate 할인율 (RATE 방식 시 필수, nullable)
 * @param discountAmount 정액 할인금 (FIXED_AMOUNT 방식 시 필수, nullable)
 * @param maxDiscountAmount 최대 할인 한도액 (nullable)
 * @param discountCapped 할인 한도 적용 여부
 * @param minimumOrderAmount 최소 주문금액 조건 (nullable)
 * @param priority 적용 우선순위
 * @param startAt 할인 시작 시점
 * @param endAt 할인 종료 시점
 * @param totalBudget 총 예산
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateDiscountPolicyCommand(
        long discountPolicyId,
        String name,
        String description,
        String discountMethod,
        Double discountRate,
        Integer discountAmount,
        Integer maxDiscountAmount,
        boolean discountCapped,
        Integer minimumOrderAmount,
        int priority,
        Instant startAt,
        Instant endAt,
        int totalBudget) {}
