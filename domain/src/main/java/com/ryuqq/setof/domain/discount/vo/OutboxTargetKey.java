package com.ryuqq.setof.domain.discount.vo;

/**
 * 아웃박스 타겟 키 Value Object.
 *
 * <p>할인 재계산이 필요한 대상을 식별합니다.
 *
 * <p>형식: "{DiscountTargetType}:{targetId}" (예: "BRAND:123", "SELLER:45")
 *
 * <p>동일 타겟에 대해 PENDING/PUBLISHED 각 최대 1건만 허용됩니다.
 *
 * @param targetType 할인 대상 유형
 * @param targetId 대상 ID
 */
public record OutboxTargetKey(DiscountTargetType targetType, long targetId) {

    public OutboxTargetKey {
        if (targetType == null) {
            throw new IllegalArgumentException("타겟 유형은 필수입니다");
        }
        if (targetId <= 0) {
            throw new IllegalArgumentException("타겟 ID는 0보다 커야 합니다: " + targetId);
        }
    }

    public static OutboxTargetKey of(DiscountTargetType targetType, long targetId) {
        return new OutboxTargetKey(targetType, targetId);
    }

    /** "BRAND:123" 형식의 문자열 표현 */
    public String toKey() {
        return targetType.name() + ":" + targetId;
    }

    /** 문자열에서 파싱 */
    public static OutboxTargetKey fromKey(String key) {
        String[] parts = key.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("잘못된 타겟 키 형식: " + key);
        }
        DiscountTargetType type = DiscountTargetType.valueOf(parts[0]);
        long id = Long.parseLong(parts[1]);
        return new OutboxTargetKey(type, id);
    }
}
