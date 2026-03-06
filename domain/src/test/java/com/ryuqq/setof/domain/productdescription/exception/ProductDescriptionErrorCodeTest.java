package com.ryuqq.setof.domain.productdescription.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductDescriptionErrorCode 테스트")
class ProductDescriptionErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND)
                    .isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("상세설명 관련 에러 코드 테스트")
    class DescriptionErrorCodesTest {

        @Test
        @DisplayName("DESCRIPTION_NOT_FOUND 에러 코드를 검증한다")
        void descriptionNotFound() {
            // then
            assertThat(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND.getCode())
                    .isEqualTo("DESC-001");
            assertThat(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
            assertThat(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND.getMessage())
                    .isEqualTo("상세설명을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductDescriptionErrorCode.values())
                    .containsExactly(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND);
        }

        @Test
        @DisplayName("valueOf()로 enum 상수를 조회한다")
        void valueOfReturnsCorrectConstant() {
            // when
            ProductDescriptionErrorCode code =
                    ProductDescriptionErrorCode.valueOf("DESCRIPTION_NOT_FOUND");

            // then
            assertThat(code).isEqualTo(ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND);
        }
    }
}
