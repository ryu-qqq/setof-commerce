package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NotEnoughStockException 테스트")
class NotEnoughStockExceptionTest {

    @Test
    @DisplayName("예외 생성 시 모든 정보 포함")
    void shouldContainAllInformation() {
        // given
        Long productId = 100L;
        int currentStock = 10;
        int requestedQuantity = 20;
        String reason = "재고가 부족합니다";

        // when
        NotEnoughStockException exception =
                new NotEnoughStockException(productId, currentStock, requestedQuantity, reason);

        // then
        assertThat(exception.getMessage()).contains("100");
        assertThat(exception.getMessage()).contains("10");
        assertThat(exception.getMessage()).contains("20");
        assertThat(exception.getMessage()).contains("재고가 부족합니다");
        assertThat(exception.getErrorCode()).isEqualTo(ProductStockErrorCode.NOT_ENOUGH_STOCK);
    }

    @Test
    @DisplayName("context에 모든 필드 포함")
    void shouldContainAllFieldsInContext() {
        // given
        Long productId = 100L;
        int currentStock = 10;
        int requestedQuantity = 20;
        String reason = "테스트";

        // when
        NotEnoughStockException exception =
                new NotEnoughStockException(productId, currentStock, requestedQuantity, reason);

        // then
        assertThat(exception.args()).containsKey("productId");
        assertThat(exception.args()).containsKey("currentStock");
        assertThat(exception.args()).containsKey("requestedQuantity");
        assertThat(exception.args()).containsKey("reason");
    }

    @Test
    @DisplayName("null productId 처리")
    void shouldHandleNullProductId() {
        // when
        NotEnoughStockException exception = new NotEnoughStockException(null, 10, 20, "테스트");

        // then
        assertThat(exception.getMessage()).contains("null");
        assertThat(exception.args().get("productId")).isEqualTo("null");
    }
}
