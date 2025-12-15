package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidProductStockIdException 테스트")
class InvalidProductStockIdExceptionTest {

    @Test
    @DisplayName("예외 생성 시 올바른 메시지 포함")
    void shouldContainCorrectMessage() {
        // given
        Long invalidId = -1L;

        // when
        InvalidProductStockIdException exception = new InvalidProductStockIdException(invalidId);

        // then
        assertThat(exception.getMessage()).contains("-1");
        assertThat(exception.getErrorCode())
                .isEqualTo(ProductStockErrorCode.INVALID_PRODUCT_STOCK_ID);
    }

    @Test
    @DisplayName("null ID로 예외 생성")
    void shouldHandleNullId() {
        // when
        InvalidProductStockIdException exception = new InvalidProductStockIdException(null);

        // then
        assertThat(exception.getMessage()).contains("null");
        assertThat(exception.getErrorCode())
                .isEqualTo(ProductStockErrorCode.INVALID_PRODUCT_STOCK_ID);
    }

    @Test
    @DisplayName("context에 productStockId 포함")
    void shouldContainProductStockIdInContext() {
        // given
        Long invalidId = 0L;

        // when
        InvalidProductStockIdException exception = new InvalidProductStockIdException(invalidId);

        // then
        assertThat(exception.args()).containsKey("productStockId");
    }
}
