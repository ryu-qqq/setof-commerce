package com.ryuqq.setof.adapter.out.persistence.discountpolicy;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity.TargetType;
import java.time.Instant;

/**
 * DiscountTargetJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 DiscountTargetJpaEntity 관련 객체들을 생성합니다.
 */
public final class DiscountTargetJpaEntityFixtures {

    private DiscountTargetJpaEntityFixtures() {}

    public static final long DEFAULT_TARGET_ID = 101L;

    /** 특정 정책에 연결된 새 활성 PRODUCT 타입 대상 Entity (ID null). */
    public static DiscountTargetJpaEntity newActiveProductTarget(long discountPolicyId) {
        Instant now = Instant.now();
        return DiscountTargetJpaEntity.create(
                null, discountPolicyId, TargetType.PRODUCT, DEFAULT_TARGET_ID, true, now, now);
    }

    /** 특정 정책에 연결된 새 활성 SELLER 타입 대상 Entity (ID null). */
    public static DiscountTargetJpaEntity newActiveSellerTarget(long discountPolicyId) {
        Instant now = Instant.now();
        return DiscountTargetJpaEntity.create(
                null, discountPolicyId, TargetType.SELLER, DEFAULT_TARGET_ID, true, now, now);
    }

    /** 특정 정책에 연결된 새 활성 BRAND 타입 대상 Entity (ID null). */
    public static DiscountTargetJpaEntity newActiveBrandTarget(long discountPolicyId) {
        Instant now = Instant.now();
        return DiscountTargetJpaEntity.create(
                null, discountPolicyId, TargetType.BRAND, DEFAULT_TARGET_ID, true, now, now);
    }

    /** 특정 정책에 연결된 새 비활성 PRODUCT 타입 대상 Entity (ID null). */
    public static DiscountTargetJpaEntity newInactiveProductTarget(long discountPolicyId) {
        Instant now = Instant.now();
        return DiscountTargetJpaEntity.create(
                null, discountPolicyId, TargetType.PRODUCT, DEFAULT_TARGET_ID, false, now, now);
    }

    /** targetId를 지정한 새 활성 PRODUCT 타입 대상 Entity (ID null). */
    public static DiscountTargetJpaEntity newActiveProductTarget(
            long discountPolicyId, long targetId) {
        Instant now = Instant.now();
        return DiscountTargetJpaEntity.create(
                null, discountPolicyId, TargetType.PRODUCT, targetId, true, now, now);
    }
}
