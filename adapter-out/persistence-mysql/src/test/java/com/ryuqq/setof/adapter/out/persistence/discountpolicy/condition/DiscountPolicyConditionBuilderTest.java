package com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicyConditionBuilderTest - 할인 정책 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyConditionBuilder 단위 테스트")
class DiscountPolicyConditionBuilderTest {

    private DiscountPolicyConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new DiscountPolicyConditionBuilder();
    }

    // ========================================================================
    // 1. idEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("idEq 메서드 테스트")
    class IdEqTest {

        @Test
        @DisplayName("유효한 ID 입력 시 BooleanExpression을 반환합니다")
        void idEq_WithValidId_ReturnsBooleanExpression() {
            // given
            Long id = 1L;

            // when
            BooleanExpression result = conditionBuilder.idEq(id);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null ID 입력 시 null을 반환합니다")
        void idEq_WithNullId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.idEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 2. sellerIdEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("sellerIdEq 메서드 테스트")
    class SellerIdEqTest {

        @Test
        @DisplayName("유효한 셀러 ID 입력 시 BooleanExpression을 반환합니다")
        void sellerIdEq_WithValidSellerId_ReturnsBooleanExpression() {
            // given
            Long sellerId = 1L;

            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(sellerId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 셀러 ID 입력 시 null을 반환합니다")
        void sellerIdEq_WithNullSellerId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 3. applicationTypeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("applicationTypeEq 메서드 테스트")
    class ApplicationTypeEqTest {

        @Test
        @DisplayName("IMMEDIATE 입력 시 BooleanExpression을 반환합니다")
        void applicationTypeEq_WithImmediate_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.applicationTypeEq(
                            DiscountPolicyJpaEntity.ApplicationType.IMMEDIATE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("COUPON 입력 시 BooleanExpression을 반환합니다")
        void applicationTypeEq_WithCoupon_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.applicationTypeEq(
                            DiscountPolicyJpaEntity.ApplicationType.COUPON);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void applicationTypeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.applicationTypeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. publisherTypeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("publisherTypeEq 메서드 테스트")
    class PublisherTypeEqTest {

        @Test
        @DisplayName("ADMIN 입력 시 BooleanExpression을 반환합니다")
        void publisherTypeEq_WithAdmin_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.publisherTypeEq(DiscountPolicyJpaEntity.PublisherType.ADMIN);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("SELLER 입력 시 BooleanExpression을 반환합니다")
        void publisherTypeEq_WithSeller_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.publisherTypeEq(DiscountPolicyJpaEntity.PublisherType.SELLER);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void publisherTypeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.publisherTypeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. stackingGroupEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("stackingGroupEq 메서드 테스트")
    class StackingGroupEqTest {

        @Test
        @DisplayName("PLATFORM_INSTANT 입력 시 BooleanExpression을 반환합니다")
        void stackingGroupEq_WithPlatformInstant_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.stackingGroupEq(
                            DiscountPolicyJpaEntity.StackingGroup.PLATFORM_INSTANT);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("SELLER_INSTANT 입력 시 BooleanExpression을 반환합니다")
        void stackingGroupEq_WithSellerInstant_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.stackingGroupEq(
                            DiscountPolicyJpaEntity.StackingGroup.SELLER_INSTANT);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void stackingGroupEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.stackingGroupEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 6. activeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("activeEq 메서드 테스트")
    class ActiveEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithTrue_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.activeEq(Boolean.TRUE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void activeEq_WithFalse_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.activeEq(Boolean.FALSE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void activeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.activeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 7. notDeleted 테스트
    // ========================================================================

    @Nested
    @DisplayName("notDeleted 메서드 테스트")
    class NotDeletedTest {

        @Test
        @DisplayName("삭제되지 않은 조건 BooleanExpression을 반환합니다")
        void notDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.notDeleted();

            // then
            assertThat(result).isNotNull();
        }
    }
}
