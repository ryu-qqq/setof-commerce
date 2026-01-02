package com.ryuqq.setof.adapter.out.persistence.discount.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountGroupType;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTargetTypeEnum;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTypeEnum;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DiscountPolicyQueryAdapter 통합 테스트
 *
 * <p>DiscountPolicyQueryPort 구현체의 조회 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("DiscountPolicyQueryAdapter 통합 테스트")
class DiscountPolicyQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private DiscountPolicyQueryAdapter discountPolicyQueryAdapter;

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Instant PAST_TIME = Instant.parse("2024-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 100L;
    private static final Long OTHER_SELLER_ID = 200L;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        @Test
        @DisplayName("성공 - ID로 정책을 조회한다")
        void findById_existingPolicy_returnsPolicy() {
            // Given
            DiscountPolicyJpaEntity entity = persistAndFlush(createActiveRateDiscountEntity());
            flushAndClear();

            // When
            Optional<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findById(DiscountPolicyId.of(entity.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getIdValue()).isEqualTo(entity.getId());
            assertThat(result.get().getPolicyNameValue()).isEqualTo("활성 정률 할인");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회시 빈 Optional 반환")
        void findById_nonExistingPolicy_returnsEmpty() {
            // Given
            Long nonExistingId = 99999L;

            // When
            Optional<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findById(DiscountPolicyId.of(nonExistingId));

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerId 메서드")
    class FindBySellerId {

        @Test
        @DisplayName("성공 - 셀러 ID로 모든 정책을 조회한다 (삭제 제외)")
        void findBySellerId_excludeDeleted_returnsPolicies() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(),
                    createInactiveRateDiscountEntity(),
                    createDeletedEntity());
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findBySellerId(TEST_SELLER_ID, false);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(p -> !p.isDeleted());
        }

        @Test
        @DisplayName("성공 - 셀러 ID로 삭제된 정책도 포함하여 조회한다")
        void findBySellerId_includeDeleted_returnsPolicies() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(),
                    createInactiveRateDiscountEntity(),
                    createDeletedEntity());
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findBySellerId(TEST_SELLER_ID, true);

            // Then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("성공 - 다른 셀러의 정책은 조회되지 않는다")
        void findBySellerId_differentSeller_returnsEmpty() {
            // Given
            persistAndFlush(createActiveRateDiscountEntity());
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findBySellerId(OTHER_SELLER_ID, false);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerIdAndGroup 메서드")
    class FindBySellerIdAndGroup {

        @Test
        @DisplayName("성공 - 셀러 ID와 그룹으로 활성 정책만 조회한다")
        void findBySellerIdAndGroup_activeOnly_returnsPolicies() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(), // PRODUCT, active
                    createInactiveRateDiscountEntity(), // PRODUCT, inactive
                    createCartDiscountEntity()); // CART, active
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findBySellerIdAndGroup(
                            TEST_SELLER_ID, DiscountGroup.PRODUCT, true);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDiscountGroup()).isEqualTo(DiscountGroup.PRODUCT);
            assertThat(result.get(0).isActive()).isTrue();
        }

        @Test
        @DisplayName("성공 - 셀러 ID와 그룹으로 모든 정책을 조회한다")
        void findBySellerIdAndGroup_includeInactive_returnsPolicies() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(),
                    createInactiveRateDiscountEntity(),
                    createCartDiscountEntity());
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findBySellerIdAndGroup(
                            TEST_SELLER_ID, DiscountGroup.PRODUCT, false);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(p -> p.getDiscountGroup() == DiscountGroup.PRODUCT);
        }
    }

    @Nested
    @DisplayName("findByTargetTypeAndTargetId 메서드")
    class FindByTargetTypeAndTargetId {

        @Test
        @DisplayName("성공 - 대상 타입과 ID로 활성 정책을 조회한다")
        void findByTargetTypeAndTargetId_activeOnly_returnsPolicies() {
            // Given
            persistAll(
                    createPolicyWithTargetId(100L, true),
                    createPolicyWithTargetId(100L, false),
                    createPolicyWithTargetId(200L, true));
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findByTargetTypeAndTargetId(
                            DiscountTargetType.PRODUCT, 100L, true);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("findValidPolicies 메서드")
    class FindValidPolicies {

        @Test
        @DisplayName("성공 - 현재 유효한 정책만 조회한다")
        void findValidPolicies_returnsOnlyValidPolicies() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(), // 유효
                    createInactiveRateDiscountEntity(), // 비활성
                    createExpiredEntity()); // 만료
            flushAndClear();

            // When
            List<DiscountPolicy> result =
                    discountPolicyQueryAdapter.findValidPolicies(TEST_SELLER_ID);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("countBySellerId 메서드")
    class CountBySellerId {

        @Test
        @DisplayName("성공 - 셀러의 정책 개수를 조회한다 (삭제 제외)")
        void countBySellerId_excludeDeleted_returnsCount() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(),
                    createInactiveRateDiscountEntity(),
                    createDeletedEntity());
            flushAndClear();

            // When
            long count = discountPolicyQueryAdapter.countBySellerId(TEST_SELLER_ID, false);

            // Then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("성공 - 셀러의 전체 정책 개수를 조회한다 (삭제 포함)")
        void countBySellerId_includeDeleted_returnsCount() {
            // Given
            persistAll(
                    createActiveRateDiscountEntity(),
                    createInactiveRateDiscountEntity(),
                    createDeletedEntity());
            flushAndClear();

            // When
            long count = discountPolicyQueryAdapter.countBySellerId(TEST_SELLER_ID, true);

            // Then
            assertThat(count).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("existsById 메서드")
    class ExistsById {

        @Test
        @DisplayName("성공 - 존재하는 ID인 경우 true 반환")
        void existsById_existingPolicy_returnsTrue() {
            // Given
            DiscountPolicyJpaEntity entity = persistAndFlush(createActiveRateDiscountEntity());
            flushAndClear();

            // When
            boolean exists =
                    discountPolicyQueryAdapter.existsById(DiscountPolicyId.of(entity.getId()));

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID인 경우 false 반환")
        void existsById_nonExistingPolicy_returnsFalse() {
            // Given
            Long nonExistingId = 99999L;

            // When
            boolean exists =
                    discountPolicyQueryAdapter.existsById(DiscountPolicyId.of(nonExistingId));

            // Then
            assertThat(exists).isFalse();
        }
    }

    // ========== Helper Methods - Entity Objects ==========

    private DiscountPolicyJpaEntity createActiveRateDiscountEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "활성 정률 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                FIXED_TIME,
                FUTURE_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createInactiveRateDiscountEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "비활성 정률 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                FIXED_TIME,
                FUTURE_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                false,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createDeletedEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "삭제된 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                FIXED_TIME,
                FUTURE_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                false,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    private DiscountPolicyJpaEntity createCartDiscountEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "장바구니 할인",
                DiscountGroupType.CART,
                DiscountTypeEnum.FIXED_PRICE,
                DiscountTargetTypeEnum.ALL,
                null,
                null,
                5000L,
                null,
                50000L,
                FIXED_TIME,
                FUTURE_TIME,
                2,
                500,
                new BigDecimal("60"),
                new BigDecimal("40"),
                200,
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createPolicyWithTargetId(Long targetId, boolean isActive) {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "상품별 할인 " + targetId,
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.PRODUCT,
                String.valueOf(targetId),
                new BigDecimal("15"),
                null,
                20000L,
                10000L,
                FIXED_TIME,
                FUTURE_TIME,
                null,
                null,
                new BigDecimal("30"),
                new BigDecimal("70"),
                50,
                isActive,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicyJpaEntity createExpiredEntity() {
        return DiscountPolicyJpaEntity.of(
                null,
                TEST_SELLER_ID,
                "만료된 할인",
                DiscountGroupType.PRODUCT,
                DiscountTypeEnum.RATE,
                DiscountTargetTypeEnum.ALL,
                null,
                new BigDecimal("10"),
                null,
                10000L,
                30000L,
                PAST_TIME,
                PAST_TIME,
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                true,
                PAST_TIME,
                PAST_TIME,
                null);
    }
}
