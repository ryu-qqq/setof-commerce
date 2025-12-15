package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** InvalidPriceException 단위 테스트 */
@DisplayName("InvalidPriceException 단위 테스트")
class InvalidPriceExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - regularPrice, currentPrice, reason으로 예외를 생성한다")
        void shouldCreateWithPricesAndReason() {
            // Given
            BigDecimal regularPrice = BigDecimal.valueOf(10000);
            BigDecimal currentPrice = BigDecimal.valueOf(15000);
            String reason = "판매가가 정가보다 클 수 없습니다";

            // When
            InvalidPriceException exception =
                    new InvalidPriceException(regularPrice, currentPrice, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 가격");
            assertThat(exception.getMessage()).contains("10000");
            assertThat(exception.getMessage()).contains("15000");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_PRICE);
        }

        @Test
        @DisplayName("성공 - null regularPrice로 예외를 생성한다")
        void shouldCreateWithNullRegularPrice() {
            // Given
            BigDecimal currentPrice = BigDecimal.valueOf(10000);
            String reason = "정가는 null일 수 없습니다";

            // When
            InvalidPriceException exception = new InvalidPriceException(null, currentPrice, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 가격");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_PRICE);
        }

        @Test
        @DisplayName("성공 - null currentPrice로 예외를 생성한다")
        void shouldCreateWithNullCurrentPrice() {
            // Given
            BigDecimal regularPrice = BigDecimal.valueOf(10000);
            String reason = "판매가는 null일 수 없습니다";

            // When
            InvalidPriceException exception = new InvalidPriceException(regularPrice, null, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 가격");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_PRICE);
        }

        @Test
        @DisplayName("성공 - 음수 가격으로 예외를 생성한다")
        void shouldCreateWithNegativePrice() {
            // Given
            BigDecimal regularPrice = BigDecimal.valueOf(-1000);
            BigDecimal currentPrice = BigDecimal.valueOf(10000);
            String reason = "가격은 0 이상이어야 합니다";

            // When
            InvalidPriceException exception =
                    new InvalidPriceException(regularPrice, currentPrice, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 가격");
            assertThat(exception.getMessage()).contains("-1000");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_PRICE);
        }
    }
}
