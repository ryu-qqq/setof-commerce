package com.ryuqq.setof.domain.productstock.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("ProductStockErrorCode 테스트")
class ProductStockErrorCodeTest {

    @ParameterizedTest
    @EnumSource(ProductStockErrorCode.class)
    @DisplayName("모든 에러 코드는 필수 값을 가져야 함")
    void shouldHaveRequiredValues(ProductStockErrorCode errorCode) {
        // when & then
        assertThat(errorCode.getCode()).isNotBlank();
        assertThat(errorCode.getHttpStatus()).isPositive();
        assertThat(errorCode.getMessage()).isNotBlank();
    }

    @Test
    @DisplayName("PRODUCT_STOCK_NOT_FOUND 에러 코드 확인")
    void shouldHaveCorrectProductStockNotFoundCode() {
        // given
        ProductStockErrorCode errorCode = ProductStockErrorCode.PRODUCT_STOCK_NOT_FOUND;

        // when & then
        assertThat(errorCode.getCode()).isEqualTo("STOCK-001");
        assertThat(errorCode.getHttpStatus()).isEqualTo(404);
        assertThat(errorCode.getMessage()).contains("찾을 수 없습니다");
    }

    @Test
    @DisplayName("INVALID_PRODUCT_STOCK_ID 에러 코드 확인")
    void shouldHaveCorrectInvalidProductStockIdCode() {
        // given
        ProductStockErrorCode errorCode = ProductStockErrorCode.INVALID_PRODUCT_STOCK_ID;

        // when & then
        assertThat(errorCode.getCode()).isEqualTo("STOCK-002");
        assertThat(errorCode.getHttpStatus()).isEqualTo(400);
        assertThat(errorCode.getMessage()).contains("유효하지 않은");
    }

    @Test
    @DisplayName("NOT_ENOUGH_STOCK 에러 코드 확인")
    void shouldHaveCorrectNotEnoughStockCode() {
        // given
        ProductStockErrorCode errorCode = ProductStockErrorCode.NOT_ENOUGH_STOCK;

        // when & then
        assertThat(errorCode.getCode()).isEqualTo("STOCK-003");
        assertThat(errorCode.getHttpStatus()).isEqualTo(400);
        assertThat(errorCode.getMessage()).contains("부족");
    }

    @Test
    @DisplayName("STOCK_OVERFLOW 에러 코드 확인")
    void shouldHaveCorrectStockOverflowCode() {
        // given
        ProductStockErrorCode errorCode = ProductStockErrorCode.STOCK_OVERFLOW;

        // when & then
        assertThat(errorCode.getCode()).isEqualTo("STOCK-004");
        assertThat(errorCode.getHttpStatus()).isEqualTo(400);
        assertThat(errorCode.getMessage()).contains("초과");
    }

    @Test
    @DisplayName("INVALID_STOCK_QUANTITY 에러 코드 확인")
    void shouldHaveCorrectInvalidStockQuantityCode() {
        // given
        ProductStockErrorCode errorCode = ProductStockErrorCode.INVALID_STOCK_QUANTITY;

        // when & then
        assertThat(errorCode.getCode()).isEqualTo("STOCK-005");
        assertThat(errorCode.getHttpStatus()).isEqualTo(400);
        assertThat(errorCode.getMessage()).contains("유효하지 않은");
    }
}
