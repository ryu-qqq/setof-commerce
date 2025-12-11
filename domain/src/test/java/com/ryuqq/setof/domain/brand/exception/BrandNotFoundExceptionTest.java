package com.ryuqq.setof.domain.brand.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** BrandNotFoundException 단위 테스트 */
@DisplayName("BrandNotFoundException 단위 테스트")
class BrandNotFoundExceptionTest {

    @Nested
    @DisplayName("brandId 생성자")
    class BrandIdConstructor {

        @Test
        @DisplayName("성공 - brandId로 예외를 생성한다")
        void shouldCreateWithBrandId() {
            // Given
            Long brandId = 1L;

            // When
            BrandNotFoundException exception = new BrandNotFoundException(brandId);

            // Then
            assertThat(exception.getMessage()).contains("브랜드를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getErrorCode()).isEqualTo(BrandErrorCode.BRAND_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("brandCode 생성자")
    class BrandCodeConstructor {

        @Test
        @DisplayName("성공 - brandCode로 예외를 생성한다")
        void shouldCreateWithBrandCode() {
            // Given
            String brandCode = "NIKE001";

            // When
            BrandNotFoundException exception = new BrandNotFoundException(brandCode);

            // Then
            assertThat(exception.getMessage()).contains("브랜드를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("NIKE001");
        }
    }
}
