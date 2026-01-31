package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerAddressConditionBuilderTest - 셀러 주소 조건 빌더 단위 테스트.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerAddressConditionBuilder 단위 테스트")
class SellerAddressConditionBuilderTest {

    private SellerAddressConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new SellerAddressConditionBuilder();
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
    // 3. addressTypeEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("addressTypeEq 메서드 테스트")
    class AddressTypeEqTest {

        @Test
        @DisplayName("유효한 주소 유형 입력 시 BooleanExpression을 반환합니다")
        void addressTypeEq_WithValidType_ReturnsBooleanExpression() {
            // given
            String addressType = "SHIPPING";

            // when
            BooleanExpression result = conditionBuilder.addressTypeEq(addressType);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("RETURN 유형 입력 시 BooleanExpression을 반환합니다")
        void addressTypeEq_WithReturnType_ReturnsBooleanExpression() {
            // given
            String addressType = "RETURN";

            // when
            BooleanExpression result = conditionBuilder.addressTypeEq(addressType);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 주소 유형 입력 시 null을 반환합니다")
        void addressTypeEq_WithNullType_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.addressTypeEq(null);

            // then
            assertThat(result).isNull();
        }
    }

    // ========================================================================
    // 4. defaultAddressEq 테스트
    // ========================================================================

    @Nested
    @DisplayName("defaultAddressEq 메서드 테스트")
    class DefaultAddressEqTest {

        @Test
        @DisplayName("true 입력 시 BooleanExpression을 반환합니다")
        void defaultAddressEq_WithTrue_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.defaultAddressEq(Boolean.TRUE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("false 입력 시 BooleanExpression을 반환합니다")
        void defaultAddressEq_WithFalse_ReturnsBooleanExpression() {
            // when
            BooleanExpression result = conditionBuilder.defaultAddressEq(Boolean.FALSE);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("null 입력 시 null을 반환합니다")
        void defaultAddressEq_WithNull_ReturnsNull() {
            // when
            BooleanExpression result = conditionBuilder.defaultAddressEq(null);

            // then
            assertThat(result).isNull();
        }
    }
}
