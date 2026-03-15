package com.ryuqq.setof.adapter.out.persistence.discountpolicy.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DiscountTargetConditionBuilderTest - 할인 적용 대상 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountTargetConditionBuilder 단위 테스트")
class DiscountTargetConditionBuilderTest {

    private DiscountTargetConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new DiscountTargetConditionBuilder();
    }

    // ========================================================================
    // 1. policyIdEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("policyIdEq 메서드 테스트")
    class PolicyIdEqTest {

        @Test
        @DisplayName("유효한 정책 ID 입력 시 BooleanExpression을 반환합니다")
        void policyIdEq_WithValidPolicyId_ReturnsBooleanExpression() {
            // given
            Long policyId = 1L;

            // when
            BooleanExpression result = conditionBuilder.policyIdEq(policyId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 정책 ID 입력 시 null을 반환합니다")
        void policyIdEq_WithNullPolicyId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.policyIdEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 2. policyIdIn 테스트
    // ========================================================================

    @Nested
    @DisplayName("policyIdIn 메서드 테스트")
    class PolicyIdInTest {

        @Test
        @DisplayName("유효한 정책 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void policyIdIn_WithValidPolicyIds_ReturnsBooleanExpression() {
            // given
            List<Long> policyIds = List.of(1L, 2L, 3L);

            // when
            BooleanExpression result = conditionBuilder.policyIdIn(policyIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 목록 입력 시 null을 반환합니다")
        void policyIdIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.policyIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 목록 입력 시 null을 반환합니다")
        void policyIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.policyIdIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 3. targetTypeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("targetTypeEq 메서드 테스트")
    class TargetTypeEqTest {

        @Test
        @DisplayName("PRODUCT 입력 시 BooleanExpression을 반환합니다")
        void targetTypeEq_WithProduct_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.targetTypeEq(DiscountTargetJpaEntity.TargetType.PRODUCT);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("BRAND 입력 시 BooleanExpression을 반환합니다")
        void targetTypeEq_WithBrand_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.targetTypeEq(DiscountTargetJpaEntity.TargetType.BRAND);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("SELLER 입력 시 BooleanExpression을 반환합니다")
        void targetTypeEq_WithSeller_ReturnsBooleanExpression() {
            // when
            BooleanExpression result =
                    conditionBuilder.targetTypeEq(DiscountTargetJpaEntity.TargetType.SELLER);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void targetTypeEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.targetTypeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. targetIdEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("targetIdEq 메서드 테스트")
    class TargetIdEqTest {

        @Test
        @DisplayName("유효한 대상 ID 입력 시 BooleanExpression을 반환합니다")
        void targetIdEq_WithValidTargetId_ReturnsBooleanExpression() {
            // given
            Long targetId = 100L;

            // when
            BooleanExpression result = conditionBuilder.targetIdEq(targetId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 대상 ID 입력 시 null을 반환합니다")
        void targetIdEq_WithNullTargetId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.targetIdEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 5. activeEq 테스트
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
}
