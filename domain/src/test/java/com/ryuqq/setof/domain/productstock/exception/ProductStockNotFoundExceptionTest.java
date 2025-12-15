package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductStockNotFoundException 테스트")
class ProductStockNotFoundExceptionTest {

    @Nested
    @DisplayName("ProductStockId로 생성")
    class WithProductStockIdTest {

        @Test
        @DisplayName("ProductStockId로 예외 생성")
        void shouldCreateWithProductStockId() {
            // given
            ProductStockId productStockId = ProductStockId.of(1L);

            // when
            ProductStockNotFoundException exception =
                    new ProductStockNotFoundException(productStockId);

            // then
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND);
            assertThat(exception.args()).containsKey("productStockId");
        }

        @Test
        @DisplayName("null ProductStockId 처리")
        void shouldHandleNullProductStockId() {
            // when
            ProductStockNotFoundException exception =
                    new ProductStockNotFoundException((ProductStockId) null);

            // then
            assertThat(exception.getMessage()).contains("null");
        }
    }

    @Nested
    @DisplayName("ProductId로 생성")
    class WithProductIdTest {

        @Test
        @DisplayName("ProductId로 예외 생성")
        void shouldCreateWithProductId() {
            // given
            ProductId productId = ProductId.of(100L);

            // when
            ProductStockNotFoundException exception = new ProductStockNotFoundException(productId);

            // then
            assertThat(exception.getMessage()).contains("100");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND);
            assertThat(exception.args()).containsKey("productId");
        }

        @Test
        @DisplayName("null ProductId 처리")
        void shouldHandleNullProductId() {
            // when
            ProductStockNotFoundException exception =
                    new ProductStockNotFoundException((ProductId) null);

            // then
            assertThat(exception.getMessage()).contains("null");
        }
    }

    @Nested
    @DisplayName("Long productId로 생성")
    class WithLongProductIdTest {

        @Test
        @DisplayName("Long productId로 예외 생성")
        void shouldCreateWithLongProductId() {
            // given
            Long productId = 100L;

            // when
            ProductStockNotFoundException exception = new ProductStockNotFoundException(productId);

            // then
            assertThat(exception.getMessage()).contains("100");
            assertThat(exception.getErrorCode())
                    .isEqualTo(ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND);
            assertThat(exception.args()).containsKey("productId");
        }

        @Test
        @DisplayName("null Long productId 처리")
        void shouldHandleNullLongProductId() {
            // when
            ProductStockNotFoundException exception =
                    new ProductStockNotFoundException((Long) null);

            // then
            assertThat(exception.getMessage()).contains("null");
        }
    }
}
