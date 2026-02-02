package com.ryuqq.setof.domain.brand.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandErrorCode 테스트")
class BrandErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(BrandErrorCode.BRAND_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("BRAND_NOT_FOUND 테스트")
    class BrandNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'BRD-001'이다")
        void hasCorrectCode() {
            assertThat(BrandErrorCode.BRAND_NOT_FOUND.getCode()).isEqualTo("BRD-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(BrandErrorCode.BRAND_NOT_FOUND.getHttpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("메시지는 '브랜드를 찾을 수 없습니다'이다")
        void hasCorrectMessage() {
            assertThat(BrandErrorCode.BRAND_NOT_FOUND.getMessage()).isEqualTo("브랜드를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("BRAND_DUPLICATE 테스트")
    class BrandDuplicateTest {

        @Test
        @DisplayName("에러 코드는 'BRD-002'이다")
        void hasCorrectCode() {
            assertThat(BrandErrorCode.BRAND_DUPLICATE.getCode()).isEqualTo("BRD-002");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 409이다")
        void hasCorrectHttpStatus() {
            assertThat(BrandErrorCode.BRAND_DUPLICATE.getHttpStatus()).isEqualTo(409);
        }

        @Test
        @DisplayName("메시지는 '동일한 브랜드가 이미 존재합니다'이다")
        void hasCorrectMessage() {
            assertThat(BrandErrorCode.BRAND_DUPLICATE.getMessage()).isEqualTo("동일한 브랜드가 이미 존재합니다");
        }
    }

    @Nested
    @DisplayName("BRAND_INACTIVE 테스트")
    class BrandInactiveTest {

        @Test
        @DisplayName("에러 코드는 'BRD-003'이다")
        void hasCorrectCode() {
            assertThat(BrandErrorCode.BRAND_INACTIVE.getCode()).isEqualTo("BRD-003");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(BrandErrorCode.BRAND_INACTIVE.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '비활성화된 브랜드입니다'이다")
        void hasCorrectMessage() {
            assertThat(BrandErrorCode.BRAND_INACTIVE.getMessage()).isEqualTo("비활성화된 브랜드입니다");
        }
    }

    @Nested
    @DisplayName("INVALID_DISPLAY_ORDER 테스트")
    class InvalidDisplayOrderTest {

        @Test
        @DisplayName("에러 코드는 'BRD-004'이다")
        void hasCorrectCode() {
            assertThat(BrandErrorCode.INVALID_DISPLAY_ORDER.getCode()).isEqualTo("BRD-004");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(BrandErrorCode.INVALID_DISPLAY_ORDER.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '표시 순서는 0 이상이어야 합니다'이다")
        void hasCorrectMessage() {
            assertThat(BrandErrorCode.INVALID_DISPLAY_ORDER.getMessage())
                    .isEqualTo("표시 순서는 0 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(BrandErrorCode.values())
                    .containsExactly(
                            BrandErrorCode.BRAND_NOT_FOUND,
                            BrandErrorCode.BRAND_DUPLICATE,
                            BrandErrorCode.BRAND_INACTIVE,
                            BrandErrorCode.INVALID_DISPLAY_ORDER);
        }
    }
}
