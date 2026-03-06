package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductInvalidPriceException 테스트")
class ProductInvalidPriceExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("regularPrice와 currentPrice로 예외를 생성한다")
        void createWithRegularPriceAndCurrentPrice() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(5000, 10000);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.code()).isEqualTo("PRD-004");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("예외 메시지에 regularPrice와 currentPrice가 포함된다")
        void messageContainsPriceValues() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(5000, 10000);

            // then
            assertThat(exception.getMessage()).contains("5000");
            assertThat(exception.getMessage()).contains("10000");
        }

        @Test
        @DisplayName("예외 메시지에 '가격 체계가 유효하지 않습니다' 문구가 포함된다")
        void messageContainsInvalidPriceDescription() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(1000, 2000);

            // then
            assertThat(exception.getMessage()).contains("가격 체계가 유효하지 않습니다");
        }

        @Test
        @DisplayName("ErrorCode는 PRODUCT_INVALID_PRICE이다")
        void errorCodeIsProductInvalidPrice() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(10000, 15000);

            // then
            assertThat(exception.code())
                    .isEqualTo(ProductErrorCode.PRODUCT_INVALID_PRICE.getCode());
            assertThat(exception.httpStatus())
                    .isEqualTo(ProductErrorCode.PRODUCT_INVALID_PRICE.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductInvalidPriceException은 DomainException을 상속한다")
        void extendsDomainException() {
            // given
            ProductInvalidPriceException exception = new ProductInvalidPriceException(5000, 10000);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName(
                "ProductInvalidPriceException은 ProductException을 상속하지 않는다 (DomainException 직접 상속)")
        void extendsDomainExceptionNotProductException() {
            // given
            ProductInvalidPriceException exception = new ProductInvalidPriceException(5000, 10000);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("context 정보 테스트")
    class ContextTest {

        @Test
        @DisplayName("동일한 가격으로 예외를 생성할 수 있다")
        void createWithSamePrices() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(0, 0);

            // then
            assertThat(exception.getMessage()).contains("0");
        }

        @Test
        @DisplayName("음수 가격으로도 예외를 생성할 수 있다")
        void createWithNegativePrices() {
            // when
            ProductInvalidPriceException exception = new ProductInvalidPriceException(-1000, -500);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("-1000");
            assertThat(exception.getMessage()).contains("-500");
        }
    }
}
