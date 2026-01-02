package com.ryuqq.setof.domain.discount.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.exception.InvalidCostShareException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** CostShare Value Object 테스트 */
@DisplayName("CostShare Value Object")
class CostShareTest {

    @Nested
    @DisplayName("생성 검증")
    class Creation {

        @Test
        @DisplayName("유효한 비용 분담 비율을 생성할 수 있다")
        void shouldCreateValidCostShare() {
            // when
            CostShare share = CostShare.of(BigDecimal.valueOf(70), BigDecimal.valueOf(30));

            // then
            assertEquals(new BigDecimal("70.00"), share.platformRatio());
            assertEquals(new BigDecimal("30.00"), share.sellerRatio());
        }

        @Test
        @DisplayName("플랫폼 전액 부담을 생성할 수 있다")
        void shouldCreatePlatformOnlyCostShare() {
            // when
            CostShare share = CostShare.platformOnly();

            // then
            assertTrue(share.isPlatformOnly());
            assertFalse(share.isSellerOnly());
        }

        @Test
        @DisplayName("셀러 전액 부담을 생성할 수 있다")
        void shouldCreateSellerOnlyCostShare() {
            // when
            CostShare share = CostShare.sellerOnly();

            // then
            assertFalse(share.isPlatformOnly());
            assertTrue(share.isSellerOnly());
        }

        @Test
        @DisplayName("균등 분담을 생성할 수 있다")
        void shouldCreateEqualShareCostShare() {
            // when
            CostShare share = CostShare.equalShare();

            // then
            assertEquals(new BigDecimal("50.00"), share.platformRatio());
            assertEquals(new BigDecimal("50.00"), share.sellerRatio());
        }

        @Test
        @DisplayName("합이 100%가 아니면 예외가 발생한다")
        void shouldThrowExceptionWhenSumIsNot100() {
            // when & then
            assertThrows(
                    InvalidCostShareException.class,
                    () -> CostShare.of(BigDecimal.valueOf(60), BigDecimal.valueOf(30)));
        }

        @Test
        @DisplayName("null 값은 예외가 발생한다")
        void shouldThrowExceptionForNullValues() {
            // when & then
            assertThrows(
                    InvalidCostShareException.class,
                    () -> CostShare.of(null, BigDecimal.valueOf(100)));
            assertThrows(
                    InvalidCostShareException.class,
                    () -> CostShare.of(BigDecimal.valueOf(100), null));
        }

        @Test
        @DisplayName("음수 비율은 예외가 발생한다")
        void shouldThrowExceptionForNegativeRatio() {
            // when & then
            assertThrows(
                    InvalidCostShareException.class,
                    () -> CostShare.of(BigDecimal.valueOf(-10), BigDecimal.valueOf(110)));
        }

        @Test
        @DisplayName("100%를 초과하는 비율은 예외가 발생한다")
        void shouldThrowExceptionForRatioOver100() {
            // when & then
            assertThrows(
                    InvalidCostShareException.class,
                    () -> CostShare.of(BigDecimal.valueOf(110), BigDecimal.valueOf(-10)));
        }
    }

    @Nested
    @DisplayName("비용 계산")
    class CostCalculation {

        @Test
        @DisplayName("플랫폼 부담 금액을 계산할 수 있다")
        void shouldCalculatePlatformCost() {
            // given
            CostShare share = CostShare.of(BigDecimal.valueOf(70), BigDecimal.valueOf(30));

            // when
            long platformCost = share.calculatePlatformCost(10000L);

            // then
            assertEquals(7000L, platformCost);
        }

        @Test
        @DisplayName("셀러 부담 금액을 계산할 수 있다")
        void shouldCalculateSellerCost() {
            // given
            CostShare share = CostShare.of(BigDecimal.valueOf(70), BigDecimal.valueOf(30));

            // when
            long sellerCost = share.calculateSellerCost(10000L);

            // then
            assertEquals(3000L, sellerCost);
        }

        @Test
        @DisplayName("플랫폼과 셀러 비용의 합이 원 금액과 같다")
        void shouldSumToOriginalAmount() {
            // given
            CostShare share = CostShare.of(BigDecimal.valueOf(75), BigDecimal.valueOf(25));
            long discountAmount = 9999L; // 소수점 테스트

            // when
            long platformCost = share.calculatePlatformCost(discountAmount);
            long sellerCost = share.calculateSellerCost(discountAmount);

            // then
            assertEquals(discountAmount, platformCost + sellerCost);
        }
    }
}
