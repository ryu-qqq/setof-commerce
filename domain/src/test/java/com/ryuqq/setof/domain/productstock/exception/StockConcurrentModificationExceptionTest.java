package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StockConcurrentModificationException 테스트")
class StockConcurrentModificationExceptionTest {

    @Test
    @DisplayName("예외 생성 시 모든 정보 포함")
    void shouldContainAllInformation() {
        // given
        Long productId = 100L;
        int retryCount = 3;

        // when
        StockConcurrentModificationException exception =
                new StockConcurrentModificationException(productId, retryCount);

        // then
        assertThat(exception.getMessage()).contains("100");
        assertThat(exception.getMessage()).contains("3");
        assertThat(exception.getErrorCode())
                .isEqualTo(ProductStockErrorCode.STOCK_CONCURRENT_MODIFICATION);
    }

    @Test
    @DisplayName("context에 모든 필드 포함")
    void shouldContainAllFieldsInContext() {
        // given
        Long productId = 100L;
        int retryCount = 3;

        // when
        StockConcurrentModificationException exception =
                new StockConcurrentModificationException(productId, retryCount);

        // then
        assertThat(exception.args()).containsKey("productId");
        assertThat(exception.args()).containsKey("retryCount");
        assertThat(exception.args().get("productId")).isEqualTo(100L);
        assertThat(exception.args().get("retryCount")).isEqualTo(3);
    }

    @Test
    @DisplayName("null productId 처리")
    void shouldHandleNullProductId() {
        // when
        StockConcurrentModificationException exception =
                new StockConcurrentModificationException(null, 3);

        // then
        assertThat(exception.getMessage()).contains("null");
        assertThat(exception.args().get("productId")).isEqualTo("null");
    }

    @Test
    @DisplayName("HTTP 상태 코드 409 Conflict")
    void shouldReturn409HttpStatus() {
        // when
        StockConcurrentModificationException exception =
                new StockConcurrentModificationException(100L, 3);

        // then
        assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(409);
    }
}
