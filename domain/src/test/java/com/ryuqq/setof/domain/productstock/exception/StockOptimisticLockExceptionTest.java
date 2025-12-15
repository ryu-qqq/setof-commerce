package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StockOptimisticLockException 테스트")
class StockOptimisticLockExceptionTest {

    @Test
    @DisplayName("예외 생성 시 모든 정보 포함")
    void shouldContainAllInformation() {
        // given
        Long productId = 100L;

        // when
        StockOptimisticLockException exception = new StockOptimisticLockException(productId);

        // then
        assertThat(exception.getMessage()).contains("100");
        assertThat(exception.getErrorCode()).isEqualTo(ProductStockErrorCode.STOCK_OPTIMISTIC_LOCK);
        assertThat(exception.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("context에 모든 필드 포함")
    void shouldContainAllFieldsInContext() {
        // given
        Long productId = 100L;

        // when
        StockOptimisticLockException exception = new StockOptimisticLockException(productId);

        // then
        assertThat(exception.args()).containsKey("productId");
        assertThat(exception.args().get("productId")).isEqualTo(100L);
    }

    @Test
    @DisplayName("null productId 처리")
    void shouldHandleNullProductId() {
        // when
        StockOptimisticLockException exception = new StockOptimisticLockException(null);

        // then
        assertThat(exception.getMessage()).contains("null");
        assertThat(exception.args().get("productId")).isEqualTo("null");
        assertThat(exception.getProductId()).isNull();
    }

    @Test
    @DisplayName("HTTP 상태 코드 409 Conflict")
    void shouldReturn409HttpStatus() {
        // when
        StockOptimisticLockException exception = new StockOptimisticLockException(100L);

        // then
        assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(409);
    }
}
