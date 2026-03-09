package com.ryuqq.setof.storage.legacy.composite.seller.condition;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyWebSellerCompositeConditionBuilder 단위 테스트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyWebSellerCompositeConditionBuilder 단위 테스트")
class LegacyWebSellerCompositeConditionBuilderTest {

    private LegacyWebSellerCompositeConditionBuilder conditionBuilder;

    @BeforeEach
    void setUp() {
        conditionBuilder = new LegacyWebSellerCompositeConditionBuilder();
    }

    @Nested
    @DisplayName("sellerIdEq 메서드 테스트")
    class SellerIdEqTest {

        @Test
        @DisplayName("sellerId가 null이 아닐 때 BooleanExpression을 반환합니다")
        void sellerIdEq_WithValidId_ReturnsBooleanExpression() {
            // given
            Long sellerId = 1L;

            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(sellerId);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("sellerId가 null일 때 null을 반환합니다")
        void sellerIdEq_WithNullId_ReturnsNull() {
            // given
            Long sellerId = null;

            // when
            BooleanExpression result = conditionBuilder.sellerIdEq(sellerId);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("registrationNumberEq 메서드 테스트")
    class RegistrationNumberEqTest {

        @Test
        @DisplayName("registrationNumber가 null이 아닐 때 BooleanExpression을 반환합니다")
        void registrationNumberEq_WithValidNumber_ReturnsBooleanExpression() {
            // given
            String registrationNumber = "123-45-67890";

            // when
            BooleanExpression result = conditionBuilder.registrationNumberEq(registrationNumber);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("registrationNumber가 null일 때 null을 반환합니다")
        void registrationNumberEq_WithNullNumber_ReturnsNull() {
            // given
            String registrationNumber = null;

            // when
            BooleanExpression result = conditionBuilder.registrationNumberEq(registrationNumber);

            // then
            assertThat(result).isNull();
        }
    }
}
