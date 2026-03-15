package com.ryuqq.setof.adapter.out.persistence.discountpolicy;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity.ApplicationType;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity.DiscountMethod;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity.PublisherType;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity.StackingGroup;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * DiscountPolicyJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 DiscountPolicyJpaEntity 관련 객체들을 생성합니다.
 */
public final class DiscountPolicyJpaEntityFixtures {

    private DiscountPolicyJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final String DEFAULT_NAME = "테스트 할인 정책";
    public static final String DEFAULT_DESCRIPTION = "테스트용 할인 정책 메모";
    public static final DiscountMethod DEFAULT_DISCOUNT_METHOD = DiscountMethod.RATE;
    public static final Double DEFAULT_DISCOUNT_RATE = 10.0;
    public static final Integer DEFAULT_MAX_DISCOUNT_AMOUNT = 10000;
    public static final ApplicationType DEFAULT_APPLICATION_TYPE = ApplicationType.IMMEDIATE;
    public static final PublisherType DEFAULT_PUBLISHER_TYPE = PublisherType.ADMIN;
    public static final StackingGroup DEFAULT_STACKING_GROUP = StackingGroup.PLATFORM_INSTANT;
    public static final int DEFAULT_PRIORITY = 1;

    /** 새로 생성될 활성 RATE 타입 할인 정책 Entity (ID null). */
    public static DiscountPolicyJpaEntity newActiveRateEntity() {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                null,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DEFAULT_DISCOUNT_RATE,
                null,
                DEFAULT_MAX_DISCOUNT_AMOUNT,
                true,
                null,
                DEFAULT_APPLICATION_TYPE,
                DEFAULT_PUBLISHER_TYPE,
                null,
                DEFAULT_STACKING_GROUP,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                true,
                null,
                now,
                now);
    }

    /** 이름을 지정한 새로운 활성 RATE 타입 할인 정책 Entity (ID null). */
    public static DiscountPolicyJpaEntity newActiveRateEntityWithName(String name) {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                null,
                name,
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DEFAULT_DISCOUNT_RATE,
                null,
                DEFAULT_MAX_DISCOUNT_AMOUNT,
                true,
                null,
                DEFAULT_APPLICATION_TYPE,
                DEFAULT_PUBLISHER_TYPE,
                null,
                DEFAULT_STACKING_GROUP,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                true,
                null,
                now,
                now);
    }

    /** 새로 생성될 활성 FIXED_AMOUNT 타입 할인 정책 Entity (ID null). */
    public static DiscountPolicyJpaEntity newActiveFixedAmountEntity() {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                null,
                "고정금액 할인 정책",
                "고정금액 할인 테스트",
                DiscountMethod.FIXED_AMOUNT,
                null,
                5000,
                null,
                false,
                null,
                DEFAULT_APPLICATION_TYPE,
                DEFAULT_PUBLISHER_TYPE,
                null,
                DEFAULT_STACKING_GROUP,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                true,
                null,
                now,
                now);
    }

    /** 새로 생성될 비활성 RATE 타입 할인 정책 Entity (ID null). */
    public static DiscountPolicyJpaEntity newInactiveRateEntity() {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                null,
                "비활성 할인 정책",
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DEFAULT_DISCOUNT_RATE,
                null,
                DEFAULT_MAX_DISCOUNT_AMOUNT,
                true,
                null,
                DEFAULT_APPLICATION_TYPE,
                DEFAULT_PUBLISHER_TYPE,
                null,
                DEFAULT_STACKING_GROUP,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                false,
                null,
                now,
                now);
    }

    /** ID가 있는 활성 RATE 타입 할인 정책 Entity (toDomain 변환 테스트용). */
    public static DiscountPolicyJpaEntity existingActiveRateEntity(Long id) {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                id,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DEFAULT_DISCOUNT_RATE,
                null,
                DEFAULT_MAX_DISCOUNT_AMOUNT,
                true,
                null,
                DEFAULT_APPLICATION_TYPE,
                DEFAULT_PUBLISHER_TYPE,
                null,
                DEFAULT_STACKING_GROUP,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                true,
                null,
                now,
                now);
    }

    /** SELLER 발행자 타입의 새 활성 할인 정책 Entity (ID null). */
    public static DiscountPolicyJpaEntity newActiveSellerEntity() {
        Instant now = Instant.now();
        Instant startAt = now.minus(1, ChronoUnit.DAYS);
        Instant endAt = now.plus(30, ChronoUnit.DAYS);

        return DiscountPolicyJpaEntity.create(
                null,
                "판매자 할인 정책",
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                20.0,
                null,
                20000,
                true,
                null,
                DEFAULT_APPLICATION_TYPE,
                PublisherType.SELLER,
                1L,
                StackingGroup.SELLER_INSTANT,
                DEFAULT_PRIORITY,
                startAt,
                endAt,
                0,
                0,
                true,
                null,
                now,
                now);
    }
}
