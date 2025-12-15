package com.ryuqq.setof.domain.product.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.product.exception.InvalidPriceException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Price Value Object 테스트
 *
 * <p>가격 정보에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Price Value Object")
class PriceTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("정상적인 가격으로 Price 생성")
        void shouldCreatePriceWithValidValues() {
            // Given
            BigDecimal regularPrice = BigDecimal.valueOf(50000);
            BigDecimal currentPrice = BigDecimal.valueOf(45000);

            // When
            Price price = Price.of(regularPrice, currentPrice);

            // Then
            assertNotNull(price);
            assertEquals(regularPrice, price.regularPrice());
            assertEquals(currentPrice, price.currentPrice());
        }

        @Test
        @DisplayName("정가와 판매가가 같은 경우 생성")
        void shouldCreatePriceWhenRegularEqualsCurrentPrice() {
            // Given
            BigDecimal samePrice = BigDecimal.valueOf(30000);

            // When
            Price price = Price.of(samePrice, samePrice);

            // Then
            assertNotNull(price);
            assertEquals(samePrice, price.regularPrice());
            assertEquals(samePrice, price.currentPrice());
        }

        @Test
        @DisplayName("0원으로 Price 생성")
        void shouldCreatePriceWithZero() {
            // Given
            BigDecimal zero = BigDecimal.ZERO;

            // When
            Price price = Price.of(zero, zero);

            // Then
            assertNotNull(price);
            assertEquals(BigDecimal.ZERO, price.regularPrice());
            assertEquals(BigDecimal.ZERO, price.currentPrice());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("정가가 null이면 예외 발생")
        void shouldThrowExceptionWhenRegularPriceIsNull() {
            // When & Then
            assertThrows(
                    InvalidPriceException.class, () -> Price.of(null, BigDecimal.valueOf(10000)));
        }

        @Test
        @DisplayName("판매가가 null이면 예외 발생")
        void shouldThrowExceptionWhenCurrentPriceIsNull() {
            // When & Then
            assertThrows(
                    InvalidPriceException.class, () -> Price.of(BigDecimal.valueOf(10000), null));
        }

        @Test
        @DisplayName("정가가 음수이면 예외 발생")
        void shouldThrowExceptionWhenRegularPriceIsNegative() {
            // When & Then
            assertThrows(
                    InvalidPriceException.class,
                    () -> Price.of(BigDecimal.valueOf(-1000), BigDecimal.valueOf(10000)));
        }

        @Test
        @DisplayName("판매가가 음수이면 예외 발생")
        void shouldThrowExceptionWhenCurrentPriceIsNegative() {
            // When & Then
            assertThrows(
                    InvalidPriceException.class,
                    () -> Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(-1000)));
        }

        @Test
        @DisplayName("정가보다 판매가가 큰 경우 예외 발생")
        void shouldThrowExceptionWhenCurrentPriceExceedsRegularPrice() {
            // When & Then
            assertThrows(
                    InvalidPriceException.class,
                    () -> Price.of(BigDecimal.valueOf(10000), BigDecimal.valueOf(15000)));
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드 테스트")
    class BusinessMethodsTest {

        @Test
        @DisplayName("discountAmount()는 정가와 판매가의 차이를 반환한다")
        void shouldReturnCorrectDiscountAmount() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));

            // When
            BigDecimal discountAmount = price.discountAmount();

            // Then
            assertEquals(BigDecimal.valueOf(5000), discountAmount);
        }

        @Test
        @DisplayName("discountAmount()는 할인이 없으면 0을 반환한다")
        void shouldReturnZeroWhenNoDiscount() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(30000));

            // When
            BigDecimal discountAmount = price.discountAmount();

            // Then
            assertEquals(BigDecimal.ZERO, discountAmount);
        }

        @Test
        @DisplayName("discountPercentage()는 할인율을 반환한다")
        void shouldReturnCorrectDiscountPercentage() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(100000), BigDecimal.valueOf(80000));

            // When
            int discountPercentage = price.discountPercentage();

            // Then
            assertEquals(20, discountPercentage);
        }

        @Test
        @DisplayName("discountPercentage()는 할인이 없으면 0을 반환한다")
        void shouldReturnZeroDiscountPercentageWhenNoDiscount() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(30000));

            // When
            int discountPercentage = price.discountPercentage();

            // Then
            assertEquals(0, discountPercentage);
        }

        @Test
        @DisplayName("discountPercentage()는 정가가 0이면 0을 반환한다")
        void shouldReturnZeroDiscountPercentageWhenRegularPriceIsZero() {
            // Given
            Price price = Price.of(BigDecimal.ZERO, BigDecimal.ZERO);

            // When
            int discountPercentage = price.discountPercentage();

            // Then
            assertEquals(0, discountPercentage);
        }

        @Test
        @DisplayName("isDiscounted()는 할인이 있으면 true를 반환한다")
        void shouldReturnTrueWhenIsDiscounted() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));

            // When & Then
            assertTrue(price.isDiscounted());
        }

        @Test
        @DisplayName("isDiscounted()는 할인이 없으면 false를 반환한다")
        void shouldReturnFalseWhenNotDiscounted() {
            // Given
            Price price = Price.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(30000));

            // When & Then
            assertFalse(price.isDiscounted());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 Price는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            Price price1 = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));
            Price price2 = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));

            // When & Then
            assertEquals(price1, price2);
            assertEquals(price1.hashCode(), price2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Price는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            Price price1 = Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000));
            Price price2 = Price.of(BigDecimal.valueOf(60000), BigDecimal.valueOf(55000));

            // When & Then
            assertNotEquals(price1, price2);
        }
    }
}
