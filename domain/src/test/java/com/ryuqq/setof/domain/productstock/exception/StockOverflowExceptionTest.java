package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StockOverflowException 테스트")
class StockOverflowExceptionTest {

    @Test
    @DisplayName("예외 생성 시 모든 정보 포함")
    void shouldContainAllInformation() {
        // given
        Long productId = 100L;
        int currentStock = Integer.MAX_VALUE - 10;
        int restoreQuantity = 20;
        String reason = "최대 재고 수량을 초과합니다";

        // when
        StockOverflowException exception =
                new StockOverflowException(productId, currentStock, restoreQuantity, reason);

        // then
        assertThat(exception.getMessage()).contains("100");
        assertThat(exception.getMessage()).contains("20");
        assertThat(exception.getMessage()).contains("최대 재고 수량을 초과합니다");
        assertThat(exception.getErrorCode()).isEqualTo(ProductStockErrorCode.STOCK_OVERFLOW);
    }

    @Test
    @DisplayName("context에 모든 필드 포함")
    void shouldContainAllFieldsInContext() {
        // given
        Long productId = 100L;
        int currentStock = 100;
        int restoreQuantity = 20;
        String reason = "테스트";

        // when
        StockOverflowException exception =
                new StockOverflowException(productId, currentStock, restoreQuantity, reason);

        // then
        assertThat(exception.args()).containsKey("productId");
        assertThat(exception.args()).containsKey("currentStock");
        assertThat(exception.args()).containsKey("restoreQuantity");
        assertThat(exception.args()).containsKey("reason");
    }

    @Test
    @DisplayName("null productId 처리")
    void shouldHandleNullProductId() {
        // when
        StockOverflowException exception = new StockOverflowException(null, 100, 20, "테스트");

        // then
        assertThat(exception.getMessage()).contains("null");
        assertThat(exception.args().get("productId")).isEqualTo("null");
    }
}
