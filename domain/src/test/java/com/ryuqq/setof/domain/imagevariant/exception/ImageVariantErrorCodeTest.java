package com.ryuqq.setof.domain.imagevariant.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantErrorCode 테스트")
class ImageVariantErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            assertThat(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("에러 코드 값 테스트")
    class ErrorCodeValuesTest {

        @Test
        @DisplayName("IMAGE_VARIANT_NOT_FOUND 에러 코드를 검증한다")
        void imageVariantNotFound() {
            assertThat(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND.getCode())
                    .isEqualTo("IMGVAR-001");
            assertThat(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
            assertThat(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND.getMessage())
                    .isEqualTo("이미지 Variant를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            assertThat(ImageVariantErrorCode.values())
                    .containsExactly(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND);
        }
    }
}
