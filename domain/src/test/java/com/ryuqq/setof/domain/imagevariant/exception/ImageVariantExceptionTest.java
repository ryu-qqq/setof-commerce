package com.ryuqq.setof.domain.imagevariant.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantException 테스트")
class ImageVariantExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ImageVariantException exception =
                    new ImageVariantException(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("이미지 Variant를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("IMGVAR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ImageVariantException exception =
                    new ImageVariantException(
                            ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND, "ID 99 Variant 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 99 Variant 없음");
            assertThat(exception.code()).isEqualTo("IMGVAR-001");
        }

        @Test
        @DisplayName("ErrorCode, 커스텀 메시지, args로 예외를 생성한다")
        void createWithErrorCodeMessageAndArgs() {
            // given
            Map<String, Object> args = Map.of("imageVariantId", 99L);

            // when
            ImageVariantException exception =
                    new ImageVariantException(
                            ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND,
                            "이미지 Variant를 찾을 수 없습니다: 99",
                            args);

            // then
            assertThat(exception.getMessage()).contains("99");
            assertThat(exception.code()).isEqualTo("IMGVAR-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ImageVariantException exception =
                    new ImageVariantException(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("IMGVAR-001");
        }
    }

    @Nested
    @DisplayName("ImageVariantNotFoundException 테스트")
    class NotFoundExceptionTest {

        @Test
        @DisplayName("ID를 포함한 메시지로 예외를 생성한다")
        void createWithImageVariantId() {
            // when
            ImageVariantNotFoundException exception = new ImageVariantNotFoundException(42L);

            // then
            assertThat(exception.code()).isEqualTo("IMGVAR-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("42");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ImageVariantException은 DomainException을 상속한다")
        void imageVariantExceptionExtendsDomainException() {
            // given
            ImageVariantException exception =
                    new ImageVariantException(ImageVariantErrorCode.IMAGE_VARIANT_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ImageVariantNotFoundException은 ImageVariantException을 상속한다")
        void notFoundExceptionExtendsImageVariantException() {
            // given
            ImageVariantNotFoundException exception = new ImageVariantNotFoundException(1L);

            // then
            assertThat(exception).isInstanceOf(ImageVariantException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
