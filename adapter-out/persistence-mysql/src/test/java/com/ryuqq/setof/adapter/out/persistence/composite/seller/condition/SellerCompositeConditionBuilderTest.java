package com.ryuqq.setof.adapter.out.persistence.composite.seller.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerCompositeConditionBuilderTest - 셀러 Composite 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerCompositeConditionBuilder 단위 테스트")
class SellerCompositeConditionBuilderTest {

    private SellerCompositeConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new SellerCompositeConditionBuilder();
    }

    // ========================================================================
    // 1. Seller 조건 테스트
    // ========================================================================

    @Nested
    @DisplayName("Seller 조건 테스트")
    class SellerConditionsTest {

        @Test
        @DisplayName("유효한 셀러 ID 입력 시 BooleanExpression을 반환합니다")
        void sellerIdEq_WithValidId_ReturnsBooleanExpression() {
            // given
            Long sellerId = 1L;

            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(sellerId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 셀러 ID 입력 시 null을 반환합니다")
        void sellerIdEq_WithNullId_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("유효한 셀러 ID 목록 입력 시 BooleanExpression을 반환합니다")
        void sellerIdIn_WithValidIds_ReturnsBooleanExpression() {
            // given
            List<Long> sellerIds = List.of(1L, 2L, 3L);

            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(sellerIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 셀러 ID 목록 입력 시 null을 반환합니다")
        void sellerIdIn_WithNullList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(null);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("빈 셀러 ID 목록 입력 시 null을 반환합니다")
        void sellerIdIn_WithEmptyList_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerIdIn(Collections.emptyList());

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("삭제되지 않은 셀러 조건 BooleanExpression을 반환합니다")
        void sellerNotDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.sellerNotDeleted();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("true 활성 상태 입력 시 BooleanExpression을 반환합니다")
        void sellerActiveEq_WithTrue_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.sellerActiveEq(Boolean.TRUE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 활성 상태 입력 시 BooleanExpression을 반환합니다")
        void sellerActiveEq_WithFalse_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.sellerActiveEq(Boolean.FALSE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 활성 상태 입력 시 null을 반환합니다")
        void sellerActiveEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.sellerActiveEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 2. Address 조건 테스트
    // ========================================================================

    @Nested
    @DisplayName("Address 조건 테스트")
    class AddressConditionsTest {

        @Test
        @DisplayName("주소 셀러 ID 일치 조건 BooleanExpression을 반환합니다")
        void addressSellerIdEq_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.addressSellerIdEq();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("삭제되지 않은 주소 조건 BooleanExpression을 반환합니다")
        void addressNotDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.addressNotDeleted();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("기본 주소만 조건 BooleanExpression을 반환합니다")
        void addressDefaultOnly_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.addressDefaultOnly();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("주소 조인 조건 BooleanExpression을 반환합니다")
        void addressJoinCondition_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.addressJoinCondition();

            // then
            assertThat(result).isNotNull();
        }
    }

    // ========================================================================
    // 3. BusinessInfo 조건 테스트
    // ========================================================================

    @Nested
    @DisplayName("BusinessInfo 조건 테스트")
    class BusinessInfoConditionsTest {

        @Test
        @DisplayName("사업자 정보 셀러 ID 일치 조건 BooleanExpression을 반환합니다")
        void businessInfoSellerIdEq_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.businessInfoSellerIdEq();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("삭제되지 않은 사업자 정보 조건 BooleanExpression을 반환합니다")
        void businessInfoNotDeleted_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.businessInfoNotDeleted();

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("사업자 정보 조인 조건 BooleanExpression을 반환합니다")
        void businessInfoJoinCondition_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.businessInfoJoinCondition();

            // then
            assertThat(result).isNotNull();
        }
    }
}
